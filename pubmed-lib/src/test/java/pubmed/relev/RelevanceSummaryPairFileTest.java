
package pubmed.relev;

import java.util.List;
import java.util.Set;

import pubmed.article.PMID;
import pubmed.bulk.BulkFile;
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
    private static final Subject atorvastatin = MeshSubject.instance("D000069059");
    private static final Subject osteoarthritis = MeshSubject.instance("D010003");

    @Test public void testCancerFile() {
        RelevanceSummarySubjectFile cancerFile = RelevanceSummarySubjectFile.instance(cancer);
        RelevanceSummarySubjectFile atorvastatinFile = RelevanceSummarySubjectFile.instance(atorvastatin);
        RelevanceSummarySubjectFile osteoarthritisFile = RelevanceSummarySubjectFile.instance(osteoarthritis);

        RelevanceSummaryPairFile cancerAtorvastatinFile =
            RelevanceSummaryPairFile.instance(cancer, atorvastatin);

        RelevanceSummaryPairFile cancerOsteoarthritisFile =
            RelevanceSummaryPairFile.instance(cancer, osteoarthritis);

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
        org.junit.runner.JUnitCore.main("pubmed.relev.RelevanceSummaryPairFileTest");
    }
}
