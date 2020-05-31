
package pubmed.junit;

import java.util.List;

import pubmed.mesh.MeshDescriptorName;
import pubmed.mesh.MeshDescriptorXmlDocument;
import pubmed.mesh.MeshQualifierName;
import pubmed.mesh.MeshQualifierXmlDocument;
import pubmed.mesh.MeshRecord;
import pubmed.mesh.MeshRecordKey;
import pubmed.mesh.MeshRecordName;
import pubmed.mesh.MeshSupplementalName;
import pubmed.mesh.MeshSupplementalXmlDocument;

import org.junit.*;
import static org.junit.Assert.*;

public class MeshRecordTest {
    private static MeshRecordKey descKey1 = MeshRecordKey.instance("D000001");
    private static MeshRecordKey descKey2 = MeshRecordKey.instance("D000002");
    private static MeshRecordKey descKey3 = MeshRecordKey.instance("D000003");
    private static MeshRecordKey qualKey1 = MeshRecordKey.instance("Q000037");
    private static MeshRecordKey qualKey2 = MeshRecordKey.instance("Q000819");
    private static MeshRecordKey suppKey1 = MeshRecordKey.instance("C117343");

    private static MeshRecordName descName1 = MeshDescriptorName.instance("Calcimycin");
    private static MeshRecordName qualName1 = MeshQualifierName.instance("antagonists & inhibitors");
    private static MeshRecordName suppName1 = MeshSupplementalName.instance("red yeast rice");

    private static List<MeshRecord> records;

    static {
        MeshDescriptorXmlDocument.parse("data/test/mesh_desc_sample.xml");
        MeshQualifierXmlDocument.parse("data/test/mesh_qual_sample.xml");
        MeshSupplementalXmlDocument.parse("data/test/mesh_supp_sample.xml");

        records = MeshRecord.list();
        records.sort(MeshRecord.KEY_COMPARATOR);
    }

    @Test public void testList() {
        assertEquals(6, records.size());
        assertEquals(suppKey1, records.get(0).getKey());
        assertEquals(descKey1, records.get(1).getKey());
        assertEquals(descKey2, records.get(2).getKey());
        assertEquals(descKey3, records.get(3).getKey());
        assertEquals(qualKey1, records.get(4).getKey());
        assertEquals(qualKey2, records.get(5).getKey());
    }

    @Test public void testLookupKey() {
        assertEquals(descKey1, MeshRecord.lookup(descKey1).getKey());
        assertEquals(qualKey1, MeshRecord.lookup(qualKey1).getKey());
        assertEquals(suppKey1, MeshRecord.lookup(suppKey1).getKey());
    }

    @Test public void testLookupName() {
        assertEquals(descKey1, MeshRecord.lookup(descName1).getKey());
        assertEquals(qualKey1, MeshRecord.lookup(qualName1).getKey());
        assertEquals(suppKey1, MeshRecord.lookup(suppName1).getKey());
    }

    @Test public void testLookupTerm() {
        assertEquals(descKey1, MeshRecord.lookup("Calcimycin").getKey());
        assertEquals(descKey1, MeshRecord.lookup("A-23187").getKey());
        assertEquals(descKey1, MeshRecord.lookup("A 23187").getKey());
        assertEquals(descKey1, MeshRecord.lookup("A23187").getKey());
        assertEquals(descKey1, MeshRecord.lookup("Antibiotic A23187").getKey());
        assertEquals(descKey1, MeshRecord.lookup("A23187, Antibiotic").getKey());
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("pubmed.junit.MeshRecordTest");
    }
}
