
package pubmed.junit;

import java.util.List;

import pubmed.mesh.MeshDescriptor;
import pubmed.mesh.MeshDescriptorName;
import pubmed.mesh.MeshDescriptorKey;
import pubmed.mesh.MeshDescriptorXmlDocument;
import pubmed.mesh.MeshRecord;
import pubmed.mesh.MeshTerm;
import pubmed.mesh.MeshTermList;
import pubmed.mesh.MeshTreeNumber;
import pubmed.mesh.MeshTreeNumberList;
import pubmed.nlp.LemmaList;

import org.junit.*;
import static org.junit.Assert.*;

public class MeshDescriptorTest {
    private static final String SAMPLE_XML = "data/test/mesh_desc_sample.xml";

    private static final MeshDescriptorKey key1 = MeshDescriptorKey.instance("D000001");
    private static final MeshDescriptorKey key2 = MeshDescriptorKey.instance("D000002");
    private static final MeshDescriptorKey key3 = MeshDescriptorKey.instance("D000003");
    private static final MeshDescriptorKey key4 = MeshDescriptorKey.instance("Dxxxxxx");

    private static final MeshDescriptorName name1 = MeshDescriptorName.instance("Calcimycin");
    private static final MeshDescriptorName name2 = MeshDescriptorName.instance("Temefos");
    private static final MeshDescriptorName name3 = MeshDescriptorName.instance("Abattoirs");
    private static final MeshDescriptorName name4 = MeshDescriptorName.instance("zzzzzzzz");

    private static final MeshTreeNumber number1 = MeshTreeNumber.instance("D03.633.100.221.173");

    private static final MeshTreeNumber number21 = MeshTreeNumber.instance("D02.705.400.625.800");
    private static final MeshTreeNumber number22 = MeshTreeNumber.instance("D02.705.539.345.800");
    private static final MeshTreeNumber number23 = MeshTreeNumber.instance("D02.886.300.692.800");

    private static final List<MeshDescriptor> descriptors = MeshDescriptorXmlDocument.parse(SAMPLE_XML);

    @Test public void testComparator() {
        MeshDescriptor desc1 = descriptors.get(0);
        MeshDescriptor desc2 = descriptors.get(1);
        MeshDescriptor desc3 = descriptors.get(2);

        assertTrue(MeshRecord.KEY_COMPARATOR.compare(desc1, desc1) == 0);
        assertTrue(MeshRecord.KEY_COMPARATOR.compare(desc1, desc2)  < 0);
        assertTrue(MeshRecord.KEY_COMPARATOR.compare(desc1, desc3)  < 0);

        assertTrue(MeshRecord.KEY_COMPARATOR.compare(desc2, desc1)  > 0);
        assertTrue(MeshRecord.KEY_COMPARATOR.compare(desc2, desc2) == 0);
        assertTrue(MeshRecord.KEY_COMPARATOR.compare(desc2, desc3)  < 0);

        assertTrue(MeshRecord.KEY_COMPARATOR.compare(desc3, desc1)  > 0);
        assertTrue(MeshRecord.KEY_COMPARATOR.compare(desc3, desc2)  > 0);
        assertTrue(MeshRecord.KEY_COMPARATOR.compare(desc3, desc3) == 0);

        assertTrue(MeshRecord.NAME_COMPARATOR.compare(desc1, desc1) == 0);
        assertTrue(MeshRecord.NAME_COMPARATOR.compare(desc1, desc2)  < 0);
        assertTrue(MeshRecord.NAME_COMPARATOR.compare(desc1, desc3)  > 0);

        assertTrue(MeshRecord.NAME_COMPARATOR.compare(desc2, desc1)  > 0);
        assertTrue(MeshRecord.NAME_COMPARATOR.compare(desc2, desc2) == 0);
        assertTrue(MeshRecord.NAME_COMPARATOR.compare(desc2, desc3)  > 0);

        assertTrue(MeshRecord.NAME_COMPARATOR.compare(desc3, desc1)  < 0);
        assertTrue(MeshRecord.NAME_COMPARATOR.compare(desc3, desc2)  < 0);
        assertTrue(MeshRecord.NAME_COMPARATOR.compare(desc3, desc3) == 0);
    }

    @Test public void testExists() {
        assertTrue(MeshRecord.exists(key1));
        assertTrue(MeshRecord.exists(key2));
        assertTrue(MeshRecord.exists(key3));
        assertFalse(MeshRecord.exists(key4));
    }

    @Test public void testInstance() {
        assertEquals(name1, MeshDescriptor.instance(key1).getName());
        assertEquals(name2, MeshDescriptor.instance(key2).getName());
        assertEquals(name3, MeshDescriptor.instance(key3).getName());
        assertNull(MeshRecord.lookup(key4));
    }

    @Test public void testIsBroader() {
        assertFalse(descriptors.get(1).isBroader(MeshTreeNumber.instance("D02.705.539.345")));
        assertFalse(descriptors.get(1).isBroader(MeshTreeNumber.instance("D02.705.539.345.700")));
        assertFalse(descriptors.get(1).isBroader(MeshTreeNumber.instance("D02.705.539.345.800")));
        assertTrue( descriptors.get(1).isBroader(MeshTreeNumber.instance("D02.705.539.345.800.123")));
    }

    @Test public void testIsNarrower() {
        assertTrue( descriptors.get(1).isNarrower(MeshTreeNumber.instance("D02.705.539.345")));
        assertFalse(descriptors.get(1).isNarrower(MeshTreeNumber.instance("D02.705.539.345.700")));
        assertFalse(descriptors.get(1).isNarrower(MeshTreeNumber.instance("D02.705.539.345.800")));
        assertFalse(descriptors.get(1).isNarrower(MeshTreeNumber.instance("D02.705.539.345.800.123")));
    }

    @Test public void testNote() {
        assertTrue(descriptors.get(0).getNote().startsWith("An ionophorous, polyether antibiotic"));
        assertEquals("An organothiophosphate insecticide.", descriptors.get(1).getNote());
    }

    @Test public void testParser() {
        assertEquals(key1, descriptors.get(0).getKey());
        assertEquals(key2, descriptors.get(1).getKey());
        assertEquals(key3, descriptors.get(2).getKey());

        assertEquals(name1, descriptors.get(0).getName());
        assertEquals(name2, descriptors.get(1).getName());
        assertEquals(name3, descriptors.get(2).getName());
    }

    @Test public void testRegistry() {
        //
        // The descriptors are stored in a private registry, so test
        // for physical equivalence (equality of references)...
        //
        assertTrue(descriptors.get(0) == MeshDescriptor.instance(key1));
        assertTrue(descriptors.get(1) == MeshDescriptor.instance(key2));
        assertTrue(descriptors.get(2) == MeshDescriptor.instance(key3));
    }

    @Test public void testTermLemmas() {
        assertEquals(List.of(LemmaList.create("hematoma")), descriptors.get(4).termLemmas());

        assertEquals(LemmaList.create("nivolumab"), descriptors.get(3).termLemmas().get(0));
        assertEquals(LemmaList.create("opdivo"), descriptors.get(3).termLemmas().get(1));
    }

    @Test public void testTermList() {
        assertEquals(List.of("Calcimycin",
                             "A-23187",
                             "A 23187",
                             "A23187",
                             "Antibiotic A23187",
                             "A23187, Antibiotic"),
                     descriptors.get(0).termStrings());

        assertEquals(List.of("Temefos", "Temephos", "Abate", "Difos"), descriptors.get(1).termStrings());
    }

    @Test public void testTermObjects() {
        List<MeshTerm> objects = descriptors.get(1).termObjects();

        assertEquals(4, objects.size());
        assertEquals(MeshTerm.create("Temefos",  true,  key2), objects.get(0));
        assertEquals(MeshTerm.create("Temephos", false, key2), objects.get(1));
        assertEquals(MeshTerm.create("Abate",    false, key2), objects.get(2));
        assertEquals(MeshTerm.create("Difos",    false, key2), objects.get(3));
    }

    @Test public void testTreeNumberList() {
        assertEquals(MeshTreeNumberList.of(number1), descriptors.get(0).getNumberList());
        assertEquals(MeshTreeNumberList.of(number21, number22, number23), descriptors.get(1).getNumberList());
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("pubmed.junit.MeshDescriptorTest");
    }
}
