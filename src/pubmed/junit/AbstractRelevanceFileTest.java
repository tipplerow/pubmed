
package pubmed.junit;

import java.util.List;
import java.util.Set;

import pubmed.article.PMID;
import pubmed.bulk.AbstractRelevanceFile;
import pubmed.bulk.BulkFile;
import pubmed.flat.RelevanceScoreRecord;
import pubmed.flat.RelevanceScoreTable;
import pubmed.subject.CancerSubject;
import pubmed.subject.MeshSubject;
import pubmed.subject.Subject;

import org.junit.*;
import static org.junit.Assert.*;

public class AbstractRelevanceFileTest {
    private static final BulkFile bulkFile = BulkFile.create("data/test/pubmed_sample.xml");
    private static final AbstractRelevanceFile abstractFile = AbstractRelevanceFile.from(bulkFile);

    private static final Subject cancer = CancerSubject.INSTANCE;
    private static final Subject atorvastatin = MeshSubject.create("D000069059");
    private static final Subject osteoarthritis = MeshSubject.create("D010003");

    @Test public void testFile() {
        abstractFile.delete();
        List<Subject> subjects = List.of(cancer, atorvastatin, osteoarthritis);

        assertTrue(abstractFile.loadTOC().isEmpty());
        assertEquals(subjects, abstractFile.findUnprocessed(subjects));

        abstractFile.process(List.of(cancer));
        abstractFile.process(List.of(cancer));

        assertEquals(Set.of("CANCER"), abstractFile.loadTOC());

        abstractFile.process(List.of(cancer, atorvastatin, osteoarthritis));

        RelevanceScoreTable table = abstractFile.load();

        assertEquals(4, table.count());
        assertEquals(6, table.select(PMID.instance(24451147), cancer.getKey()).getScore());
        assertEquals(4, table.select(PMID.instance(31383287), cancer.getKey()).getScore());
        assertEquals(3, table.select(PMID.instance(24451147), atorvastatin.getKey()).getScore());
        assertEquals(5, table.select(PMID.instance(31383387), osteoarthritis.getKey()).getScore());

        assertTrue(abstractFile.delete());
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("pubmed.junit.AbstractRelevanceFileTest");
    }
}
