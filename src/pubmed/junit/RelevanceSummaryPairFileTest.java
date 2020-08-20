
package pubmed.junit;

import java.util.List;
import java.util.Set;

import pubmed.article.PMID;
import pubmed.bulk.BulkFile;
import pubmed.relev.RelevanceSummaryPairFile;
import pubmed.relev.RelevanceSummaryRecord;
import pubmed.relev.RelevanceSummarySubjectFile;
import pubmed.relev.RelevanceSummaryTable;
import pubmed.subject.CancerSubject;
import pubmed.subject.MeshSubject;
import pubmed.subject.Subject;

import org.junit.*;
import static org.junit.Assert.*;

public class RelevanceSummaryPairFileTest {
    static {
        System.setProperty(RelevanceSummarySubjectFile.RELEV_DIR_PROPERTY, "data/test/relevance_pair");
    }

    private static final BulkFile bulkFile = BulkFile.create("data/test/pubmed_sample.xml");

    private static final Subject cancer = CancerSubject.INSTANCE;
    private static final Subject atorvastatin = MeshSubject.create("D000069059");
    private static final Subject osteoarthritis = MeshSubject.create("D010003");

    private static final RelevanceSummarySubjectFile cancerFile = RelevanceSummarySubjectFile.instance(cancer);
    private static final RelevanceSummarySubjectFile atorvastatinFile = RelevanceSummarySubjectFile.instance(atorvastatin);
    private static final RelevanceSummarySubjectFile osteoarthritisFile = RelevanceSummarySubjectFile.instance(osteoarthritis);

    private static final RelevanceSummaryPairFile cancerAtorvastatinFile =
        RelevanceSummaryPairFile.instance(cancer, atorvastatin);

    private static final RelevanceSummaryPairFile cancerOsteoarthritisFile =
        RelevanceSummaryPairFile.instance(cancer, osteoarthritis);

    @Test public void testCancerFile() {
        cancerFile.delete();
        atorvastatinFile.delete();
        osteoarthritisFile.delete();

        //assertTrue(cancerFile.loadContrib().isEmpty());
        assertFalse(cancerFile.isContributor(bulkFile));

        RelevanceSummarySubjectFile.process(bulkFile, List.of(cancer, atorvastatin, osteoarthritis));
        RelevanceSummaryPairFile.process(cancer, List.of(atorvastatin, osteoarthritis));

        RelevanceSummaryTable atorvastatinTable = cancerAtorvastatinFile.load();
        RelevanceSummaryTable osteoarthritisTable = cancerOsteoarthritisFile.load();

        assertEquals(1, atorvastatinTable.size());
        assertEquals(0, osteoarthritisTable.size());

        assertRecord(atorvastatinTable.get(PMID.instance(24451147), "D000069059"), 1, 3, 1, 1, 0, 1);

        assertTrue(cancerFile.delete());
        assertTrue(atorvastatinFile.delete());
        assertTrue(osteoarthritisFile.delete());

        assertTrue(cancerAtorvastatinFile.delete());
        assertTrue(cancerOsteoarthritisFile.delete());
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
        org.junit.runner.JUnitCore.main("pubmed.junit.RelevanceSummaryPairFileTest");
    }
}
