
package pubmed.junit;

import java.time.LocalDate;
import java.util.List;

import pubmed.article.PMID;
import pubmed.bulk.BulkFile;
import pubmed.bulk.PubDateFile;
import pubmed.flat.PubDateRecord;
import pubmed.flat.PubDateTable;

import org.junit.*;
import static org.junit.Assert.*;

public class PubDateFileTest {
    private static final BulkFile bulkFile = BulkFile.create("data/test/pubmed_sample.xml");

    @Test public void testProcess() {
        PubDateFile pubDateFile = PubDateFile.from(bulkFile);

        pubDateFile.processFile(true);
        pubDateFile.processFile(false);

        PubDateTable table = pubDateFile.load();

        assertEquals(6, table.count());
        assertEquals(1, table.countPrimary(PMID.instance(24451147)));
        assertEquals(1, table.countPrimary(PMID.instance(1)));

        List<PubDateRecord> records = table.selectPrimary(PMID.instance(24451147));

        assertEquals(1, records.size());
        assertEquals(LocalDate.of(2014, 1, 22), records.get(0).getDate());

        assertTrue(pubDateFile.delete());
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("pubmed.junit.PubDateFileTest");
    }
}
