
package pubmed.junit;

import java.util.List;
import java.util.Set;

import pubmed.article.PMID;
import pubmed.bulk.BulkFile;
import pubmed.bulk.MeshTreeRelevanceFile;
import pubmed.flat.RelevanceScoreRecord;
import pubmed.flat.RelevanceScoreTable;
import pubmed.subject.CancerSubject;
import pubmed.subject.MeshSubject;
import pubmed.subject.Subject;

import org.junit.*;
import static org.junit.Assert.*;

public class MeshTreeRelevanceFileTest {
    private static final BulkFile bulkFile = BulkFile.create("data/test/pubmed_sample.xml");
    private static final MeshTreeRelevanceFile relevanceFile = MeshTreeRelevanceFile.from(bulkFile);

    private static final Subject cancer = CancerSubject.INSTANCE;

    private static final Subject tocilizumab = MeshSubject.create("C502936");

    private static final Subject aldehydeOxidoreductases     = MeshSubject.create("D000445"); // The exact heading
    private static final Subject enzymes                     = MeshSubject.create("D004798"); // Broader than the heading
    private static final Subject glycolaldehydeDehydrogenase = MeshSubject.create("D050618"); // Narrower than the heading
    private static final Subject ketoneOxidoreductases       = MeshSubject.create("D007658"); // Same branch as heading

    private static final Subject prostaticNeoplasms   = MeshSubject.create("D011471"); // The exact heading
    private static final Subject urogenitalNeoplasms  = MeshSubject.create("D014565"); // Broader than the heading
    private static final Subject prostaticNeoplasmsCR = MeshSubject.create("D064129"); // Narrower than the heading
    private static final Subject testicularNeoplasms  = MeshSubject.create("D013736"); // Same branch as heading

    @Test public void testFile() {
        relevanceFile.delete();

        List<Subject> subjects =
            List.of(cancer,
                    tocilizumab,
                    aldehydeOxidoreductases,
                    enzymes,
                    glycolaldehydeDehydrogenase,
                    ketoneOxidoreductases,
                    urogenitalNeoplasms,
                    prostaticNeoplasms,
                    prostaticNeoplasmsCR,
                    testicularNeoplasms);

        assertTrue(relevanceFile.loadTOC().isEmpty());
        assertEquals(subjects, relevanceFile.findUnprocessed(subjects));

        relevanceFile.process(List.of(cancer));
        relevanceFile.process(List.of(cancer));

        assertEquals(Set.of("CANCER"), relevanceFile.loadTOC());

        relevanceFile.process(subjects);

        RelevanceScoreTable table = relevanceFile.load();

        assertFalse(table.containsPrimary(PMID.instance(31383287)));
        assertFalse(table.containsPrimary(PMID.instance(31383287)));
        assertFalse(table.containsForeign("C502936"));

        PMID pmid1 = PMID.instance(1);
        PMID pmid24451147 = PMID.instance(24451147);
        PMID pmid31383582 = PMID.instance(31383582);

        assertEquals(-1, table.getScore(pmid1, cancer));
        assertEquals( 1, table.getScore(pmid24451147, cancer));
        assertEquals(-1, table.getScore(pmid31383582, cancer));

        assertEquals( 1, table.getScore(pmid1, enzymes));
        assertEquals( 1, table.getScore(pmid1, aldehydeOxidoreductases));
        assertEquals(-1, table.getScore(pmid1, ketoneOxidoreductases));
        assertEquals(-1, table.getScore(pmid1, glycolaldehydeDehydrogenase));

        assertEquals( 1, table.getScore(pmid24451147, urogenitalNeoplasms));
        assertEquals( 1, table.getScore(pmid24451147, prostaticNeoplasms));
        assertEquals(-1, table.getScore(pmid24451147, testicularNeoplasms));
        assertEquals(-1, table.getScore(pmid24451147, prostaticNeoplasmsCR));

        assertTrue(relevanceFile.delete());
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("pubmed.junit.MeshTreeRelevanceFileTest");
    }
}
