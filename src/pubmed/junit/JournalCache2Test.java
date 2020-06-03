
package pubmed.junit;

import java.util.List;

import pubmed.article.PubmedArticle;
import pubmed.article.PubmedXmlDocument;
import pubmed.medline.MedlineJournal;
import pubmed.medline.MedlineTA;
import pubmed.sql.DbManager;
import pubmed.sql.JournalCache;
import pubmed.sql.JournalTable;

import org.junit.*;
import static org.junit.Assert.*;

public class JournalCache2Test {
    private static final String SAMPLE_XML = "data/test/pubmed_sample.xml";

    private static final PubmedXmlDocument document = PubmedXmlDocument.parse(SAMPLE_XML);
    private static final List<PubmedArticle> articles = document.viewLatest();

    private static final MedlineJournal journal1 = articles.get(0).getMedlineJournal();
    private static final MedlineJournal journal2 = articles.get(1).getMedlineJournal();
    private static final MedlineJournal journal3 = articles.get(2).getMedlineJournal();
    private static final MedlineJournal journal4 = articles.get(3).getMedlineJournal();
    private static final MedlineJournal journal5 = articles.get(4).getMedlineJournal();

    private static final MedlineTA key1 = journal1.getMedlineTA();
    private static final MedlineTA key2 = journal2.getMedlineTA();
    private static final MedlineTA key3 = journal3.getMedlineTA();
    private static final MedlineTA key4 = journal4.getMedlineTA();
    private static final MedlineTA key5 = journal5.getMedlineTA();

    static {
        DbManager.useTest();
    }

    @Test public void testInsert() {
        JournalTable.dropTable();

        JournalCache cache = JournalCache.instance();
        JournalTable table = JournalTable.instance();

        assertFalse(table.contains(key1));
        assertFalse(table.contains(key2));
        assertFalse(table.contains(key3));
        assertFalse(table.contains(key4));
        assertFalse(table.contains(key5));

        assertFalse(cache.contains(key1));
        assertFalse(cache.contains(key2));
        assertFalse(cache.contains(key3));
        assertFalse(cache.contains(key4));
        assertFalse(cache.contains(key5));

        assertFalse(cache.contains(journal1));
        assertFalse(cache.contains(journal2));
        assertFalse(cache.contains(journal3));
        assertFalse(cache.contains(journal4));
        assertFalse(cache.contains(journal5));

        cache.addAll(List.of(journal1, journal2, journal3, journal2, journal1));

        assertTrue(table.contains(key1));
        assertTrue(table.contains(key2));
        assertTrue(table.contains(key3));
        assertFalse(table.contains(key4));
        assertFalse(table.contains(key5));

        assertTrue(cache.contains(key1));
        assertTrue(cache.contains(key2));
        assertTrue(cache.contains(key3));
        assertFalse(cache.contains(key4));
        assertFalse(cache.contains(key5));

        assertTrue(cache.contains(journal1));
        assertTrue(cache.contains(journal2));
        assertTrue(cache.contains(journal3));
        assertFalse(cache.contains(journal4));
        assertFalse(cache.contains(journal5));

        cache.addAll(List.of(journal1, journal2, journal3, journal4, journal5));

        assertTrue(table.contains(key1));
        assertTrue(table.contains(key2));
        assertTrue(table.contains(key3));
        assertTrue(table.contains(key4));
        assertTrue(table.contains(key5));

        assertTrue(cache.contains(key1));
        assertTrue(cache.contains(key2));
        assertTrue(cache.contains(key3));
        assertTrue(cache.contains(key4));
        assertTrue(cache.contains(key5));

        assertTrue(cache.contains(journal1));
        assertTrue(cache.contains(journal2));
        assertTrue(cache.contains(journal3));
        assertTrue(cache.contains(journal4));
        assertTrue(cache.contains(journal5));
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("pubmed.junit.JournalCache2Test");
    }
}
