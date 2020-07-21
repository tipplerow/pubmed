
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
    private static MeshRecordKey descKey4 = MeshRecordKey.instance("D000077594");
    private static MeshRecordKey descKey5 = MeshRecordKey.instance("D006406");
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

    @Test public void testIsChemical() {
        assertTrue(MeshRecord.lookup(descKey1).isChemical());
        assertTrue(MeshRecord.lookup(descKey2).isChemical());
        assertTrue(MeshRecord.lookup(descKey4).isChemical());
        assertTrue(MeshRecord.lookup(suppKey1).isChemical());

        assertFalse(MeshRecord.lookup(descKey3).isChemical());
        assertFalse(MeshRecord.lookup(descKey5).isChemical());
        assertFalse(MeshRecord.lookup(qualKey1).isChemical());
        assertFalse(MeshRecord.lookup(qualKey2).isChemical());
    }

    @Test public void testIsDescriptor() {
        assertTrue(MeshRecord.lookup(descKey1).isDescriptor());
        assertTrue(MeshRecord.lookup(descKey2).isDescriptor());
        assertTrue(MeshRecord.lookup(descKey3).isDescriptor());
        assertTrue(MeshRecord.lookup(descKey4).isDescriptor());
        assertTrue(MeshRecord.lookup(descKey5).isDescriptor());

        assertFalse(MeshRecord.lookup(qualKey1).isDescriptor());
        assertFalse(MeshRecord.lookup(qualKey2).isDescriptor());
        assertFalse(MeshRecord.lookup(suppKey1).isDescriptor());
    }

    @Test public void testIsQualifier() {
        assertTrue(MeshRecord.lookup(qualKey1).isQualifier());
        assertTrue(MeshRecord.lookup(qualKey2).isQualifier());

        assertFalse(MeshRecord.lookup(descKey1).isQualifier());
        assertFalse(MeshRecord.lookup(descKey2).isQualifier());
        assertFalse(MeshRecord.lookup(descKey3).isQualifier());
        assertFalse(MeshRecord.lookup(descKey4).isQualifier());
        assertFalse(MeshRecord.lookup(descKey5).isQualifier());
        assertFalse(MeshRecord.lookup(suppKey1).isQualifier());
    }

    @Test public void testIsSupplemental() {
        assertTrue(MeshRecord.lookup(suppKey1).isSupplemental());

        assertFalse(MeshRecord.lookup(descKey1).isSupplemental());
        assertFalse(MeshRecord.lookup(descKey2).isSupplemental());
        assertFalse(MeshRecord.lookup(descKey3).isSupplemental());
        assertFalse(MeshRecord.lookup(descKey4).isSupplemental());
        assertFalse(MeshRecord.lookup(descKey5).isSupplemental());
        assertFalse(MeshRecord.lookup(qualKey1).isSupplemental());
        assertFalse(MeshRecord.lookup(qualKey2).isSupplemental());
    }

    @Test public void testList() {
        assertEquals(8, records.size());
        assertEquals(suppKey1, records.get(0).getKey());
        assertEquals(descKey1, records.get(1).getKey());
        assertEquals(descKey2, records.get(2).getKey());
        assertEquals(descKey3, records.get(3).getKey());
        assertEquals(descKey4, records.get(4).getKey());
        assertEquals(descKey5, records.get(5).getKey());
        assertEquals(qualKey1, records.get(6).getKey());
        assertEquals(qualKey2, records.get(7).getKey());
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
