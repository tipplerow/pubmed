
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

public class JournalCache1Test {
    private static final String SAMPLE_XML = "data/test/pubmed_sample.xml";

    private static final PubmedXmlDocument document = PubmedXmlDocument.parse(SAMPLE_XML);
    private static final List<PubmedArticle> articles = document.viewLatest();

    private static final MedlineJournal journal1 = articles.get(0).getMedlineJournal();
    private static final MedlineJournal journal2 = articles.get(1).getMedlineJournal();
    private static final MedlineJournal journal3 = articles.get(2).getMedlineJournal();

    private static final MedlineTA key1 = journal1.getMedlineTA();
    private static final MedlineTA key2 = journal2.getMedlineTA();
    private static final MedlineTA key3 = journal3.getMedlineTA();

    static {
        DbManager.useTest();
    }

    @Test public void testAddContainsFetch() {
        JournalTable.dropTable();

        JournalCache cache = JournalCache.instance();
        JournalTable table = JournalTable.instance();

        cache.add(journal1);
        cache.add(journal1);

        assertTrue(table.contains(key1));
        assertTrue(cache.contains(key1));
        assertTrue(cache.contains(journal1));

        assertFalse(table.contains(key2));
        assertFalse(cache.contains(key2));
        assertFalse(cache.contains(journal2));

        assertFalse(table.contains(key3));
        assertFalse(cache.contains(key3));
        assertFalse(cache.contains(journal3));

        assertEquals(journal1, table.fetch(key1));
        assertEquals(journal1, cache.fetch(key1));
        assertNull(table.fetch(key2));
        assertNull(cache.fetch(key2));
        assertNull(table.fetch(key3));
        assertNull(cache.fetch(key3));

        cache.add(journal2);
        cache.add(journal2);

        assertTrue(table.contains(key1));
        assertTrue(cache.contains(key1));
        assertTrue(cache.contains(journal1));

        assertTrue(table.contains(key2));
        assertTrue(cache.contains(key2));
        assertTrue(cache.contains(journal2));

        assertFalse(table.contains(key3));
        assertFalse(cache.contains(key3));
        assertFalse(cache.contains(journal3));

        cache.add(journal3);
        cache.add(journal3);

        assertTrue(table.contains(key1));
        assertTrue(cache.contains(key1));
        assertTrue(cache.contains(journal1));

        assertTrue(table.contains(key2));
        assertTrue(cache.contains(key2));
        assertTrue(cache.contains(journal2));

        assertTrue(table.contains(key3));
        assertTrue(cache.contains(key3));
        assertTrue(cache.contains(journal3));

        assertEquals(journal1, table.fetch(key1));
        assertEquals(journal1, cache.fetch(key1));
        assertEquals(journal2, table.fetch(key2));
        assertEquals(journal2, cache.fetch(key2));
        assertEquals(journal3, table.fetch(key3));
        assertEquals(journal3, cache.fetch(key3));
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("pubmed.junit.JournalCache1Test");
    }
}
