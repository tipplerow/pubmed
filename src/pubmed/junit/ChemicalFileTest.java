
package pubmed.junit;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ListMultimap;

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

    @Test public void testMap() {
        ChemicalFile chemicalFile = ChemicalFile.from(bulkFile);
        assertFalse(chemicalFile.exists());

        ListMultimap<PMID, ChemicalRecord> recordMap = chemicalFile.getRecordMap();

        // File is created on demand...
        assertTrue(chemicalFile.exists());
        assertEquals(List.of(PMID.instance(24451147), PMID.instance(1)), new ArrayList<PMID>(recordMap.keySet()));

        List<ChemicalRecord> recordList =
            new ArrayList<ChemicalRecord>(recordMap.get(PMID.instance(1)));

        assertEquals(4, recordList.size());
        assertEquals(MeshRecordKey.instance("D005561"), recordList.get(0).getMeshKey());
        assertEquals(MeshRecordKey.instance("D002245"), recordList.get(1).getMeshKey());
        assertEquals(MeshRecordKey.instance("D000445"), recordList.get(2).getMeshKey());
        assertEquals(MeshRecordKey.instance("D000432"), recordList.get(3).getMeshKey());
        
        assertTrue(chemicalFile.delete());
    }

    @Test public void testProcess() {
        ChemicalFile chemicalFile = ChemicalFile.from(bulkFile);

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
