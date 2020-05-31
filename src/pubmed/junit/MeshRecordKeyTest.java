
package pubmed.junit;

import pubmed.mesh.MeshDescriptorKey;
import pubmed.mesh.MeshQualifierKey;
import pubmed.mesh.MeshRecordKey;
import pubmed.mesh.MeshRecordType;
import pubmed.mesh.MeshSupplementalKey;

import org.junit.*;
import static org.junit.Assert.*;

public class MeshRecordKeyTest {
    @Test public void testInstance() {
        assertEquals(MeshRecordType.DESCRIPTOR,   MeshRecordKey.instance("D123456").getType());
        assertEquals(MeshRecordType.QUALIFIER,    MeshRecordKey.instance("Q123456").getType());
        assertEquals(MeshRecordType.SUPPLEMENTAL, MeshRecordKey.instance("C123456").getType());

        assertEquals(MeshRecordType.DESCRIPTOR,   MeshDescriptorKey.instance("D000001").getType());
        assertEquals(MeshRecordType.QUALIFIER,    MeshQualifierKey.instance("Q000002").getType());
        assertEquals(MeshRecordType.SUPPLEMENTAL, MeshSupplementalKey.instance("C000003").getType());

        assertTrue(MeshRecordKey.instance("D123456") == MeshDescriptorKey.instance("D123456"));
        assertTrue(MeshRecordKey.instance("Q123456") == MeshQualifierKey.instance("Q123456"));
        assertTrue(MeshRecordKey.instance("C123456") == MeshSupplementalKey.instance("C123456"));
    }

    @Test public void testIsA() {
        MeshRecordKey desc = MeshRecordKey.instance("D123456");
        MeshRecordKey qual = MeshRecordKey.instance("Q123456");
        MeshRecordKey supp = MeshRecordKey.instance("C123456");

        assertTrue(desc.isDescriptorKey());
        assertFalse(desc.isQualifierKey());
        assertFalse(desc.isSupplementalKey());

        assertFalse(qual.isDescriptorKey());
        assertTrue(qual.isQualifierKey());
        assertFalse(qual.isSupplementalKey());

        assertFalse(supp.isDescriptorKey());
        assertFalse(supp.isQualifierKey());
        assertTrue(supp.isSupplementalKey());
    }

    @Test(expected = RuntimeException.class)
    public void testDescriptorKeyInstanceInvalid() {
        MeshDescriptorKey.instance("C567890");
    }

    @Test(expected = RuntimeException.class)
    public void testQualifierKeyInstanceInvalid() {
        MeshQualifierKey.instance("D567890");
    }

    @Test(expected = RuntimeException.class)
    public void testRecordKeyInstanceInvalid() {
        MeshRecordKey.instance("abcdefg");
    }

    @Test(expected = RuntimeException.class)
    public void testSupplementalKeyInstanceInvalid() {
        MeshSupplementalKey.instance("Q567890");
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("pubmed.junit.MeshRecordKeyTest");
    }
}
