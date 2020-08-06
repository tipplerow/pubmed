
package pubmed.junit;

import java.util.List;
import java.util.Set;

import pubmed.article.PMID;
import pubmed.bulk.BulkFile;
import pubmed.bulk.HeadingRelevanceFile;
import pubmed.flat.RelevanceScoreRecord;
import pubmed.flat.RelevanceScoreTable;
import pubmed.subject.CancerSubject;
import pubmed.subject.MeshSubject;
import pubmed.subject.Subject;

import org.junit.*;
import static org.junit.Assert.*;

public class HeadingRelevanceFileTest {
    private static final BulkFile bulkFile = BulkFile.create("data/test/pubmed_sample.xml");
    private static final HeadingRelevanceFile headingFile = HeadingRelevanceFile.from(bulkFile);

    private static final Subject cancer = CancerSubject.INSTANCE;
    private static final Subject tocilizumab = MeshSubject.create("C502936");
    private static final Subject atorvastatin = MeshSubject.create("D000069059");
    private static final Subject osteoarthritis = MeshSubject.create("D010003");

    @Test public void testFile() {
        headingFile.delete();
        List<Subject> subjects = List.of(cancer, tocilizumab, atorvastatin, osteoarthritis);

        assertTrue(headingFile.loadTOC().isEmpty());
        assertEquals(subjects, headingFile.findUnprocessed(subjects));

        headingFile.process(List.of(cancer));
        headingFile.process(List.of(cancer));

        assertEquals(Set.of("CANCER"), headingFile.loadTOC());

        headingFile.process(List.of(cancer, tocilizumab, atorvastatin, osteoarthritis));

        RelevanceScoreTable table = headingFile.load();

        assertEquals(6, table.count());
        assertEquals(-1, table.getScore(PMID.instance(1), atorvastatin));
        assertEquals( 1, table.getScore(PMID.instance(24451147), atorvastatin));
        assertEquals(-1, table.getScore(PMID.instance(31383582), atorvastatin));
        assertEquals(-1, table.getScore(PMID.instance(1), osteoarthritis));
        assertEquals(-1, table.getScore(PMID.instance(24451147), osteoarthritis));
        assertEquals(-1, table.getScore(PMID.instance(31383582), osteoarthritis));

        assertTrue(headingFile.delete());
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("pubmed.junit.HeadingRelevanceFileTest");
    }
}
