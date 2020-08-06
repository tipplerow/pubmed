
package pubmed.junit;

import java.util.List;
import java.util.Set;

import pubmed.article.PMID;
import pubmed.bulk.BulkFile;
import pubmed.bulk.TitleRelevanceFile;
import pubmed.flat.RelevanceScoreRecord;
import pubmed.flat.RelevanceScoreTable;
import pubmed.subject.CancerSubject;
import pubmed.subject.MeshSubject;
import pubmed.subject.Subject;

import org.junit.*;
import static org.junit.Assert.*;

public class TitleRelevanceFileTest {
    private static final BulkFile bulkFile = BulkFile.create("data/test/pubmed_sample.xml");
    private static final TitleRelevanceFile titleFile = TitleRelevanceFile.from(bulkFile);

    private static final Subject cancer = CancerSubject.INSTANCE;
    private static final Subject atorvastatin = MeshSubject.create("D000069059");
    private static final Subject osteoarthritis = MeshSubject.create("D010003");

    @Test public void testFile() {
        titleFile.delete();
        List<Subject> subjects = List.of(cancer, atorvastatin, osteoarthritis);

        assertTrue(titleFile.loadTOC().isEmpty());
        assertEquals(subjects, titleFile.findUnprocessed(subjects));

        titleFile.process(List.of(cancer));
        titleFile.process(List.of(cancer));

        assertEquals(Set.of("CANCER"), titleFile.loadTOC());

        titleFile.process(List.of(cancer, atorvastatin, osteoarthritis));

        RelevanceScoreTable table = titleFile.load();

        assertEquals(4, table.count());
        assertEquals(1, table.getScore(PMID.instance(24451147), cancer));
        assertEquals(2, table.getScore(PMID.instance(31383287), cancer));
        assertEquals(1, table.getScore(PMID.instance(24451147), atorvastatin));
        assertEquals(1, table.getScore(PMID.instance(31383387), osteoarthritis));

        assertTrue(titleFile.delete());
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("pubmed.junit.TitleRelevanceFileTest");
    }
}
