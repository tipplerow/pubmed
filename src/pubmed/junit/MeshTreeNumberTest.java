
package pubmed.junit;

import pubmed.mesh.MeshTreeNumber;

import org.junit.*;
import static org.junit.Assert.*;

public class MeshTreeNumberTest {
    private static final MeshTreeNumber root1 = MeshTreeNumber.instance("C04");
    private static final MeshTreeNumber root2 = MeshTreeNumber.instance("G03");

    private static final MeshTreeNumber sub11 = MeshTreeNumber.instance("C04.182");
    private static final MeshTreeNumber sub12 = MeshTreeNumber.instance("C04.588");
    private static final MeshTreeNumber sub21 = MeshTreeNumber.instance("G03.295");
    private static final MeshTreeNumber sub22 = MeshTreeNumber.instance("G03.500");

    private static final MeshTreeNumber sub111 = MeshTreeNumber.instance("C04.182.089");
    private static final MeshTreeNumber sub112 = MeshTreeNumber.instance("C04.182.640");
    private static final MeshTreeNumber sub121 = MeshTreeNumber.instance("C04.588.149");
    private static final MeshTreeNumber sub122 = MeshTreeNumber.instance("C04.588.805");

    @Test public void testGetParent() {
        assertNull(root1.getParent());
        assertNull(root2.getParent());

        assertEquals(root1, sub11.getParent());
        assertEquals(root1, sub12.getParent());
        assertEquals(root2, sub21.getParent());
        assertEquals(root2, sub22.getParent());

        assertEquals(sub11, sub111.getParent());
        assertEquals(sub11, sub112.getParent());
        assertEquals(sub12, sub121.getParent());
        assertEquals(sub12, sub122.getParent());
    }

    @Test public void testIsBroader() {
        assertFalse(root1.isBroader(root1));
        assertFalse(root1.isBroader(root2));

        assertTrue(root1.isBroader(sub11));
        assertTrue(root1.isBroader(sub12));
        assertFalse(root1.isBroader(sub21));
        assertFalse(root1.isBroader(sub22));

        assertTrue(root1.isBroader(sub111));
        assertTrue(root1.isBroader(sub112));
        assertTrue(root1.isBroader(sub122));
        assertTrue(root1.isBroader(sub122));

        assertFalse(sub11.isBroader(root1));
        assertFalse(sub11.isBroader(root2));

        assertFalse(sub11.isBroader(sub11));
        assertFalse(sub11.isBroader(sub12));
        assertFalse(sub11.isBroader(sub21));
        assertFalse(sub11.isBroader(sub22));

        assertTrue(sub11.isBroader(sub111));
        assertTrue(sub11.isBroader(sub112));
        assertFalse(sub11.isBroader(sub122));
        assertFalse(sub11.isBroader(sub122));

        assertFalse(sub111.isBroader(root1));
        assertFalse(sub111.isBroader(root2));

        assertFalse(sub111.isBroader(sub11));
        assertFalse(sub111.isBroader(sub12));
        assertFalse(sub111.isBroader(sub21));
        assertFalse(sub111.isBroader(sub22));

        assertFalse(sub111.isBroader(sub111));
        assertFalse(sub111.isBroader(sub112));
        assertFalse(sub111.isBroader(sub122));
        assertFalse(sub111.isBroader(sub122));
    }

    @Test public void testIsNarrower() {
        assertFalse(root1.isNarrower(root1));
        assertFalse(root1.isNarrower(root2));

        assertFalse(root1.isNarrower(sub11));
        assertFalse(root1.isNarrower(sub12));
        assertFalse(root1.isNarrower(sub21));
        assertFalse(root1.isNarrower(sub22));

        assertFalse(root1.isNarrower(sub111));
        assertFalse(root1.isNarrower(sub112));
        assertFalse(root1.isNarrower(sub122));
        assertFalse(root1.isNarrower(sub122));

        assertTrue(sub11.isNarrower(root1));
        assertFalse(sub11.isNarrower(root2));

        assertFalse(sub11.isNarrower(sub11));
        assertFalse(sub11.isNarrower(sub12));
        assertFalse(sub11.isNarrower(sub21));
        assertFalse(sub11.isNarrower(sub22));

        assertFalse(sub11.isNarrower(sub111));
        assertFalse(sub11.isNarrower(sub112));
        assertFalse(sub11.isNarrower(sub122));
        assertFalse(sub11.isNarrower(sub122));

        assertTrue(sub111.isNarrower(root1));
        assertFalse(sub111.isNarrower(root2));

        assertTrue(sub111.isNarrower(sub11));
        assertFalse(sub111.isNarrower(sub12));
        assertFalse(sub111.isNarrower(sub21));
        assertFalse(sub111.isNarrower(sub22));

        assertFalse(sub111.isNarrower(sub111));
        assertFalse(sub111.isNarrower(sub112));
        assertFalse(sub111.isNarrower(sub122));
        assertFalse(sub111.isNarrower(sub122));
    }

    @Test public void testIsRootNumber() {
        assertTrue(root1.isRootNumber());
        assertTrue(root2.isRootNumber());

        assertFalse(sub11.isRootNumber());
        assertFalse(sub12.isRootNumber());
        assertFalse(sub21.isRootNumber());
        assertFalse(sub22.isRootNumber());

        assertFalse(sub111.isRootNumber());
        assertFalse(sub112.isRootNumber());
        assertFalse(sub121.isRootNumber());
        assertFalse(sub122.isRootNumber());
    }

    @Test public void testIsSibling() {
        assertFalse(root1.isSibling(root1));
        assertFalse(root1.isSibling(root2));

        assertTrue(sub11.isSibling(sub12));
        assertTrue(sub12.isSibling(sub11));

        assertFalse(sub11.isSibling(sub11));
        assertFalse(sub11.isSibling(sub21));
        assertFalse(sub11.isSibling(sub22));
        assertFalse(sub11.isSibling(sub111));
        assertFalse(sub11.isSibling(sub112));
        assertFalse(sub11.isSibling(sub121));
        assertFalse(sub11.isSibling(sub122));

        assertTrue(sub111.isSibling(sub112));
        assertTrue(sub112.isSibling(sub111));

        assertFalse(sub111.isSibling(sub11));
        assertFalse(sub111.isSibling(sub22));
        assertFalse(sub111.isSibling(sub21));
        assertFalse(sub111.isSibling(sub22));
        assertFalse(sub111.isSibling(sub111));
        assertFalse(sub111.isSibling(sub121));
        assertFalse(sub111.isSibling(sub122));
    }

    @Test(expected = RuntimeException.class)
    public void testInstanceInvalid1() {
        MeshTreeNumber.instance("a01");
    }

    @Test(expected = RuntimeException.class)
    public void testInstanceInvalid2() {
        MeshTreeNumber.instance("A01.bcx");
    }

    @Test(expected = RuntimeException.class)
    public void testInstanceInvalid3() {
        MeshTreeNumber.instance("A01;123");
    }

    @Test public void testOnSubTree() {
        assertTrue(root1.onSubTree(root1));
        assertFalse(root1.onSubTree(root2));

        assertFalse(root1.onSubTree(sub11));
        assertFalse(root1.onSubTree(sub12));
        assertFalse(root1.onSubTree(sub21));
        assertFalse(root1.onSubTree(sub22));

        assertFalse(root1.onSubTree(sub111));
        assertFalse(root1.onSubTree(sub112));
        assertFalse(root1.onSubTree(sub122));
        assertFalse(root1.onSubTree(sub122));

        assertTrue(sub11.onSubTree(root1));
        assertFalse(sub11.onSubTree(root2));

        assertTrue(sub11.onSubTree(sub11));
        assertFalse(sub11.onSubTree(sub12));
        assertFalse(sub11.onSubTree(sub21));
        assertFalse(sub11.onSubTree(sub22));

        assertFalse(sub11.onSubTree(sub111));
        assertFalse(sub11.onSubTree(sub112));
        assertFalse(sub11.onSubTree(sub122));
        assertFalse(sub11.onSubTree(sub122));

        assertTrue(sub111.onSubTree(root1));
        assertFalse(sub111.onSubTree(root2));

        assertTrue(sub111.onSubTree(sub11));
        assertFalse(sub111.onSubTree(sub12));
        assertFalse(sub111.onSubTree(sub21));
        assertFalse(sub111.onSubTree(sub22));

        assertTrue(sub111.onSubTree(sub111));
        assertFalse(sub111.onSubTree(sub112));
        assertFalse(sub111.onSubTree(sub122));
        assertFalse(sub111.onSubTree(sub122));
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("pubmed.junit.MeshTreeNumberTest");
    }
}
