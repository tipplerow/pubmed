
package pubmed.junit;

import java.util.List;
import java.util.Set;

import pubmed.article.PMID;
import pubmed.bulk.BulkFile;
import pubmed.bulk.ChemicalRelevanceFile;
import pubmed.flat.RelevanceScoreRecord;
import pubmed.flat.RelevanceScoreTable;
import pubmed.subject.CancerSubject;
import pubmed.subject.MeshSubject;
import pubmed.subject.Subject;

import org.junit.*;
import static org.junit.Assert.*;

public class ChemicalRelevanceFileTest {
    private static final BulkFile bulkFile = BulkFile.create("data/test/pubmed_sample.xml");
    private static final ChemicalRelevanceFile chemicalFile = ChemicalRelevanceFile.from(bulkFile);

    private static final Subject cancer = CancerSubject.INSTANCE;
    private static final Subject atorvastatin = MeshSubject.create("D000069059");
    private static final Subject osteoarthritis = MeshSubject.create("D010003");

    @Test public void testFile() {
        chemicalFile.delete();
        List<Subject> subjects = List.of(cancer, atorvastatin, osteoarthritis);

        assertTrue(chemicalFile.loadTOC().isEmpty());
        assertEquals(subjects, chemicalFile.findUnprocessed(subjects));

        chemicalFile.process(List.of(cancer));
        chemicalFile.process(List.of(cancer));

        assertEquals(Set.of("CANCER"), chemicalFile.loadTOC());

        chemicalFile.process(List.of(cancer, atorvastatin, osteoarthritis));

        RelevanceScoreTable table = chemicalFile.load();

        assertEquals(2, table.count());
        assertEquals(-1, table.select(PMID.instance(1), atorvastatin.getKey()).getScore());
        assertEquals( 1, table.select(PMID.instance(24451147), atorvastatin.getKey()).getScore());

        assertTrue(chemicalFile.delete());
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("pubmed.junit.ChemicalRelevanceFileTest");
    }
}
