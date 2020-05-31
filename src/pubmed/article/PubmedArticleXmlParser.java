
package pubmed.article;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.jdom2.Element;

import pubmed.medline.MedlineJournal;
import pubmed.mesh.MeshDescriptorKey;
import pubmed.mesh.MeshHeading;
import pubmed.mesh.MeshRecordKey;

/**
 * Extracts {@code PubmedArticle} objects from their XML elements.
 */
public final class PubmedArticleXmlParser extends PubmedXmlParser {
    private final Element pubmedArticleElement;

    private Element medlineCitationElement;
    private List<Element> medlineCitationChildren;

    private Element pubmedDataElement;
    private List<Element> pubmedDataChildren;

    // MedlineCitation children...
    private Element pmidElement;
    private Element articleElement;
    private Element chemicalListElement;
    private Element coiStatementElement;
    private Element commCorrListElement;
    private Element dateRevisedElement;
    private Element dateCompletedElement;
    private Element meshHeadingListElement;
    private Element medlineJournalInfoElement;

    private final List<Element> keywordListElements = new ArrayList<Element>();

    // Article children...
    private Element titleElement;
    private Element abstractElement;
    private Element pubTypeListElement;
    private Element authorListElement;
    private List<Element> articleDateElements;

    // PubmedData children...
    private Element historyElement;
    private Element articleIdListElement;
    private List<Element> referenceListElements;
    private List<Element> pubMedPubDateElements = new ArrayList<Element>();

    private PMID pmid;
    private int version;
    private DOI doi;
    private String title;
    private String abstract_;
    private String coiStatement;
    private PubmedDate pubDate;
    private MedlineJournal medlineJournal;

    private List<PMID> referenceList;
    private List<String> keywordList;
    private List<MeshHeading> headingList;
    private List<PubmedAuthor> authorList;
    private List<MeshRecordKey> chemicalList;
    private List<MeshDescriptorKey> pubTypes;
    private List<CommentCorrection> commCorrList;
        
    private PubmedArticleXmlParser(Element pubmedArticleElement) {
        this.pubmedArticleElement = pubmedArticleElement;
    }

    /**
     * Extracts the article from an XML element.
     *
     * @param pubmedArticleElement an XML element encoding a
     * {@code PubMed} article.
     *
     * @return the {@code PubMed} article encoded in the element.
     *
     * @throws RuntimeException if any parsing errors occur.
     */
    public static PubmedArticle parse(Element pubmedArticleElement) {
        PubmedArticleXmlParser parser =
            new PubmedArticleXmlParser(pubmedArticleElement);

        return parser.parse();
    }

    private PubmedArticle parse() {
        assertTagName(pubmedArticleElement, "PubmedArticle");

        medlineCitationElement = getRequiredChild(pubmedArticleElement, "MedlineCitation");
        pubmedDataElement = pubmedArticleElement.getChild("PubmedData");

        assignMedlineCitationChildren();
        assignArticleChildren();
        assignPubmedDataChildren();

        parsePMID();
        parseVersion();
        parseDOI();
        parseTitle();
        parseAbstract();
        parseAuthorList();
        parseReferenceLists();
        parsePubTypes();
        parseMedlineJournal();
        parseChemicalList();
        parseCommCorrList();
        parseMeshHeadingList();
        parseKeywordList();
        parseCOIStatement();

        // We will take the first date from the choices between the
        // date completed, date revised, and the article dates...
        resolvePubDate();

        return PubmedArticle.create(pmid,
                                    version,
                                    doi,
                                    title,
                                    abstract_,
                                    coiStatement,
                                    pubDate,
                                    medlineJournal,
                                    authorList,
                                    referenceList,
                                    keywordList,
                                    headingList,
                                    chemicalList,
                                    pubTypes,
                                    commCorrList);
    }

    private void assignMedlineCitationChildren() {
        medlineCitationChildren =
            medlineCitationElement.getChildren();

        for (Element element : medlineCitationChildren)
            assignMedlineCitationChild(element);
    }

    private void assignMedlineCitationChild(Element element) {
        switch (element.getName()) {
            //
            // The cases are ordered according to the DTD...
            //
        case "PMID":
            pmidElement = element;
            break;

        case "DateCompleted":
            dateCompletedElement = element;

        case "DateRevised":
            dateRevisedElement = element;

        case "Article":
            articleElement = element;
            break;

        case "MedlineJournalInfo":
            medlineJournalInfoElement = element;
            break;

        case "ChemicalList":
            chemicalListElement = element;
            break;

        case "MeshHeadingList":
            meshHeadingListElement = element;
            break;

        case "KeywordList":
            keywordListElements.add(element);
            break;

        case "CoiStatement":
            coiStatementElement = element;
            break;

        default:
            // Ignore this element...
            break;
        }
    }

    private void assignPubmedDataChildren() {
        if (pubmedDataElement == null)
            return;

        Element historyElement = pubmedDataElement.getChild("History");

        if (historyElement != null) {
            for (Element element : historyElement.getChildren()) {
                String pubStatus = element.getAttributeValue("PubStatus");

                if (pubStatus.equals("entrez")
                    || pubStatus.equals("medline")
                    || pubStatus.equals("pubmed"))
                    pubMedPubDateElements.add(element);
            }
        }

        articleIdListElement = getRequiredChild(pubmedDataElement, "ArticleIdList");
        referenceListElements = pubmedDataElement.getChildren("ReferenceList");
    }

    private void assignArticleChildren() {
        titleElement = getRequiredChild(articleElement, "ArticleTitle");
        abstractElement = articleElement.getChild("Abstract");
        authorListElement = articleElement.getChild("AuthorList");
        pubTypeListElement = getRequiredChild(articleElement, "PublicationTypeList");
        articleDateElements = articleElement.getChildren("ArticleDate");
    }

    private void parsePMID() {
        pmid = parsePMID(pmidElement);
    }

    private void parseVersion() {
        version = parseVersion(pmidElement);
    }

    private void parseDOI() {
        if (articleIdListElement != null)
            doi = parseDOI(articleIdListElement);
    }

    private void parseTitle() {
        title = parseArticleTitle(titleElement);
    }

    private void parseAbstract() {
        if (abstractElement == null)
            abstract_ = "";
        else
            abstract_ = parseAbstract(abstractElement);
    }

    private void parseAuthorList() {
        if (authorListElement == null)
            authorList = List.of();
        else
            authorList = parseAuthorList(authorListElement);
    }

    private void parseReferenceLists() {
        referenceList = new ArrayList<PMID>();

        for (Element referenceListElement : referenceListElements)
            referenceList.addAll(parseReferenceList(referenceListElement));
    }

    private void parsePubTypes() {
        pubTypes = parsePublicationTypeList(pubTypeListElement);
    }

    private void parseMedlineJournal() {
        medlineJournal = parseMedlineJournalInfo(medlineJournalInfoElement);
    }

    private void parseChemicalList() {
        if (chemicalListElement == null)
            chemicalList = List.of();
        else
            chemicalList = parseChemicalList(chemicalListElement);
    }

    private void parseCommCorrList() {
        if (commCorrListElement == null)
            commCorrList = List.of();
        else
            commCorrList = parseCommentsCorrectionsList(commCorrListElement);
    }

    private void parseMeshHeadingList() {
        if (meshHeadingListElement == null)
            headingList = List.of();
        else
            headingList = parseMeshHeadingList(meshHeadingListElement);
    }

    private void parseKeywordList() {
        if (keywordListElements.isEmpty()) {
            keywordList = List.of();
            return;
        }

        keywordList = new ArrayList<String>(keywordListElements.size());

        for (Element keywordListElement : keywordListElements)
            keywordList.addAll(parseKeywordList(keywordListElement));
    }

    private void parseCOIStatement() {
        if (coiStatementElement == null)
            coiStatement = "";
        else
            coiStatement = parseCOIStatement(coiStatementElement);
    }

    private void resolvePubDate() {
        //
        // Collect the available publication dates in a TreeSet so
        // that we can pick the first as the earliest...
        //
        TreeSet<PubmedDate> pubDates = new TreeSet<PubmedDate>();

        if (dateCompletedElement != null)
            addPubDate(pubDates, parsePubDate(dateCompletedElement));

        if (dateRevisedElement != null)
            addPubDate(pubDates, parsePubDate(dateRevisedElement));

        for (Element articleDateElement : articleDateElements)
            addPubDate(pubDates, parsePubDate(articleDateElement));

        for (Element pubDateElement : pubMedPubDateElements)
            addPubDate(pubDates, parsePubDate(pubDateElement));

        if (pubDates.isEmpty())
            pubDate = null;
        else
            pubDate = pubDates.first();
    }

    private void addPubDate(Collection<PubmedDate> pubDates, PubmedDate date) {
        if (date != null)
            pubDates.add(date);
    }
}
