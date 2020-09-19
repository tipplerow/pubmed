
package pubmed.junit;

import java.util.List;

import pubmed.article.PMID;
import pubmed.article.PubmedArticle;
import pubmed.article.PubmedXmlDocument;
import pubmed.filter.KeywordListFilter;
import pubmed.mesh.MeshRecordKey;

import org.junit.*;
import static org.junit.Assert.*;

public class KeywordListFilterTest {
    private static final String SAMPLE_XML = "data/test/pubmed_sample.xml";

    private static final PubmedXmlDocument document = PubmedXmlDocument.parse(SAMPLE_XML);
    private static final List<PubmedArticle> articles = document.viewLatest();

    @Test public void testFilter() {
        KeywordListFilter filter1 = KeywordListFilter.create("glioblastoma");
        KeywordListFilter filter2 = KeywordListFilter.create("rhinovirus");
        KeywordListFilter filter3 = KeywordListFilter.create("spheroid");

        assertFilter(filter1, false,  true, false, false, false);
        assertFilter(filter2, false, false, false, false, true);
        assertFilter(filter3, false, false, false, false, false);
    }

    private void assertFilter(KeywordListFilter filter, boolean... expected) {
        for (int index = 0; index < expected.length; ++index)
            assertEquals(expected[index], filter.test(articles.get(index)));
    }

    @Test public void testScore() {
        KeywordListFilter filter1 = KeywordListFilter.create("glioblastoma");
        KeywordListFilter filter2 = KeywordListFilter.create("rhinovirus");
        KeywordListFilter filter3 = KeywordListFilter.create("spheroid");

        assertScore(filter1, 0,  1, 0, 0, -1);
        assertScore(filter2, 0, -1, 0, 0,  1);
        assertScore(filter3, 0, -1, 0, 0, -1);
    }

    private void assertScore(KeywordListFilter filter, int... expected) {
        for (int index = 0; index < expected.length; ++index)
            assertEquals(expected[index], filter.score(articles.get(index)));
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("pubmed.junit.KeywordListFilterTest");
    }
}
