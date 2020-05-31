
package pubmed.junit;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import pubmed.mesh.MeshDescriptorKey;
import pubmed.mesh.MeshPharmCoActor;
import pubmed.mesh.MeshPharmActionXmlDocument;
import pubmed.mesh.MeshRecordKey;

import org.junit.*;
import static org.junit.Assert.*;

public class MeshPharmCoActorTest {
    private static final String SAMPLE_XML = "data/test/mesh_pa_sample.xml";

    private static final MeshDescriptorKey action1 = MeshDescriptorKey.instance("D058888");
    private static final MeshDescriptorKey action2 = MeshDescriptorKey.instance("D058891");

    private static final MeshRecordKey actor1 = MeshRecordKey.instance("D003022");
    private static final MeshRecordKey actor2 = MeshRecordKey.instance("D004464");
    private static final MeshRecordKey actor3 = MeshRecordKey.instance("D015725");
    private static final MeshRecordKey actor4 = MeshRecordKey.instance("C486878");
    private static final MeshRecordKey actor5 = MeshRecordKey.instance("C077586");
    private static final MeshRecordKey actor6 = MeshRecordKey.instance("C088478");
    private static final MeshRecordKey actor7 = MeshRecordKey.instance("C084634");

    static {
        MeshPharmActionXmlDocument.parse(SAMPLE_XML);
    }

    @Test public void testFind() {
        List<MeshPharmCoActor> coActorList = MeshPharmCoActor.find(actor1);

        assertEquals(6, coActorList.size());
        coActorList.sort(MeshPharmCoActor.DESCENDING_COUNT_COMPARATOR);

        assertCoActor(coActorList.get(0), actor1, actor2, action1, action2);
        assertCoActor(coActorList.get(1), actor1, actor5, action2);
        assertCoActor(coActorList.get(2), actor1, actor7, action2);
        assertCoActor(coActorList.get(3), actor1, actor6, action2);
        assertCoActor(coActorList.get(4), actor1, actor4, action1);
        assertCoActor(coActorList.get(5), actor1, actor3, action1);

        coActorList = MeshPharmCoActor.find(actor2);

        assertEquals(6, coActorList.size());
        coActorList.sort(MeshPharmCoActor.DESCENDING_COUNT_COMPARATOR);

        assertCoActor(coActorList.get(0), actor2, actor1, action1, action2);
        assertCoActor(coActorList.get(1), actor2, actor5, action2);
        assertCoActor(coActorList.get(2), actor2, actor7, action2);
        assertCoActor(coActorList.get(3), actor2, actor6, action2);
        assertCoActor(coActorList.get(4), actor2, actor4, action1);
        assertCoActor(coActorList.get(5), actor2, actor3, action1);

        coActorList = MeshPharmCoActor.find(actor3);

        assertEquals(3, coActorList.size());
        coActorList.sort(MeshPharmCoActor.DESCENDING_COUNT_COMPARATOR);

        assertCoActor(coActorList.get(0), actor3, actor4, action1);
        assertCoActor(coActorList.get(1), actor3, actor1, action1);
        assertCoActor(coActorList.get(2), actor3, actor2, action1);

        coActorList = MeshPharmCoActor.find(actor5);

        assertEquals(4, coActorList.size());
        coActorList.sort(MeshPharmCoActor.DESCENDING_COUNT_COMPARATOR);

        assertCoActor(coActorList.get(0), actor5, actor7, action2);
        assertCoActor(coActorList.get(1), actor5, actor6, action2);
        assertCoActor(coActorList.get(2), actor5, actor1, action2);
        assertCoActor(coActorList.get(3), actor5, actor2, action2);
    }

    private void assertCoActor(MeshPharmCoActor actual,
                               MeshRecordKey actor,
                               MeshRecordKey coActor,
                               MeshDescriptorKey... actions) {
        assertEquals(actor, actual.getActor());
        assertEquals(coActor, actual.getCoActor());
        assertEquals(Set.of(actions), actual.viewActions());
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("pubmed.junit.MeshPharmCoActorTest");
    }
}
