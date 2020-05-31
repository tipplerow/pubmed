
package pubmed.junit;

import pubmed.article.PMID;

import org.junit.*;
import static org.junit.Assert.*;

public class PMIDTest {
    private final PMID id1A = PMID.instance(1000);
    private final PMID id1B = PMID.instance("1000");
    private final PMID id2  = PMID.instance(2000);

    @Test public void testCompareTo() {
        assertTrue(id1A.compareTo(id1A) == 0);
        assertTrue(id1A.compareTo(id1B) == 0);
        assertTrue(id1A.compareTo(id2) < 0);

        assertTrue(id1B.compareTo(id1A) == 0);
        assertTrue(id1B.compareTo(id1B) == 0);
        assertTrue(id1B.compareTo(id2) < 0);

        assertTrue(id2.compareTo(id1A) > 0);
        assertTrue(id2.compareTo(id1B) > 0);
        assertTrue(id2.compareTo(id2) == 0);
    }

    @Test public void testEquals() {
        assertTrue(id1A.equals(id1A));
        assertTrue(id1A.equals(id1B));
        assertFalse(id1A.equals(id2));

        assertTrue(id1B.equals(id1A));
        assertTrue(id1B.equals(id1B));
        assertFalse(id1B.equals(id2));

        assertFalse(id2.equals(id1A));
        assertFalse(id2.equals(id1B));
        assertTrue(id2.equals(id2));
    }

    @Test public void testHashCode() {
        assertEquals(1000, id1A.hashCode());
        assertEquals(1000, id1B.hashCode());
        assertEquals(2000, id2.hashCode());
    }

    @Test public void testIntValue() {
        assertEquals(1000, id1A.intValue());
        assertEquals(1000, id1B.intValue());
        assertEquals(2000, id2.intValue());
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("pubmed.junit.PMIDTest");
    }
}
