
package pubmed.junit;

import java.time.LocalDate;
import java.util.List;

import pubmed.article.DOI;
import pubmed.article.ISSN;
import pubmed.article.PMID;
import pubmed.article.PublicationType;
import pubmed.article.PubmedJournal;
import pubmed.mesh.MeshDescriptorKey;
import pubmed.mesh.MeshHeading;
import pubmed.mesh.MeshQualifierKey;
import pubmed.mesh.MeshRecordKey;
import pubmed.xml.PubmedArticleElement;
import pubmed.xml.PubmedXmlDocument;

import org.junit.*;
import static org.junit.Assert.*;

public class PubmedXmlDocumentTest {
    private static final String SAMPLE_XML = "data/test/pubmed_sample.xml";

    private static final PubmedXmlDocument document = PubmedXmlDocument.parse(SAMPLE_XML);

    private PubmedArticleElement getPubmedArticleElement(int index) {
        return document.getPubmedArticleElements().get(index);
    }

    @Test public void testAbstract() {
        assertTrue(getPubmedArticleElement(0).getAbstract().startsWith("Epidemiological studies indicate"));
        assertTrue(getPubmedArticleElement(0).getAbstract().endsWith("effects of extracellular ATP."));

        assertTrue(getPubmedArticleElement(2).getAbstract().startsWith("Pain-related affective"));
        assertTrue(getPubmedArticleElement(2).getAbstract().endsWith("receiving conservative treatment."));

        assertNull(getPubmedArticleElement(3).getAbstract());
    }

    @Test public void testArticleDates() {
        assertEquals(List.of(LocalDate.of(2014, 1, 22)), getPubmedArticleElement(0).getArticleDates());
        assertEquals(List.of(LocalDate.of(2019, 6, 15)), getPubmedArticleElement(1).getArticleDates());
        assertEquals(List.of(), getPubmedArticleElement(3).getArticleDates());
    }

    @Test public void testArticleTitle() {
        assertTrue(getPubmedArticleElement(0).getArticleTitle().startsWith("Atorvastatin prevents ATP driven"));
        assertTrue(getPubmedArticleElement(0).getArticleTitle().endsWith("PTEN-expressing prostate cancer cells."));

        assertTrue(getPubmedArticleElement(1).getArticleTitle().startsWith("A 3D tumor spheroid model"));
        assertTrue(getPubmedArticleElement(1).getArticleTitle().endsWith("line phenotypic characterization (V2)."));
    }

    @Test public void testChemicalList() {
        List<MeshRecordKey> chemicalList =
            getPubmedArticleElement(0).getChemicalList();

        assertEquals(12, chemicalList.size());
        assertEquals(MeshRecordKey.instance("D002352"), chemicalList.get(0));
        assertEquals(MeshRecordKey.instance("C494929"), chemicalList.get(11));

        assertEquals(List.of(), getPubmedArticleElement(1).getChemicalList());
    }

    @Test public void testDOI() {
        assertEquals(DOI.instance("10.1093/carcin/bgu019"), getPubmedArticleElement(0).getDOI());
        assertEquals(DOI.instance("10.1016/j.tice.2019.05.007"), getPubmedArticleElement(1).getDOI());
    }

    @Test public void testDateCompleted() {
        assertEquals(LocalDate.of(2014, 9, 25), getPubmedArticleElement(0).getDateCompleted());
        assertNull(getPubmedArticleElement(1).getDateCompleted());
    }

    @Test public void testDateRevised() {
        assertEquals(LocalDate.of(2018, 12, 2), getPubmedArticleElement(0).getDateRevised());
        assertEquals(LocalDate.of(2019, 8, 6), getPubmedArticleElement(1).getDateRevised());
    }

    @Test public void testDeleteCitations() {
        assertEquals(List.of(PMID.instance(30830050),
                             PMID.instance(30830053),
                             PMID.instance(30830055)),
                     document.getDeletedCitations());
    }

    @Test public void testKeywordList() {
        List<String> keywords = getPubmedArticleElement(1).getKeywordList();

        assertEquals(List.of("Glioblastoma",
                             "Glioblastoma cell lines",
                             "Invasion",
                             "Multicellular spheroids",
                             "T98G"), keywords);

        assertTrue(getPubmedArticleElement(0).getKeywordList().isEmpty());
    }

    @Test public void testMeshHeadingList() {
        List<MeshHeading> headings = getPubmedArticleElement(0).getMeshHeadingList();

        assertEquals(22, headings.size());

        assertEquals(MeshDescriptorKey.instance("D000255"), headings.get(0).getDescriptorKey());
        assertEquals(List.of(MeshQualifierKey.instance("Q000009")), headings.get(0).viewQualifierKeys());

        assertEquals(MeshDescriptorKey.instance("D000069059"), headings.get(1).getDescriptorKey());
        assertEquals(List.of(), headings.get(1).viewQualifierKeys());

        assertEquals(MeshDescriptorKey.instance("D058486"), headings.get(19).getDescriptorKey());
        assertEquals(List.of(MeshQualifierKey.instance("Q000737"),
                             MeshQualifierKey.instance("Q000235"),
                             MeshQualifierKey.instance("Q000378")),
                     headings.get(19).viewQualifierKeys());

        assertTrue(getPubmedArticleElement(1).getMeshHeadingList().isEmpty());
    }

    @Test public void testPMID() {
        assertEquals(PMID.instance(24451147), getPubmedArticleElement(0).getPMID());
        assertEquals(PMID.instance(31383287), getPubmedArticleElement(1).getPMID());
    }

    @Test public void testPublicationTypes() {
        List<PublicationType> types0 = getPubmedArticleElement(0).getPublicationTypes();
        List<PublicationType> types1 = getPubmedArticleElement(1).getPublicationTypes();
        List<PublicationType> types3 = getPubmedArticleElement(3).getPublicationTypes();

        assertEquals(2, types0.size());
        assertEquals(MeshDescriptorKey.instance("D016428"), types0.get(0).getDescriptorKey());
        assertEquals(MeshDescriptorKey.instance("D013485"), types0.get(1).getDescriptorKey());

        assertEquals(1, types1.size());
        assertEquals(MeshDescriptorKey.instance("D016428"), types1.get(0).getDescriptorKey());

        assertEquals(2, types3.size());
        assertEquals(MeshDescriptorKey.instance("D016428"), types3.get(0).getDescriptorKey());
        assertEquals(MeshDescriptorKey.instance("D013487"), types3.get(1).getDescriptorKey());
    }

    @Test public void testPubmedDate() {
        assertEquals(LocalDate.of(2014, 1, 24), getPubmedArticleElement(0).getPubmedDate());
        assertEquals(LocalDate.of(2019, 8,  7), getPubmedArticleElement(1).getPubmedDate());
        assertEquals(LocalDate.of(1975, 6,  1), getPubmedArticleElement(3).getPubmedDate());
    }

    @Test public void testPubmedJournal() {
        assertEquals(ISSN.instance("1460-2180"), getPubmedArticleElement(0).getPubmedJournal().getISSN());
        assertEquals("Tissue Cell", getPubmedArticleElement(1).getPubmedJournal().getISOAbbreviation());
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("pubmed.junit.PubmedXmlDocumentTest");
    }
}
