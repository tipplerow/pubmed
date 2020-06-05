
package pubmed.junit;

import java.util.List;

import pubmed.article.PMID;
import pubmed.article.PubmedArticle;
import pubmed.article.PubmedXmlDocument;
import pubmed.sql.ChemicalRecord;
import pubmed.sql.ChemicalTable;
import pubmed.sql.DbEnv;

import org.junit.*;
import static org.junit.Assert.*;

public class ChemicalTableTest {
    private static final String SAMPLE_XML = "data/test/pubmed_sample.xml";

    private static final PubmedXmlDocument document = PubmedXmlDocument.parse(SAMPLE_XML);
    private static final List<PubmedArticle> articles = document.viewLatest();

    private static final PubmedArticle article1 = articles.get(0);
    private static final PubmedArticle article2 = articles.get(1);
    private static final PubmedArticle article3 = articles.get(2);
    private static final PubmedArticle article4 = articles.get(3);
    private static final PubmedArticle article5 = articles.get(4);

    private static final PMID pmid1 = article1.getPMID();
    private static final PMID pmid2 = article2.getPMID();
    private static final PMID pmid3 = article3.getPMID();
    private static final PMID pmid4 = article4.getPMID();
    private static final PMID pmid5 = article5.getPMID();

    static {
        DbEnv.TEST.use();
    }

    @Test public void testInsertAdd() {
        ChemicalTable.dropTable();
        ChemicalTable table = ChemicalTable.instance();

        table.insertArticles(List.of(article1, article2, article3, article5));

        assertEquals(12, table.fetchKey1(pmid1).size());
        assertEquals(0, table.fetchKey1(pmid2).size());
        assertEquals(0, table.fetchKey1(pmid3).size());
        assertEquals(0, table.fetchKey1(pmid4).size());
        assertEquals(0, table.fetchKey1(pmid5).size());

        assertTrue(table.containsKey1(pmid1));
        assertFalse(table.containsKey1(pmid2));
        assertFalse(table.containsKey1(pmid3));
        assertFalse(table.containsKey1(pmid4));
        assertFalse(table.containsKey1(pmid5));

        assertEquals(1, table.fetchKey2("C485118").size());
        assertEquals(0, table.fetchKey2("D000432").size());

        table.insertArticle(article4);

        assertEquals(12, table.fetchKey1(pmid1).size());
        assertEquals(0, table.fetchKey1(pmid2).size());
        assertEquals(0, table.fetchKey1(pmid3).size());
        assertEquals(4, table.fetchKey1(pmid4).size());
        assertEquals(0, table.fetchKey1(pmid5).size());

        assertTrue(table.containsKey1(pmid1));
        assertFalse(table.containsKey1(pmid2));
        assertFalse(table.containsKey1(pmid3));
        assertTrue(table.containsKey1(pmid4));
        assertFalse(table.containsKey1(pmid5));

        assertEquals(1, table.fetchKey2("C485118").size());
        assertEquals(1, table.fetchKey2("D000432").size());
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("pubmed.junit.ChemicalTableTest");
    }
}
