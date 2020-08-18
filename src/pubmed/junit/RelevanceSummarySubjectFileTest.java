
package pubmed.junit;

import java.util.List;
import java.util.Set;

import pubmed.article.PMID;
import pubmed.bulk.BulkFile;
import pubmed.relev.RelevanceSummarySubjectFile;
import pubmed.relev.RelevanceSummaryRecord;
import pubmed.relev.RelevanceSummaryTable;
import pubmed.subject.CancerSubject;
import pubmed.subject.MeshSubject;
import pubmed.subject.Subject;

import org.junit.*;
import static org.junit.Assert.*;

public class RelevanceSummarySubjectFileTest {
    static {
        System.setProperty(RelevanceSummarySubjectFile.RELEV_DIR_PROPERTY, "data/test");
    }

    private static final BulkFile bulkFile = BulkFile.create("data/test/pubmed_sample.xml");

    private static final Subject cancer = CancerSubject.INSTANCE;
    private static final Subject atorvastatin = MeshSubject.create("D000069059");
    private static final Subject osteoarthritis = MeshSubject.create("D010003");

    private static final RelevanceSummarySubjectFile cancerFile = RelevanceSummarySubjectFile.instance(cancer);
    private static final RelevanceSummarySubjectFile atorvastatinFile = RelevanceSummarySubjectFile.instance(atorvastatin);
    private static final RelevanceSummarySubjectFile osteoarthritisFile = RelevanceSummarySubjectFile.instance(osteoarthritis);

    @Test public void testCancerFile() {
        cancerFile.delete();
        atorvastatinFile.delete();
        osteoarthritisFile.delete();

        assertTrue(cancerFile.loadContrib().isEmpty());
        assertFalse(cancerFile.isContributor(bulkFile));

        RelevanceSummarySubjectFile.process(bulkFile, List.of(cancer, atorvastatin, osteoarthritis));

        assertFalse(cancerFile.loadContrib().isEmpty());
        assertTrue(cancerFile.isContributor(bulkFile));

        RelevanceSummaryTable cancerTable = cancerFile.load();
        RelevanceSummaryTable atorvastatinTable = atorvastatinFile.load();
        RelevanceSummaryTable osteoarthritisTable = osteoarthritisFile.load();

        assertEquals(2, cancerTable.size());
        assertEquals(1, atorvastatinTable.size());
        assertEquals(1, osteoarthritisTable.size());

        assertRecord(cancerTable.get(PMID.instance(24451147), CancerSubject.INSTANCE), 1, 6, 1, 0, 0, 0);
        assertRecord(cancerTable.get(PMID.instance(31383287), CancerSubject.INSTANCE), 2, 4, 0, 0, 1, 0);
        assertRecord(atorvastatinTable.get(PMID.instance(24451147), "D000069059"), 1, 3, 1, 1, 0, 1);
        assertRecord(osteoarthritisTable.get(PMID.instance(31383387), "D010003"), 1, 5, 0, 0, 0, 0);

        assertEquals(Set.of(PMID.instance(24451147), PMID.instance(31383287)),
                     RelevanceSummarySubjectFile.loadRelevantPMID(CancerSubject.INSTANCE));

        assertEquals(Set.of(PMID.instance(24451147)),
                     RelevanceSummarySubjectFile.loadRelevantPMID(atorvastatin));

        assertTrue(cancerFile.delete());
        assertTrue(atorvastatinFile.delete());
        assertTrue(osteoarthritisFile.delete());
    }

    private void assertRecord(RelevanceSummaryRecord record,
                              int titleScore,
                              int abstractScore,
                              int meshTreeScore,
                              int headingListScore,
                              int keywordListScore,
                              int chemicalListScore) {
        assertEquals(titleScore, record.getTitleScore());
        assertEquals(abstractScore, record.getAbstractScore());
        assertEquals(meshTreeScore, record.getMeshTreeScore());
        assertEquals(headingListScore, record.getHeadingListScore());
        assertEquals(keywordListScore, record.getKeywordListScore());
        assertEquals(chemicalListScore, record.getChemicalListScore());
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("pubmed.junit.RelevanceSummarySubjectFileTest");
    }
}
