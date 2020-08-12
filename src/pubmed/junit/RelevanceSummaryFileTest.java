
package pubmed.junit;

import java.util.List;
import java.util.Set;

import pubmed.article.PMID;
import pubmed.bulk.BulkFile;
import pubmed.relev.RelevanceSummaryFile;
import pubmed.relev.RelevanceSummaryRecord;
import pubmed.relev.RelevanceSummaryTable;
import pubmed.subject.CancerSubject;
import pubmed.subject.Subject;

import org.junit.*;
import static org.junit.Assert.*;

public class RelevanceSummaryFileTest {
    static {
        System.setProperty(RelevanceSummaryFile.RELEV_DIR_PROPERTY, "data/test");
    }

    private static final BulkFile bulkFile = BulkFile.create("data/test/pubmed_sample.xml");

    private static final RelevanceSummaryFile cancerFile = RelevanceSummaryFile.instance(CancerSubject.INSTANCE);

    @Test public void testCancerFile() {
        cancerFile.delete();

        assertTrue(cancerFile.loadContrib().isEmpty());
        assertFalse(cancerFile.isContributor(bulkFile));

        RelevanceSummaryFile.process(bulkFile, List.of(CancerSubject.INSTANCE));

        assertFalse(cancerFile.loadContrib().isEmpty());
        assertTrue(cancerFile.isContributor(bulkFile));

        RelevanceSummaryTable cancerTable = cancerFile.load();
        assertEquals(2, cancerTable.count());
        
        RelevanceSummaryRecord rec1 = cancerTable.select(PMID.instance(24451147), "CANCER");
        RelevanceSummaryRecord rec2 = cancerTable.select(PMID.instance(31383287), "CANCER");

        assertEquals(PMID.instance(24451147), rec1.getPMID());
        assertEquals("CANCER", rec1.getSubjectKey());
        assertEquals(1, rec1.getTitleScore());
        assertEquals(6, rec1.getAbstractScore());
        assertEquals(1, rec1.getMeshTreeScore());
        assertEquals(0, rec1.getHeadingListScore());
        assertEquals(0, rec1.getKeywordListScore());
        assertEquals(0, rec1.getChemicalListScore());

        assertEquals(PMID.instance(31383287), rec2.getPMID());
        assertEquals("CANCER", rec2.getSubjectKey());
        assertEquals(2, rec2.getTitleScore());
        assertEquals(4, rec2.getAbstractScore());
        assertEquals(0, rec2.getMeshTreeScore());
        assertEquals(0, rec2.getHeadingListScore());
        assertEquals(1, rec2.getKeywordListScore());
        assertEquals(0, rec2.getChemicalListScore());

        assertTrue(cancerFile.delete());
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("pubmed.junit.RelevanceSummaryFileTest");
    }
}
