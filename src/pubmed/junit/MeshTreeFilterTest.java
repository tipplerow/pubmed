
package pubmed.junit;

import java.util.List;

import pubmed.article.PMID;
import pubmed.article.PubmedArticle;
import pubmed.article.PubmedXmlDocument;
import pubmed.filter.MeshTreeFilter;
import pubmed.mesh.MeshTreeNumber;

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

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("pubmed.junit.MeshTreeFilterTest");
    }
}
