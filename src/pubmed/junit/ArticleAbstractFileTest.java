
package pubmed.junit;

import java.io.File;

import pubmed.article.PMID;
import pubmed.flat.ArticleAbstractFile;
import pubmed.flat.ArticleAbstractRecord;
import pubmed.flat.ArticleAbstractTable;

import org.junit.*;
import static org.junit.Assert.*;

public class ArticleAbstractFileTest {
    private static final File sampleXml = new File("data/test/pubmed_sample.xml");
    private static final ArticleAbstractFile abstractFile = ArticleAbstractFile.from(sampleXml);

    @Test public void testFile() {
        abstractFile.processFile(true);
        abstractFile.processFile(true);

        ArticleAbstractTable table = abstractFile.load();
        ArticleAbstractRecord record = table.select(PMID.instance(31383387));

        assertEquals(4, table.count());
        assertTrue(table.contains(PMID.instance(24451147)));
        assertFalse(table.contains(PMID.instance(1)));

        assertTrue(record.getAbstract().startsWith("Pain-related affective"));
        assertTrue(record.getAbstract().endsWith("receiving conservative treatment."));

        assertTrue(abstractFile.delete());
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("pubmed.junit.ArticleAbstractFileTest");
    }
}
