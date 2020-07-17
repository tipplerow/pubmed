
package pubmed.junit;

import pubmed.mesh.MeshRecordType;

import org.junit.*;
import static org.junit.Assert.*;

public class MeshRecordTypeTest {
    @Test public void testIsRecordKey() {
        assertTrue(MeshRecordType.isRecordKey("D123456"));
        assertTrue(MeshRecordType.isRecordKey("Q123456"));
        assertTrue(MeshRecordType.isRecordKey("C123456"));
        assertFalse(MeshRecordType.isRecordKey("X123456"));
    }

    @Test public void testResolveKeyType() {
        assertEquals(MeshRecordType.DESCRIPTOR,   MeshRecordType.resolveKeyType("D123456"));
        assertEquals(MeshRecordType.QUALIFIER,    MeshRecordType.resolveKeyType("Q123456"));
        assertEquals(MeshRecordType.SUPPLEMENTAL, MeshRecordType.resolveKeyType("C123456"));
    }

    @Test(expected = RuntimeException.class)
    public void testResolveKeyTypeInvalid() {
        MeshRecordType.resolveKeyType("abcdefg");
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("pubmed.junit.MeshRecordTypeTest");
    }
}
