
package pubmed.bulk;

import java.util.List;

import pubmed.article.PMID;
import pubmed.flat.ChemicalRecord;
import pubmed.flat.ChemicalTable;
import pubmed.mesh.MeshRecordKey;

import org.junit.*;
import static org.junit.Assert.*;

public class ChemicalFileTest {
    private static final BulkFile bulkFile = BulkFile.create("data/test/pubmed_sample.xml");

    @Test public void testProcess() {
        ChemicalFile chemicalFile = ChemicalFile.instance(bulkFile);

        chemicalFile.processFile(true);
        chemicalFile.processFile(false);

        ChemicalTable table = chemicalFile.load();

        assertEquals(16, table.count());
        assertEquals(12, table.countPrimary(PMID.instance(24451147)));
        assertEquals( 4, table.countPrimary(PMID.instance(1)));

        List<ChemicalRecord> records = table.selectPrimary(PMID.instance(1));

        assertEquals(4, records.size());
        assertEquals(MeshRecordKey.instance("D005561"), records.get(0).getForeignKey());
        assertEquals(MeshRecordKey.instance("D000432"), records.get(3).getMeshKey());

        assertTrue(chemicalFile.delete());
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("pubmed.bulk.ChemicalFileTest");
    }
}
