
package pubmed.junit;

import java.util.List;

import pubmed.article.PMID;
import pubmed.article.PubmedArticle;
import pubmed.article.PubmedXmlDocument;
import pubmed.filter.ChemicalListFilter;
import pubmed.mesh.MeshRecordKey;

import org.junit.*;
import static org.junit.Assert.*;

public class ChemicalListFilterTest {
    private static final String SAMPLE_XML = "data/test/pubmed_sample.xml";

    private static final MeshRecordKey key1 = MeshRecordKey.instance("C485118");
    private static final MeshRecordKey key2 = MeshRecordKey.instance("D006538");
    private static final MeshRecordKey key3 = MeshRecordKey.instance("D000003");

    private static final PubmedXmlDocument document = PubmedXmlDocument.parse(SAMPLE_XML);
    private static final List<PubmedArticle> articles = document.viewLatest();

    @Test public void testFilter() {
        ChemicalListFilter filter1 = ChemicalListFilter.create(key1);
        ChemicalListFilter filter2 = ChemicalListFilter.create(key2);
        ChemicalListFilter filter3 = ChemicalListFilter.create(key3);
        ChemicalListFilter filter123 = ChemicalListFilter.create(key1, key2, key3);

        assertTrue(filter1.test(articles.get(0)));
        assertTrue(filter2.test(articles.get(0)));
        assertFalse(filter3.test(articles.get(0)));
        assertTrue(filter123.test(articles.get(0)));

        assertFalse(filter1.test(articles.get(1)));
        assertFalse(filter2.test(articles.get(1)));
        assertFalse(filter3.test(articles.get(1)));
        assertFalse(filter123.test(articles.get(1)));
    }

    @Test public void testScore() {
        ChemicalListFilter filter1 = ChemicalListFilter.create(key1);
        ChemicalListFilter filter2 = ChemicalListFilter.create(key2);
        ChemicalListFilter filter3 = ChemicalListFilter.create(key3);
        ChemicalListFilter filter123 = ChemicalListFilter.create(key1, key2, key3);

        assertEquals( 1, filter1.score(articles.get(0)));
        assertEquals( 1, filter2.score(articles.get(0)));
        assertEquals(-1, filter3.score(articles.get(0)));
        assertEquals( 1, filter123.score(articles.get(0)));

        assertEquals(0, filter1.score(articles.get(1)));
        assertEquals(0, filter2.score(articles.get(1)));
        assertEquals(0, filter3.score(articles.get(1)));
        assertEquals(0, filter123.score(articles.get(1)));
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("pubmed.junit.ChemicalListFilterTest");
    }
}
