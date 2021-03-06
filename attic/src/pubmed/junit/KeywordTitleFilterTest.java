
package pubmed.junit;

import pubmed.article.PubmedArticle;
import pubmed.article.PubmedXmlDocument;
import pubmed.filter.KeywordTitleFilter;
import pubmed.subject.MeshSubject;

import org.junit.*;
import static org.junit.Assert.*;

public class KeywordTitleFilterTest {
    private static final KeywordTitleFilter atorvastatinFilter =
        KeywordTitleFilter.create(MeshSubject.create("D000069059"));

    private static final KeywordTitleFilter cancerFilter =
        KeywordTitleFilter.create(MeshSubject.NEOPLASMS);

    private static final KeywordTitleFilter hematomaFilter =
        KeywordTitleFilter.create(MeshSubject.create("D006406"));

    private static final KeywordTitleFilter nivolumabFilter =
        KeywordTitleFilter.create(MeshSubject.create("D000077594"));

    private static final String nivolumabFile = "data/test/29355075.xml";
    private static final String atorvastatinFile = "data/test/30073290.xml";

    private static final PubmedArticle atorvastatinArticle =
        PubmedXmlDocument.parse(atorvastatinFile).viewLatest().get(0);

    private static final PubmedArticle nivolumabArticle =
        PubmedXmlDocument.parse(nivolumabFile).viewLatest().get(0);

    @Test public void testFilter() {
        assertTrue(atorvastatinFilter.test(atorvastatinArticle));
        assertFalse(atorvastatinFilter.test(nivolumabArticle));

        assertTrue(nivolumabFilter.test(nivolumabArticle));
        assertFalse(nivolumabFilter.test(atorvastatinArticle));

        assertTrue(hematomaFilter.test(atorvastatinArticle));
        assertFalse(hematomaFilter.test(nivolumabArticle));

        assertTrue(cancerFilter.test(nivolumabArticle));
        assertFalse(cancerFilter.test(atorvastatinArticle));
    }

    @Test public void testScore() {
        assertEquals(1, atorvastatinFilter.score(atorvastatinArticle));
        assertEquals(0, atorvastatinFilter.score(nivolumabArticle));

        assertEquals(1, nivolumabFilter.score(nivolumabArticle));
        assertEquals(0, nivolumabFilter.score(atorvastatinArticle));

        assertEquals(1, hematomaFilter.score(atorvastatinArticle));
        assertEquals(0, hematomaFilter.score(nivolumabArticle));

        assertEquals(1, cancerFilter.score(nivolumabArticle));
        assertEquals(0, cancerFilter.score(atorvastatinArticle));
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("pubmed.junit.KeywordTitleFilterTest");
    }
}
