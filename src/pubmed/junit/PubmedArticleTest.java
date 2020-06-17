
package pubmed.junit;

import java.util.List;

import jam.util.ListUtil;

import pubmed.article.ArticleType;
import pubmed.article.DOI;
import pubmed.article.ORCID;
import pubmed.article.PMID;
import pubmed.article.PubmedArticle;
import pubmed.article.PubmedAuthor;
import pubmed.article.PubmedDate;
import pubmed.article.PubmedRegistry;
import pubmed.article.PubmedXmlDocument;
import pubmed.medline.ISSN;
import pubmed.medline.MedlineJournal;
import pubmed.medline.MedlineTA;
import pubmed.medline.NlmUniqueID;
import pubmed.mesh.MeshDescriptorKey;
import pubmed.mesh.MeshHeading;
import pubmed.mesh.MeshQualifierKey;
import pubmed.mesh.MeshRecordKey;
import pubmed.nlp.LemmaList;

import org.junit.*;
import static org.junit.Assert.*;

public class PubmedArticleTest {
    private static final String SAMPLE_XML = "data/test/pubmed_sample.xml";

    private static final MeshDescriptorKey key1 = MeshDescriptorKey.instance("D000001");
    private static final MeshDescriptorKey key2 = MeshDescriptorKey.instance("D000002");
    private static final MeshDescriptorKey key3 = MeshDescriptorKey.instance("D000003");
    private static final MeshDescriptorKey key4 = MeshDescriptorKey.instance("Dxxxxxx");

    private static final PMID pmid1 = PMID.instance(24451147);
    private static final PMID pmid2 = PMID.instance(31383287);
    private static final PMID pmid3 = PMID.instance(31383387);
    private static final PMID pmid4 = PMID.instance(1);
    private static final PMID pmid5 = PMID.instance(31687927);
    private static final PMID pmidx = PMID.instance(88);

    private static final PubmedXmlDocument document = PubmedXmlDocument.parse(SAMPLE_XML);
    private static final List<PubmedArticle> articles = document.viewLatest();
    private static final PubmedRegistry registry = PubmedRegistry.create(articles);

    private PubmedArticle parseArticle(String baseName) {
        PubmedXmlDocument doc = PubmedXmlDocument.parse("data/test/" + baseName);
        assertEquals(1, doc.viewLatest().size());
        return doc.viewLatest().get(0);
    }

    @Test public void testAbstract() {
        assertAbstract(pmid1, "Epidemiological studies indicate", "extracellular ATP.");
        assertAbstract(pmid2, "Major Glioblastoma's hallmarks", "primary GB cell cultures.");
        assertAbstract(pmid3, "Pain-related affective and/or", "receiving conservative treatment.");
    }

    private void assertAbstract(PMID pmid, String start, String end) {
        assertStartEnd(registry.get(pmid).getAbstract(), start, end);
    }

    private void assertStartEnd(String actual, String start, String end) {
        assertTrue(actual.startsWith(start));
        assertTrue(actual.endsWith(end));
    }

    @Test public void testAbstractLemmas() {
        String string0 = articles.get(0).getAbstractLemmas().join();
        String string1 = articles.get(1).getAbstractLemmas().join();
        String string2 = articles.get(2).getAbstractLemmas().join();

        assertTrue(string0.startsWith("epidemiological study indicate statin"));
        assertTrue(string0.endsWith("p2x7 counteract invasive effect extracellular atp"));

        assertTrue(string1.startsWith("major glioblastoma hallmark include"));
        assertTrue(string1.endsWith("primary gb cell culture"));

        assertTrue(string2.startsWith("pain - relate affective / cognitive"));
        assertTrue(string2.endsWith("osteoarthritis receive conservative treatment"));
    }

    @Test public void testAuthorList() {
        List<PubmedAuthor> authors =
            registry.get(pmid1).viewAuthorList();

        assertEquals(5, authors.size());

        assertEquals(ORCID.instance("0000-0001-5534-7202"), authors.get(0).getORCID());
        assertNull(authors.get(1).getORCID());
        assertNull(authors.get(2).getORCID());
        assertNull(authors.get(3).getORCID());
        assertNull(authors.get(4).getORCID());

        assertEquals("Ghalali", authors.get(0).getLastName());
        assertEquals("Fredrik", authors.get(1).getForeName());
        assertEquals("H", authors.get(2).getInitials());
        assertEquals("", authors.get(3).getSuffix());
    }

    @Test public void testChemicalList() {
        assertEquals(List.of(), articles.get(1).viewChemicalList());
        assertEquals(List.of(), articles.get(2).viewChemicalList());

        assertEquals(12, articles.get(0).viewChemicalList().size());
        assertSubstance(articles.get(0).viewChemicalList().get(0), "D002352");
        assertSubstance(articles.get(0).viewChemicalList().get(1), "C485118");
        assertSubstance(articles.get(0).viewChemicalList().get(11), "C494929");
    }

    private void assertSubstance(MeshRecordKey actual, String expected) {
        assertEquals(actual.getKey(), expected);
    }

    @Test public void testCOIStatement() {
        assertEquals("", registry.get(pmid1).getCOIStatement());
        assertEquals("No competing interests declared.", registry.get(pmid2).getCOIStatement());
        assertEquals("", registry.get(pmid3).getCOIStatement());
    }

    @Test public void testDOI() {
        assertEquals(DOI.instance("10.1093/carcin/bgu019"), registry.get(pmid1).getDOI());
        assertEquals(DOI.instance("10.1016/j.tice.2019.05.007"), registry.get(pmid2).getDOI());
        assertEquals(DOI.instance("10.1016/j.jos.2019.06.016"), registry.get(pmid3).getDOI());
    }

    @Test public void testHeadingList() {
        assertEquals(List.of(), articles.get(1).viewHeadingList());
        assertEquals(List.of(), articles.get(2).viewHeadingList());

        assertHeading(articles.get(0).viewHeadingList().get(0), "D000255", "Q000009");
        assertHeading(articles.get(0).viewHeadingList().get(1), "D000069059");
        assertHeading(articles.get(0).viewHeadingList().get(3), "D002352", "Q000037", "Q000235", "Q000378");
    }

    private void assertHeading(MeshHeading heading, String descriptorKey, String... qualifierKeys) {
        assertEquals(descriptorKey, heading.getDescriptorKey().getKey());

        for (int index = 0; index < qualifierKeys.length; ++index)
            assertEquals(qualifierKeys[index], heading.viewQualifierKeys().get(index).getKey());
    }

    @Test public void testKeywordLemmas() {
        assertEquals(List.of(), articles.get(0).viewKeywordLemmas());
        assertEquals(List.of(), articles.get(2).viewKeywordLemmas());

        assertEquals(List.of(LemmaList.create("glioblastoma"),
                             LemmaList.create("glioblastoma", "cell", "line"),
                             LemmaList.create("invasion"),
                             LemmaList.create("multicellular", "spheroid"),
                             LemmaList.create("t98g")),
                     articles.get(1).viewKeywordLemmas());
    }

    @Test public void testKeywordList() {
        assertEquals(List.of(), articles.get(0).viewKeywordList());
        assertEquals(List.of(), articles.get(2).viewKeywordList());

        assertEquals(List.of("Glioblastoma",
                             "Glioblastoma cell lines",
                             "Invasion",
                             "Multicellular spheroids",
                             "T98G"),
                     articles.get(1).viewKeywordList());
    }

    @Test public void testMedlineJournal() {
        assertMedlineJournal(pmid1, "England", "Carcinogenesis", "8008055", "0143-3334");
        assertMedlineJournal(pmid2, "Scotland", "Tissue Cell", "0214745", "0040-8166");
        assertMedlineJournal(pmid3, "Japan", "J Orthop Sci", "9604934", "0949-2658");
    }

    private void assertMedlineJournal(PMID pmid, String country, String medlineTA, String nlmUniqueID, String issn) {
        MedlineJournal expected =
            MedlineJournal.create(MedlineTA.instance(medlineTA),
                                  NlmUniqueID.instance(nlmUniqueID),
                                  ISSN.instance(issn), country);

        assertEquals(expected, registry.get(pmid).getMedlineJournal());
    }

    @Test public void testMetaAnalysis() {
        PubmedArticle article = parseArticle("32260179.xml");

        assertEquals(ArticleType.META_ANALYSIS, article.getType());
    }

    @Test public void testPubDate() {
        assertEquals(PubmedDate.instance(2014, 1, 22), registry.get(pmid1).getPubDate());
        assertEquals(PubmedDate.instance(2019, 6, 15), registry.get(pmid2).getPubDate());
        assertEquals(PubmedDate.instance(2019, 8,  2), registry.get(pmid3).getPubDate());
        assertEquals(PubmedDate.instance(1975, 6,  1), registry.get(pmid4).getPubDate());
    }

    @Test public void testPublicationTypes() {
        List<MeshDescriptorKey> types1 = registry.get(pmid1).viewPubTypes();
        List<MeshDescriptorKey> types2 = registry.get(pmid2).viewPubTypes();
        List<MeshDescriptorKey> types3 = registry.get(pmid3).viewPubTypes();

        MeshDescriptorKey article = MeshDescriptorKey.instance("D016428");
        MeshDescriptorKey support = MeshDescriptorKey.instance("D013485");

        assertEquals(List.of(article, support), types1);
        assertEquals(List.of(article), types2);
        assertEquals(List.of(article), types3);
    }

    @Test public void testReferenceList() {
        assertTrue(registry.get(pmid1).viewReferenceList().isEmpty());
        assertTrue(registry.get(pmid2).viewReferenceList().isEmpty());
        assertTrue(registry.get(pmid3).viewReferenceList().isEmpty());
        assertTrue(registry.get(pmid4).viewReferenceList().isEmpty());

        assertEquals(47, registry.get(pmid5).viewReferenceList().size());
        assertEquals(PMID.instance(8241910), registry.get(pmid5).viewReferenceList().get(0));
        assertEquals(PMID.instance(21292846), registry.get(pmid5).viewReferenceList().get(46));
    }

    @Test public void testRegistry() {
        assertTrue(registry.contains(pmid1));
        assertTrue(registry.contains(pmid2));
        assertTrue(registry.contains(pmid3));
        assertTrue(registry.contains(pmid4));
        assertTrue(registry.contains(pmid5));
        assertFalse(registry.contains(pmidx));
    }

    @Test public void testTitle() {
        assertTitle(pmid1, "Atorvastatin prevents ATP driven", "prostate cancer cells.");
        assertTitle(pmid2, "A 3D tumor spheroid model", "phenotypic characterization.");
        assertTitle(pmid3, "Prediction models considering", "prospective cohort study.");
    }

    private void assertTitle(PMID pmid, String start, String end) {
        assertStartEnd(registry.get(pmid).getTitle(), start, end);
    }

    @Test public void testTitleLemmas() {
        String string0 = articles.get(0).getTitleLemmas().join();
        String string1 = articles.get(1).getTitleLemmas().join();
        String string2 = articles.get(2).getTitleLemmas().join();

        assertTrue(string0.startsWith("atorvastatin prevent atp drive"));
        assertTrue(string0.endsWith("express prostate cancer cell"));

        assertTrue(string1.startsWith("3d tumor spheroid"));
        assertTrue(string1.endsWith("line phenotypic characterization"));

        assertTrue(string2.startsWith("prediction model consider"));
        assertTrue(string2.endsWith("prospective cohort study"));
    }

    @Test public void testTitleMarkup() {
        PubmedArticle article = parseArticle("32048627.xml");

        assertTrue(article.getTitle().startsWith("In vitro comparison"));
        assertEquals(List.of("in", "vitro", "comparison"), article.getTitleLemmas().subList(0, 3));
    }

    @Test public void testVersion() {
        assertEquals(1, registry.get(pmid1).getVersion());
        assertEquals(2, registry.get(pmid2).getVersion());
        assertEquals(3, registry.get(pmid3).getVersion());
        assertEquals(1, registry.get(pmid4).getVersion());
        assertEquals(1, registry.get(pmid5).getVersion());
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("pubmed.junit.PubmedArticleTest");
    }
}
