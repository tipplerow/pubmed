
package pubmed.junit;

import java.util.List;

import pubmed.article.PMID;
import pubmed.article.PubmedArticle;
import pubmed.article.PubmedXmlDocument;
import pubmed.sql.DbEnv;
import pubmed.sql.HeadingRecord;
import pubmed.sql.HeadingTable;

import org.junit.*;
import static org.junit.Assert.*;

public class HeadingTableTest {
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
        HeadingTable.dropTable();
        HeadingTable table = HeadingTable.instance();

        table.insertArticles(List.of(article1, article2, article3, article4, article5));
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("pubmed.junit.HeadingTableTest");
    }
}
