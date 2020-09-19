
package pubmed.bulk;

import java.util.List;
import java.util.Set;

import pubmed.article.PMID;
import pubmed.flat.RelevanceScoreRecord;
import pubmed.flat.RelevanceScoreTable;
import pubmed.subject.CancerSubject;
import pubmed.subject.MeshSubject;
import pubmed.subject.Subject;

import org.junit.*;
import static org.junit.Assert.*;

public class RelevanceScoreFileTest {
    private static final BulkFile bulkFile = BulkFile.create("data/test/pubmed_sample.xml");
    private static final RelevanceScoreFile scoreFile = RelevanceScoreFile.instance(bulkFile);

    private static final Subject cancer = CancerSubject.INSTANCE;
    private static final Subject rhinovirus = MeshSubject.instance("D012229");
    private static final Subject tocilizumab = MeshSubject.instance("C502936");
    private static final Subject atorvastatin = MeshSubject.instance("D000069059");
    private static final Subject osteoarthritis = MeshSubject.instance("D010003");

    private static final Subject aldehydeOxidoreductases     = MeshSubject.instance("D000445"); // The exact heading
    private static final Subject enzymes                     = MeshSubject.instance("D004798"); // Broader than the heading
    private static final Subject glycolaldehydeDehydrogenase = MeshSubject.instance("D050618"); // Narrower than the heading
    private static final Subject ketoneOxidoreductases       = MeshSubject.instance("D007658"); // Same branch as heading

    private static final Subject prostaticNeoplasms   = MeshSubject.instance("D011471"); // The exact heading
    private static final Subject urogenitalNeoplasms  = MeshSubject.instance("D014565"); // Broader than the heading
    private static final Subject prostaticNeoplasmsCR = MeshSubject.instance("D064129"); // Narrower than the heading
    private static final Subject testicularNeoplasms  = MeshSubject.instance("D013736"); // Same branch as heading

    @Test public void testFile() {
        scoreFile.delete();

        List<Subject> subjects =
            List.of(cancer,
                    rhinovirus,
                    tocilizumab,
                    atorvastatin,
                    osteoarthritis,
                    aldehydeOxidoreductases,
                    enzymes,
                    glycolaldehydeDehydrogenase,
                    ketoneOxidoreductases,
                    urogenitalNeoplasms,
                    prostaticNeoplasms,
                    prostaticNeoplasmsCR,
                    testicularNeoplasms);

        assertTrue(scoreFile.loadTOC().isEmpty());
        assertEquals(subjects, scoreFile.findUnprocessed(subjects));

        scoreFile.process(List.of(cancer));
        scoreFile.process(List.of(cancer));

        assertEquals(Set.of("CANCER_v2"), scoreFile.loadTOC());

        scoreFile.process(subjects);

        /*
        RelevanceScoreTable table = scoreFile.load();

        assertEquals(4, table.count());
        assertEquals(6, table.getScore(PMID.instance(24451147), cancer));
        assertEquals(4, table.getScore(PMID.instance(31383287), cancer));
        assertEquals(3, table.getScore(PMID.instance(24451147), atorvastatin));
        assertEquals(5, table.getScore(PMID.instance(31383387), osteoarthritis));
        */

        //assertTrue(scoreFile.delete());
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("pubmed.bulk.RelevanceScoreFileTest");
    }
}
/*
        RelevanceScoreTable table = chemicalFile.load();

        assertEquals(2, table.count());
        assertEquals(-1, table.getScore(PMID.instance(1), atorvastatin));
        assertEquals( 1, table.getScore(PMID.instance(24451147), atorvastatin));

        RelevanceScoreTable table = headingFile.load();

        assertEquals(6, table.count());
        assertEquals(-1, table.getScore(PMID.instance(1), atorvastatin));
        assertEquals( 1, table.getScore(PMID.instance(24451147), atorvastatin));
        assertEquals(-1, table.getScore(PMID.instance(31383582), atorvastatin));
        assertEquals(-1, table.getScore(PMID.instance(1), osteoarthritis));
        assertEquals(-1, table.getScore(PMID.instance(24451147), osteoarthritis));
        assertEquals(-1, table.getScore(PMID.instance(31383582), osteoarthritis));

        RelevanceScoreTable table = keywordFile.load();

        assertEquals(6, table.count());
        assertEquals( 1, table.getScore(PMID.instance(31383287), cancer));
        assertEquals(-1, table.getScore(PMID.instance(31687927), cancer));
        assertEquals(-1, table.getScore(PMID.instance(31383287), atorvastatin));
        assertEquals(-1, table.getScore(PMID.instance(31687927), atorvastatin));
        assertEquals(-1, table.getScore(PMID.instance(31383287), rhinovirus));
        assertEquals( 1, table.getScore(PMID.instance(31687927), rhinovirus));


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

        RelevanceScoreTable table = titleFile.load();

        assertEquals(4, table.count());
        assertEquals(1, table.getScore(PMID.instance(24451147), cancer));
        assertEquals(2, table.getScore(PMID.instance(31383287), cancer));
        assertEquals(1, table.getScore(PMID.instance(24451147), atorvastatin));
        assertEquals(1, table.getScore(PMID.instance(31383387), osteoarthritis));
*/
