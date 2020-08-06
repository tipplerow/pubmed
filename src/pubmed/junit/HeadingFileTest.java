
package pubmed.junit;

import java.util.Set;

import com.google.common.collect.Multimap;

import pubmed.article.PMID;
import pubmed.bulk.BulkFile;
import pubmed.bulk.HeadingFile;
import pubmed.flat.HeadingRecord;
import pubmed.flat.HeadingTable;
import pubmed.mesh.MeshDescriptorKey;
import pubmed.mesh.MeshQualifierKey;

import org.junit.*;
import static org.junit.Assert.*;

public class HeadingFileTest {
    private static final BulkFile bulkFile = BulkFile.create("data/test/pubmed_sample.xml");
    /*
    @Test public void testMap() {
        HeadingFile headingFile = HeadingFile.from(bulkFile);
        assertFalse(headingFile.exists());

        Multimap<PMID, HeadingRecord> recordMap = headingFile.getRecordMap();

        // File is created on demand...
        assertTrue(headingFile.exists());
        assertEquals(List.of(PMID.instance(24451147), PMID.instance(1)), new ArrayList<PMID>(recordMap.keySet()));

        List<HeadingRecord> recordList =
            new ArrayList<HeadingRecord>(recordMap.get(PMID.instance(1)));

        assertEquals(4, recordList.size());
        assertEquals(MeshRecordKey.instance("D005561"), recordList.get(0).getMeshKey());
        assertEquals(MeshRecordKey.instance("D002245"), recordList.get(1).getMeshKey());
        assertEquals(MeshRecordKey.instance("D000445"), recordList.get(2).getMeshKey());
        assertEquals(MeshRecordKey.instance("D000432"), recordList.get(3).getMeshKey());
        
        assertTrue(headingFile.delete());
    }
    */
    @Test public void testProcess() {
        HeadingFile headingFile = HeadingFile.from(bulkFile);

        headingFile.processFile(true);
        headingFile.processFile(false);

        HeadingTable table = headingFile.load();

        assertEquals(44, table.count());

        assertEquals(Set.of(PMID.instance(1), PMID.instance(24451147), PMID.instance(31383582)), table.pmidSet());
        assertEquals(34, table.descriptorKeySet().size());
        assertEquals(Set.of(MeshQualifierKey.instance("Q000009"),
                            MeshQualifierKey.instance("Q000032"),
                            MeshQualifierKey.instance("Q000037"),
                            MeshQualifierKey.instance("Q000097"),
                            MeshQualifierKey.instance("Q000187"),
                            MeshQualifierKey.instance("Q000188"),
                            MeshQualifierKey.instance("Q000201"),
                            MeshQualifierKey.instance("Q000235"),
                            MeshQualifierKey.instance("Q000378"),
                            MeshQualifierKey.instance("Q000473"),
                            MeshQualifierKey.instance("Q000494"),
                            MeshQualifierKey.instance("Q000506"),
                            MeshQualifierKey.instance("Q000737")),
                     table.qualifierKeySet());

        HeadingRecord rec1 = HeadingRecord.parse("31383582|D001782|-");
        HeadingRecord rec2 = HeadingRecord.parse("31383582|D006801|-");

        assertEquals(Set.of(rec1, rec2), table.select(PMID.instance(31383582)));

        HeadingRecord rec3 = HeadingRecord.parse("24451147|D051059|Q000235");
        HeadingRecord rec4 = HeadingRecord.parse("24451147|D051059|Q000378");

        assertEquals(Set.of(rec3, rec4), table.select(MeshDescriptorKey.instance("D051059")));

        HeadingRecord rec5 = HeadingRecord.parse("24451147|D006538|Q000494");
        HeadingRecord rec6 = HeadingRecord.parse("24451147|D019161|Q000494");
        HeadingRecord rec7 = HeadingRecord.parse("24451147|D011758|Q000494");

        assertEquals(Set.of(rec5, rec6, rec7), table.select(MeshQualifierKey.instance("Q000494")));

        assertTrue(headingFile.delete());
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("pubmed.junit.HeadingFileTest");
    }
}
