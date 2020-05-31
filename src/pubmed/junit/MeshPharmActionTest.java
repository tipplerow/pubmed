
package pubmed.junit;

import java.util.List;
import java.util.Set;

import com.google.common.collect.Multiset;

import pubmed.mesh.MeshDescriptorKey;
import pubmed.mesh.MeshPharmAction;
import pubmed.mesh.MeshPharmActionXmlDocument;
import pubmed.mesh.MeshRecordKey;

import org.junit.*;
import static org.junit.Assert.*;

public class MeshPharmActionTest {
    private static final String SAMPLE_XML = "data/test/mesh_pa_sample.xml";

    private static final MeshDescriptorKey action1 = MeshDescriptorKey.instance("D058888");
    private static final MeshDescriptorKey action2 = MeshDescriptorKey.instance("D058891");
    private static final MeshDescriptorKey action3 = MeshDescriptorKey.instance("Dxxxxxx");

    private static final MeshRecordKey actor1 = MeshRecordKey.instance("D003022");
    private static final MeshRecordKey actor2 = MeshRecordKey.instance("D004464");
    private static final MeshRecordKey actor3 = MeshRecordKey.instance("D015725");
    private static final MeshRecordKey actor4 = MeshRecordKey.instance("C486878");
    private static final MeshRecordKey actor5 = MeshRecordKey.instance("C077586");
    private static final MeshRecordKey actor6 = MeshRecordKey.instance("C088478");
    private static final MeshRecordKey actor7 = MeshRecordKey.instance("C084634");

    private static final List<MeshPharmAction> actions = MeshPharmActionXmlDocument.parse(SAMPLE_XML);

    @Test public void testExists() {
        assertTrue(MeshPharmAction.exists(action1));
        assertTrue(MeshPharmAction.exists(action2));
        assertFalse(MeshPharmAction.exists(action3));
    }

    @Test public void testFindCoActors() {
        Multiset<MeshRecordKey> coActors1 = MeshPharmAction.findCoActors(actor1);
        Multiset<MeshRecordKey> coActors2 = MeshPharmAction.findCoActors(actor2);
        Multiset<MeshRecordKey> coActors3 = MeshPharmAction.findCoActors(actor3);
        Multiset<MeshRecordKey> coActors5 = MeshPharmAction.findCoActors(actor5);

        assertEquals(0, coActors1.count(actor1));
        assertEquals(2, coActors1.count(actor2));
        assertEquals(1, coActors1.count(actor3));
        assertEquals(1, coActors1.count(actor4));
        assertEquals(1, coActors1.count(actor5));
        assertEquals(1, coActors1.count(actor6));
        assertEquals(1, coActors1.count(actor7));

        assertEquals(2, coActors2.count(actor1));
        assertEquals(0, coActors2.count(actor2));
        assertEquals(1, coActors2.count(actor3));
        assertEquals(1, coActors2.count(actor4));
        assertEquals(1, coActors2.count(actor5));
        assertEquals(1, coActors2.count(actor6));
        assertEquals(1, coActors2.count(actor7));

        assertEquals(1, coActors3.count(actor1));
        assertEquals(1, coActors3.count(actor2));
        assertEquals(0, coActors3.count(actor3));
        assertEquals(1, coActors3.count(actor4));
        assertEquals(0, coActors3.count(actor5));
        assertEquals(0, coActors3.count(actor6));
        assertEquals(0, coActors3.count(actor7));

        assertEquals(1, coActors5.count(actor1));
        assertEquals(1, coActors5.count(actor2));
        assertEquals(0, coActors5.count(actor3));
        assertEquals(0, coActors5.count(actor4));
        assertEquals(0, coActors5.count(actor5));
        assertEquals(1, coActors5.count(actor6));
        assertEquals(1, coActors5.count(actor7));
    }

    @Test public void testParser() {
        assertEquals(action1, actions.get(0).getKey());
        assertEquals(action2, actions.get(1).getKey());

        assertEquals(Set.of(actor1, actor2, actor3, actor4), actions.get(0).viewActors());
        assertEquals(Set.of(actor1, actor2, actor5, actor6, actor7), actions.get(1).viewActors());
    }

    @Test public void testRegistry() {
        //
        // The actions are stored in a private registry, so test
        // for physical equivalence (equality of references)...
        //
        assertTrue(actions.get(0) == MeshPharmAction.instance(action1));
        assertTrue(actions.get(1) == MeshPharmAction.instance(action2));
    }

    @Test public void testViewActions() {
        assertActions(actor1, action1, action2);
        assertActions(actor2, action1, action2);

        assertActions(actor3, action1);
        assertActions(actor4, action1);

        assertActions(actor5, action2);
        assertActions(actor6, action2);
        assertActions(actor7, action2);
    }

    private void assertActions(MeshRecordKey actor, MeshDescriptorKey... expected) {
        assertEquals(Set.of(expected), MeshPharmAction.keySet(MeshPharmAction.viewActions(actor)));
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("pubmed.junit.MeshPharmActionTest");
    }
}
