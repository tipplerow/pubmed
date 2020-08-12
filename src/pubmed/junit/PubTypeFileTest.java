
package pubmed.junit;

import java.util.List;

import pubmed.article.PMID;
import pubmed.article.PublicationType;
import pubmed.bulk.BulkFile;
import pubmed.bulk.PubTypeFile;
import pubmed.flat.PubTypeRecord;
import pubmed.flat.PubTypeTable;

import org.junit.*;
import static org.junit.Assert.*;

public class PubTypeFileTest {
    private static final BulkFile bulkFile = BulkFile.create("data/test/pubmed_sample.xml");

    @Test public void testProcess() {
        PubTypeFile pubTypeFile = PubTypeFile.from(bulkFile);

        pubTypeFile.processFile(true);
        pubTypeFile.processFile(false);

        PubTypeTable table = pubTypeFile.load();

        assertEquals(9, table.count());
        assertEquals(2, table.countPrimary(PMID.instance(24451147)));
        assertEquals(1, table.countPrimary(PMID.instance(31383287)));

        List<PubTypeRecord> records = table.selectPrimary(PMID.instance(24451147));

        assertEquals(2, records.size());
        assertEquals("Journal Article", records.get(0).getType().getTypeName());
        assertEquals("D013485", records.get(1).getType().getDescriptorKey().getKey());

        assertTrue(pubTypeFile.delete());
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("pubmed.junit.PubTypeFileTest");
    }
}
