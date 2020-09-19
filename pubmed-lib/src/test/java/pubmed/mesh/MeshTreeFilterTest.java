
package pubmed.mesh;

import java.util.List;

import pubmed.article.PMID;
import pubmed.article.PubmedArticle;
import pubmed.article.PubmedXmlDocument;
import pubmed.filter.ArticleFilter;
import pubmed.filter.MeshTreeFilter;
import pubmed.subject.MeshSubject;

import org.junit.*;
import static org.junit.Assert.*;

public class MeshTreeFilterTest {
    private static final String SAMPLE_XML = "data/test/pubmed_sample.xml";

    private static final MeshTreeNumber num1 = MeshTreeNumber.instance("D12.776");
    private static final MeshTreeNumber num2 = MeshTreeNumber.instance("D12.776.097");
    private static final MeshTreeNumber num3 = MeshTreeNumber.instance("D12.776.157");
    private static final MeshTreeNumber num4 = MeshTreeNumber.instance("D12.776.157.247");

    private static final PubmedXmlDocument document = PubmedXmlDocument.parse(SAMPLE_XML);
    private static final List<PubmedArticle> articles = document.viewLatest();

    private static final PubmedArticle CRCArticle =
        PubmedXmlDocument.parse("data/test/31400583.xml").viewLatest().get(0);

    private static final PubmedArticle melanomaArticle =
        PubmedXmlDocument.parse("data/test/31408116.xml").viewLatest().get(0);

    private static final MeshSubject CRC = MeshSubject.instance("D015179");
    private static final MeshSubject FAP = MeshSubject.instance("D011125");

    private static final MeshSubject melanoma = MeshSubject.instance("D008545");
    private static final MeshSubject amelanomicMelanoma = MeshSubject.instance("D018328");

    @Test public void testFilter() {
        MeshTreeFilter filter1 = MeshTreeFilter.create(num1);
        MeshTreeFilter filter2 = MeshTreeFilter.create(num2);
        MeshTreeFilter filter3 = MeshTreeFilter.create(num3);
        MeshTreeFilter filter4 = MeshTreeFilter.create(num4);

        assertTrue(filter1.test(articles.get(0)));
        assertFalse(filter2.test(articles.get(0)));
        assertTrue(filter3.test(articles.get(0)));
        assertFalse(filter4.test(articles.get(0)));
    }

    @Test public void testScore() {
        MeshTreeFilter filter1 = MeshTreeFilter.create(num1);
        MeshTreeFilter filter2 = MeshTreeFilter.create(num2);

        assertEquals( 1, filter1.score(articles.get(0)));
        assertEquals(-1, filter2.score(articles.get(0)));

        assertEquals(0, filter1.score(articles.get(1)));
        assertEquals(0, filter2.score(articles.get(1)));
    }

    @Test public void testSubjects() {
        ArticleFilter cancerFilter = MeshTreeFilter.create(MeshSubject.NEOPLASMS);

        ArticleFilter CRCFilter = MeshTreeFilter.create(CRC);
        ArticleFilter FAPFilter = MeshTreeFilter.create(FAP);

        ArticleFilter melanomaFilter = MeshTreeFilter.create(melanoma);
        ArticleFilter amelanomicMelanomaFilter = MeshTreeFilter.create(amelanomicMelanoma);

        assertTrue(cancerFilter.test(CRCArticle));
        assertTrue(CRCFilter.test(CRCArticle));
        assertFalse(FAPFilter.test(CRCArticle));
        assertFalse(melanomaFilter.test(CRCArticle));
        assertFalse(amelanomicMelanomaFilter.test(CRCArticle));

        assertTrue(cancerFilter.test(melanomaArticle));
        assertFalse(CRCFilter.test(melanomaArticle));
        assertFalse(FAPFilter.test(melanomaArticle));
        assertTrue(melanomaFilter.test(melanomaArticle));
        assertFalse(amelanomicMelanomaFilter.test(melanomaArticle));
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("pubmed.mesh.MeshTreeFilterTest");
    }
}
