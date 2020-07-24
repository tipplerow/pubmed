
package pubmed.junit;

import java.util.List;

import pubmed.article.PMID;
import pubmed.bulk.BulkFile;
import pubmed.bulk.KeywordFile;
import pubmed.flat.KeywordRecord;
import pubmed.flat.KeywordTable;

import org.junit.*;
import static org.junit.Assert.*;

public class KeywordFileTest {
    private static final BulkFile bulkFile = BulkFile.create("data/test/pubmed_sample.xml");
    private static final KeywordFile keywordFile = KeywordFile.from(bulkFile);

    @Test public void testFile() {
        keywordFile.processFile(true);
        keywordFile.processFile(false);

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
