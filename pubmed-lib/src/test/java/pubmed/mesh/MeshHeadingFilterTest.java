
package pubmed.mesh;

import java.util.List;

import pubmed.article.PMID;
import pubmed.article.PubmedArticle;
import pubmed.article.PubmedXmlDocument;
import pubmed.filter.ArticleFilter;
import pubmed.filter.MeshHeadingFilter;
import pubmed.subject.MeshSubject;

import org.junit.*;
import static org.junit.Assert.*;

public class MeshHeadingFilterTest {
    private static final String SAMPLE_XML = "data/test/pubmed_sample.xml";

    private static final MeshDescriptorKey key1 = MeshDescriptorKey.instance("D002465");
    private static final MeshDescriptorKey key2 = MeshDescriptorKey.instance("D006538");
    private static final MeshDescriptorKey key3 = MeshDescriptorKey.instance("D000003");

    private static final PubmedXmlDocument document = PubmedXmlDocument.parse(SAMPLE_XML);
    private static final List<PubmedArticle> articles = document.viewLatest();

    @Test public void testFilter() {
        MeshHeadingFilter filter1 = MeshHeadingFilter.create(key1);
        MeshHeadingFilter filter2 = MeshHeadingFilter.create(key2);
        MeshHeadingFilter filter3 = MeshHeadingFilter.create(key3);
        MeshHeadingFilter filter123 = MeshHeadingFilter.create(key1, key2, key3);

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
        MeshHeadingFilter filter1 = MeshHeadingFilter.create(key1);
        MeshHeadingFilter filter2 = MeshHeadingFilter.create(key2);
        MeshHeadingFilter filter3 = MeshHeadingFilter.create(key3);
        MeshHeadingFilter filter123 = MeshHeadingFilter.create(key1, key2, key3);

        assertEquals( 1, filter1.score(articles.get(0)));
        assertEquals( 1, filter2.score(articles.get(0)));
        assertEquals(-1, filter3.score(articles.get(0)));
        assertEquals( 1, filter123.score(articles.get(0)));

        assertEquals(0, filter1.score(articles.get(1)));
        assertEquals(0, filter2.score(articles.get(1)));
        assertEquals(0, filter3.score(articles.get(1)));
        assertEquals(0, filter123.score(articles.get(1)));
    }

    @Test public void testSubjects() {
        PubmedArticle CRCArticle =
            PubmedXmlDocument.parse("data/test/31400583.xml").viewLatest().get(0);

        PubmedArticle melanomaArticle =
            PubmedXmlDocument.parse("data/test/31408116.xml").viewLatest().get(0);

        MeshSubject CRC = MeshSubject.instance("D015179");
        MeshSubject FAP = MeshSubject.instance("D011125");

        MeshSubject melanoma = MeshSubject.instance("D008545");
        MeshSubject amelanomicMelanoma = MeshSubject.instance("D018328");

        ArticleFilter cancerFilter = MeshHeadingFilter.create(MeshSubject.NEOPLASMS);

        ArticleFilter CRCFilter = MeshHeadingFilter.create(CRC);
        ArticleFilter FAPFilter = MeshHeadingFilter.create(FAP);

        ArticleFilter melanomaFilter = MeshHeadingFilter.create(melanoma);
        ArticleFilter amelanomicMelanomaFilter = MeshHeadingFilter.create(amelanomicMelanoma);

        assertTrue(CRCFilter.test(CRCArticle));
        assertFalse(cancerFilter.test(CRCArticle));
        assertFalse(FAPFilter.test(CRCArticle));
        assertFalse(melanomaFilter.test(CRCArticle));
        assertFalse(amelanomicMelanomaFilter.test(CRCArticle));

        assertTrue(melanomaFilter.test(melanomaArticle));
        assertFalse(cancerFilter.test(melanomaArticle));
        assertFalse(CRCFilter.test(melanomaArticle));
        assertFalse(FAPFilter.test(melanomaArticle));
        assertFalse(amelanomicMelanomaFilter.test(melanomaArticle));
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("pubmed.mesh.MeshHeadingFilterTest");
    }
}
