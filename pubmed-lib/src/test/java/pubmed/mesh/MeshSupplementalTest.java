
package pubmed.mesh;

import java.util.List;

import org.junit.*;
import static org.junit.Assert.*;

public class MeshSupplementalTest {
    private static final String SAMPLE_XML = "data/test/mesh_supp_sample.xml";

    private static final MeshSupplementalKey key1 = MeshSupplementalKey.instance("C117343");
    private static final MeshSupplementalKey key2 = MeshSupplementalKey.instance("Cxxxxxx");

    private static final MeshSupplementalName name1 = MeshSupplementalName.instance("red yeast rice");
    private static final MeshSupplementalName name2 = MeshSupplementalName.instance("zzzzzzzz");

    private static final List<MeshSupplemental> supplementals = MeshSupplementalXmlDocument.parse(SAMPLE_XML);

    @Test public void testExists() {
        assertTrue(MeshRecord.exists(key1));
        assertFalse(MeshRecord.exists(key2));
    }

    @Test public void testInstance() {
        assertEquals(name1, MeshSupplemental.instance(key1).getName());
        assertNull(MeshRecord.lookup(key2));
    }

    @Test public void testNote() {
        assertTrue(supplementals.get(0).getNote().startsWith("An extract prepared from red yeast (Monascus species)"));
        assertTrue(supplementals.get(0).getNote().endsWith("used in place of statins such as LOVASTATIN."));
    }

    @Test public void testParser() {
        assertEquals(key1, supplementals.get(0).getKey());
        assertEquals(name1, supplementals.get(0).getName());
    }

    @Test public void testRegistry() {
        //
        // The supplementals are stored in a private registry, so test
        // for physical equivalence (equality of references)...
        //
        assertTrue(supplementals.get(0) == MeshSupplemental.instance(key1));
    }

    @Test public void testTermList() {
        assertEquals(List.of("red yeast rice",
                             "Chinese red-yeast-rice dietary supplement",
                             "Cholestin"),
                     supplementals.get(0).termStrings());
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("pubmed.mesh.MeshSupplementalTest");
    }
}
