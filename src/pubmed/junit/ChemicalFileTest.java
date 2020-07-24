
package pubmed.junit;

import java.util.List;

import pubmed.article.PMID;
import pubmed.bulk.BulkFile;
import pubmed.bulk.ChemicalFile;
import pubmed.flat.ChemicalRecord;
import pubmed.flat.ChemicalTable;
import pubmed.mesh.MeshRecordKey;

import org.junit.*;
import static org.junit.Assert.*;

public class ChemicalFileTest {
    private static final BulkFile bulkFile = BulkFile.create("data/test/pubmed_sample.xml");
    private static final ChemicalFile chemicalFile = ChemicalFile.from(bulkFile);

    @Test public void testFile() {
        System.out.println(chemicalFile.getBulkFile());
        System.out.println(chemicalFile.getFlatFile());
        System.out.println(chemicalFile.getGZipFile());

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
        org.junit.runner.JUnitCore.main("pubmed.junit.ChemicalFileTest");
    }
}
