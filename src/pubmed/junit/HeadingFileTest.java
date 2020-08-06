
package pubmed.junit;

import java.util.HashSet;
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

        assertRecords(table.select(PMID.instance(31383582)),
                      "31383582|D001782|-", "31383582|D006801|-");

        assertRecords(table.select(MeshDescriptorKey.instance("D051059")),
                      "24451147|D051059|Q000235", "24451147|D051059|Q000378");

        assertRecords(table.select(MeshQualifierKey.instance("Q000494")),
                      "24451147|D006538|Q000494",
                      "24451147|D019161|Q000494",
                      "24451147|D011758|Q000494");

        assertRecords(table.select(PMID.instance(24451147), MeshDescriptorKey.instance("D002352")),
                      "24451147|D002352|Q000037",
                      "24451147|D002352|Q000235",
                      "24451147|D002352|Q000378");

        assertRecords(table.select(PMID.instance(1), MeshDescriptorKey.instance("D000818")), "1|D000818|-");

        assertTrue(table.select(PMID.instance(1), MeshDescriptorKey.instance("D002352")).isEmpty());
        assertTrue(table.select(PMID.instance(24451147), MeshDescriptorKey.instance("D007700")).isEmpty());

        assertTrue(table.contains(PMID.instance(1)));
        assertFalse(table.contains(PMID.instance(2)));

        assertTrue(table.contains(MeshDescriptorKey.instance("D007700")));
        assertFalse(table.contains(MeshDescriptorKey.instance("D007777")));

        assertTrue(table.contains(MeshQualifierKey.instance("Q000037")));
        assertFalse(table.contains(MeshQualifierKey.instance("Q000999")));

        assertTrue(headingFile.delete());
    }

    private void assertRecords(Set<HeadingRecord> actualSet, String... expectedStrings) {
        Set<HeadingRecord> expectedSet = new HashSet<HeadingRecord>();

        for (String s : expectedStrings)
            expectedSet.add(HeadingRecord.parse(s));

        assertEquals(expectedSet, actualSet);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("pubmed.junit.HeadingFileTest");
    }
}
