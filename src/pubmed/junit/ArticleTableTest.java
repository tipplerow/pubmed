
package pubmed.junit;

import java.util.List;

import pubmed.article.PMID;
import pubmed.article.PubmedArticle;
import pubmed.article.PubmedXmlDocument;
import pubmed.sql.ArticleRecord;
import pubmed.sql.ArticleTable;
import pubmed.sql.DbManager;

import org.junit.*;
import static org.junit.Assert.*;

public class ArticleTableTest {
    private static final String SAMPLE_XML = "data/test/pubmed_sample.xml";

    private static final PubmedXmlDocument document = PubmedXmlDocument.parse(SAMPLE_XML);
    private static final List<PubmedArticle> articles = document.viewLatest();

    private static final PubmedArticle article1 = articles.get(0);
    private static final PubmedArticle article2 = articles.get(1);
    private static final PubmedArticle article3 = articles.get(2);
    private static final PubmedArticle article4 = articles.get(3);
    private static final PubmedArticle article5 = articles.get(4);

    private static final ArticleRecord record1 = ArticleRecord.create(article1);
    private static final ArticleRecord record2 = ArticleRecord.create(article2);
    private static final ArticleRecord record3 = ArticleRecord.create(article3);
    private static final ArticleRecord record4 = ArticleRecord.create(article4);
    private static final ArticleRecord record5 = ArticleRecord.create(article5);

    private static final PMID key1 = record1.getPMID();
    private static final PMID key2 = record2.getPMID();
    private static final PMID key3 = record3.getPMID();
    private static final PMID key4 = record4.getPMID();
    private static final PMID key5 = record5.getPMID();

    static {
        DbManager.useTest();
    }

    @Test public void testInsertAdd() {
        ArticleTable.dropTable();
        ArticleTable table = ArticleTable.instance();

        assertFalse(table.contains(key1));
        assertFalse(table.contains(key2));
        assertFalse(table.contains(key3));
        assertFalse(table.contains(key4));
        assertFalse(table.contains(key5));

        table.insertArticles(List.of(article1, article2, article3, article4));

        assertTrue(table.contains(key1));
        assertTrue(table.contains(key2));
        assertTrue(table.contains(key3));
        assertTrue(table.contains(key4));
        assertFalse(table.contains(key5));

        table.insertArticle(article5);

        assertTrue(table.contains(key1));
        assertTrue(table.contains(key2));
        assertTrue(table.contains(key3));
        assertTrue(table.contains(key4));
        assertTrue(table.contains(key5));

        table.deleteCitations(List.of(key1, key2));

        assertFalse(table.contains(key1));
        assertFalse(table.contains(key2));
        assertTrue(table.contains(key3));
        assertTrue(table.contains(key4));
        assertTrue(table.contains(key5));
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("pubmed.junit.ArticleTableTest");
    }
}
