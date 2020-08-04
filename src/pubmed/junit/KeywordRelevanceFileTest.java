
package pubmed.junit;

import java.util.List;
import java.util.Set;

import pubmed.article.PMID;
import pubmed.bulk.BulkFile;
import pubmed.bulk.KeywordRelevanceFile;
import pubmed.flat.RelevanceScoreRecord;
import pubmed.flat.RelevanceScoreTable;
import pubmed.subject.CancerSubject;
import pubmed.subject.MeshSubject;
import pubmed.subject.Subject;

import org.junit.*;
import static org.junit.Assert.*;

public class KeywordRelevanceFileTest {
    private static final BulkFile bulkFile = BulkFile.create("data/test/pubmed_sample.xml");
    private static final KeywordRelevanceFile keywordFile = KeywordRelevanceFile.from(bulkFile);

    private static final Subject cancer = CancerSubject.INSTANCE;
    private static final Subject rhinovirus = MeshSubject.create("D012229");
    private static final Subject atorvastatin = MeshSubject.create("D000069059");

    @Test public void testFile() {
        keywordFile.delete();
        List<Subject> subjects = List.of(cancer, rhinovirus, atorvastatin);

        assertTrue(keywordFile.loadTOC().isEmpty());
        assertEquals(subjects, keywordFile.findUnprocessed(subjects));

        keywordFile.process(List.of(cancer));
        keywordFile.process(List.of(cancer));

        assertEquals(Set.of("CANCER"), keywordFile.loadTOC());

        keywordFile.process(List.of(cancer, rhinovirus, atorvastatin));

        RelevanceScoreTable table = keywordFile.load();

        assertEquals(6, table.count());
        assertEquals( 1, table.select(PMID.instance(31383287), cancer.getKey()).getScore());
        assertEquals(-1, table.select(PMID.instance(31687927), cancer.getKey()).getScore());
        assertEquals(-1, table.select(PMID.instance(31383287), atorvastatin.getKey()).getScore());
        assertEquals(-1, table.select(PMID.instance(31687927), atorvastatin.getKey()).getScore());
        assertEquals(-1, table.select(PMID.instance(31383287), rhinovirus.getKey()).getScore());
        assertEquals( 1, table.select(PMID.instance(31687927), rhinovirus.getKey()).getScore());

        assertTrue(keywordFile.delete());
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("pubmed.junit.KeywordRelevanceFileTest");
    }
}
