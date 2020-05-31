
package pubmed.junit;

import java.util.Collections;
import java.util.List;

import pubmed.mesh.MeshDescriptorXmlDocument;
import pubmed.mesh.MeshQualifierXmlDocument;
import pubmed.mesh.MeshSupplementalXmlDocument;
import pubmed.mesh.MeshTerm;

import org.junit.*;
import static org.junit.Assert.*;

public class MeshTermTest {
    static {
        MeshDescriptorXmlDocument.parse("data/test/mesh_desc_sample.xml");
        MeshQualifierXmlDocument.parse("data/test/mesh_qual_sample.xml");
        MeshSupplementalXmlDocument.parse("data/test/mesh_supp_sample.xml");
    }

    @Test public void testList() {
        List<MeshTerm> terms = MeshTerm.list();
        Collections.sort(terms);

        assertEquals(26, terms.size());
        assertEquals("A 23187", terms.get(0).getTerm());
        assertEquals("red yeast rice", terms.get(25).getTerm());
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("pubmed.junit.MeshTermTest");
    }
}
