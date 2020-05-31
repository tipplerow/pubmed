
package pubmed.junit;

import java.util.List;

import pubmed.mesh.MeshQualifier;
import pubmed.mesh.MeshQualifierName;
import pubmed.mesh.MeshQualifierKey;
import pubmed.mesh.MeshQualifierXmlDocument;
import pubmed.mesh.MeshRecord;
import pubmed.mesh.MeshTreeNumber;
import pubmed.mesh.MeshTreeNumberList;

import org.junit.*;
import static org.junit.Assert.*;

public class MeshQualifierTest {
    private static final String SAMPLE_XML = "data/test/mesh_qual_sample.xml";

    private static final MeshQualifierKey key1 = MeshQualifierKey.instance("Q000037");
    private static final MeshQualifierKey key2 = MeshQualifierKey.instance("Q000819");
    private static final MeshQualifierKey key3 = MeshQualifierKey.instance("Qxxxxxx");

    private static final MeshQualifierName name1 = MeshQualifierName.instance("antagonists & inhibitors");
    private static final MeshQualifierName name2 = MeshQualifierName.instance("agonists");
    private static final MeshQualifierName name3 = MeshQualifierName.instance("zzzzzzzz");

    private static final MeshTreeNumber number11 = MeshTreeNumber.instance("Y03.030");
    private static final MeshTreeNumber number12 = MeshTreeNumber.instance("Y07.040");

    private static final MeshTreeNumber number21 = MeshTreeNumber.instance("Y03.010");
    private static final MeshTreeNumber number22 = MeshTreeNumber.instance("Y07.030");

    private static final List<MeshQualifier> qualifiers = MeshQualifierXmlDocument.parse(SAMPLE_XML);

    @Test public void testComparator() {
        MeshQualifier qual1 = qualifiers.get(0);
        MeshQualifier qual2 = qualifiers.get(1);

        assertTrue(MeshRecord.KEY_COMPARATOR.compare(qual1, qual2)  < 0);
        assertTrue(MeshRecord.KEY_COMPARATOR.compare(qual1, qual1) == 0);
        assertTrue(MeshRecord.KEY_COMPARATOR.compare(qual2, qual1)  > 0);

        assertTrue(MeshRecord.NAME_COMPARATOR.compare(qual1, qual2)  > 0);
        assertTrue(MeshRecord.NAME_COMPARATOR.compare(qual1, qual1) == 0);
        assertTrue(MeshRecord.NAME_COMPARATOR.compare(qual2, qual1)  < 0);
    }

    @Test public void testExists() {
        assertTrue(MeshRecord.exists(key1));
        assertTrue(MeshRecord.exists(key2));
        assertFalse(MeshRecord.exists(key3));
    }

    @Test public void testInstance() {
        assertEquals(name1, MeshQualifier.instance(key1).getName());
        assertEquals(name2, MeshQualifier.instance(key2).getName());

        assertNull(MeshRecord.lookup(key3));
    }

    @Test public void testNote() {
        assertTrue(qualifiers.get(0).getNote().startsWith("Used with chemicals, drugs, and endogenous substances"));
        assertTrue(qualifiers.get(0).getNote().endsWith("counteract their biological effects by any mechanism."));

        assertTrue(qualifiers.get(1).getNote().startsWith("Used with chemicals, drugs, and endogenous substances"));
        assertTrue(qualifiers.get(1).getNote().endsWith("(From Textbook of Pharmacology, 1991, p.16)"));
    }

    @Test public void testParser() {
        assertEquals(key1, qualifiers.get(0).getKey());
        assertEquals(key2, qualifiers.get(1).getKey());

        assertEquals(name1, qualifiers.get(0).getName());
        assertEquals(name2, qualifiers.get(1).getName());
    }

    @Test public void testRegistry() {
        //
        // The qualifiers are stored in a private registry, so test
        // for physical equivalence (equality of references)...
        //
        assertTrue(qualifiers.get(0) == MeshQualifier.antagonists());
        assertTrue(qualifiers.get(1) == MeshQualifier.agonists());
    }

    @Test public void testTermList() {
        assertEquals(List.of("antagonists & inhibitors",
                             "antagonists and inhibitors",
                             "inhibitors", "antagonists"),
                     qualifiers.get(0).termStrings());

        assertEquals(List.of("agonists"), qualifiers.get(1).termStrings());
    }

    @Test public void testTreeNumberList() {
        assertEquals(MeshTreeNumberList.of(number11, number12), qualifiers.get(0).getNumberList());
        assertEquals(MeshTreeNumberList.of(number21, number22), qualifiers.get(1).getNumberList());
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("pubmed.junit.MeshQualifierTest");
    }
}
