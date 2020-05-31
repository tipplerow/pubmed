
package pubmed.junit;

import pubmed.mesh.MeshRecordType;

import org.junit.*;
import static org.junit.Assert.*;

public class MeshRecordTypeTest {
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
