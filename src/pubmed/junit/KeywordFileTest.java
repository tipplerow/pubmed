
package pubmed.junit;

import java.io.File;
import java.util.List;

import pubmed.article.PMID;
import pubmed.flat.KeywordFile;
import pubmed.flat.KeywordRecord;
import pubmed.flat.KeywordTable;

import org.junit.*;
import static org.junit.Assert.*;

public class KeywordFileTest {
    private static final File sampleXml = new File("data/test/pubmed_sample.xml");
    private static final KeywordFile keywordFile = KeywordFile.from(sampleXml);

    @Test public void testFile() {
        keywordFile.processFile(true);
        keywordFile.processFile(true);

        KeywordTable table = keywordFile.load();

        assertEquals(13, table.count());
        assertEquals( 5, table.countPrimary(PMID.instance(31383287)));
        assertEquals( 8, table.countPrimary(PMID.instance(31687927)));

        List<KeywordRecord> records = table.selectPrimary(PMID.instance(31383287));

        assertEquals(5, records.size());
        assertEquals("glioblastoma cell line", records.get(1).getKeyword());
        assertEquals("t98g", records.get(4).getKeyword());
 
        assertTrue(keywordFile.delete());
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("pubmed.junit.KeywordFileTest");
    }
}
