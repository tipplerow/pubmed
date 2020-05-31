
package pubmed.junit;

import java.util.List;

import pubmed.article.PMID;
import pubmed.article.PubmedArticle;
import pubmed.article.PubmedXmlDocument;

import org.junit.*;
import static org.junit.Assert.*;

public class PubmedXmlDocumentTest {
    private static final String SAMPLE_XML = "data/test/pubmed_sample.xml";

    private static final PMID pmid1 = PMID.instance(24451147);
    private static final PMID pmid2 = PMID.instance(31383287);
    private static final PMID pmid3 = PMID.instance(31383387);
    private static final PMID pmid4 = PMID.instance(1);
    private static final PMID pmid5 = PMID.instance(31687927);

    private void assertDocument(PubmedXmlDocument document, PMID... pmids) {
        assertEquals(pmids.length, document.countLatest());
        assertArticles(document.viewLatest(), pmids);
    }

    private void assertArticles(List<PubmedArticle> articles, PMID... pmids) {
        assertEquals(pmids.length, articles.size());

        for (int index = 0; index < pmids.length; ++index)
            assertEquals(pmids[index], articles.get(index).getPMID());
    }

    @Test public void testOriginals() {
        PubmedXmlDocument document = PubmedXmlDocument.parse(SAMPLE_XML);
        assertArticles(document.getOriginals(), pmid1, pmid4, pmid5);
    }

    @Test public void testRevisions() {
        PubmedXmlDocument document = PubmedXmlDocument.parse(SAMPLE_XML);
        assertArticles(document.getRevisions(), pmid2, pmid3);
    }

    @Test public void testVersions() {
        PubmedXmlDocument document = PubmedXmlDocument.parse(SAMPLE_XML);

        assertEquals(2, document.getRevisions().get(0).getVersion());
        assertEquals(3, document.getRevisions().get(1).getVersion());
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("pubmed.junit.PubmedXmlDocumentTest");
    }
}
