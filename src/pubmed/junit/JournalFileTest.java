
package pubmed.junit;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import pubmed.article.ISSN;
import pubmed.article.PMID;
import pubmed.article.PubmedJournal;
import pubmed.bulk.BulkFile;
import pubmed.bulk.JournalFile;
import pubmed.flat.JournalRecord;
import pubmed.flat.JournalTable;

import org.junit.*;
import static org.junit.Assert.*;

public class JournalFileTest {
    private static final BulkFile bulkFile = BulkFile.create("data/test/pubmed_sample.xml");

    @Test public void testMap() {
        JournalFile journalFile = JournalFile.from(bulkFile);
        assertFalse(journalFile.exists());

        Map<PMID, JournalRecord> recordMap = journalFile.getRecordMap();

        // File is created on demand...
        assertTrue(journalFile.exists());

        List<PMID> pmidList = new ArrayList<PMID>(recordMap.keySet());
        assertEquals(List.of(PMID.instance(24451147),
                             PMID.instance(31383287),
                             PMID.instance(31383387),
                             PMID.instance(1),
                             PMID.instance(31687927),
                             PMID.instance(31383582)), pmidList);
        
        assertTrue(journalFile.delete());
    }

    @Test public void testProcess() {
        JournalFile journalFile = JournalFile.from(bulkFile);

        journalFile.processFile(true);
        journalFile.processFile(false);

        JournalTable table = journalFile.load();

        assertEquals(6, table.count());
        assertEquals(1, table.countPrimary(PMID.instance(24451147)));
        assertEquals(1, table.countPrimary(PMID.instance(1)));
        assertEquals(1, table.countForeign(ISSN.instance("1460-2180")));

        List<JournalRecord> records = table.selectPrimary(PMID.instance(1));

        assertEquals(1, records.size());
        assertEquals("Biochemical medicine", records.get(0).getJournal().getTitle());

        records = table.selectForeign(ISSN.instance("1532-3072"));
        assertEquals(1, records.size());
        assertEquals("Tissue Cell", records.get(0).getJournal().getISOAbbreviation());

        assertTrue(journalFile.delete());
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("pubmed.junit.JournalFileTest");
    }
}
