
package pubmed.junit;

import java.io.File;
import java.util.List;

import pubmed.article.ISSN;
import pubmed.article.PMID;
import pubmed.article.PubmedJournal;
import pubmed.flat.JournalFile;
import pubmed.flat.JournalRecord;
import pubmed.flat.JournalTable;

import org.junit.*;
import static org.junit.Assert.*;

public class JournalFileTest {
    private static final File sampleXml = new File("data/test/pubmed_sample.xml");
    private static final JournalFile journalFile = JournalFile.from(sampleXml);

    @Test public void testFile() {
        journalFile.processFile(true);
        journalFile.processFile(true);

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
