
package pubmed.article;

import java.util.ArrayList;
import java.util.List;

import org.jdom2.Element;

import jam.xml.JDOMParser;

import pubmed.medline.MedlineJournal;
import pubmed.medline.MedlineTA;
import pubmed.medline.NlmUniqueID;

import pubmed.mesh.MeshDescriptorKey;
import pubmed.mesh.MeshHeading;
import pubmed.mesh.MeshQualifierKey;
import pubmed.mesh.MeshRecordKey;

/**
 * Provides a base class for {@code PubMed} element parsers.
 */
public abstract class PubmedXmlParser extends JDOMParser {
    /**
     * Extracts the raw abstract text from its XML element.
     *
     * @param element an {@code Abstract} XML element.
     *
     * @return the raw abstract text encoded in the given element.
     */
    public String parseAbstract(Element element) {
        assertTagName(element, "Abstract");

        List<Element> textElements =
            element.getChildren("AbstractText");

        if (textElements.size() < 2)
            return parseSimpleAbstract(textElements);
        else
            return parseStructuredAbstract(textElements);
    }

    private String parseSimpleAbstract(List<Element> textElements) {
        //
        // This is a simple, unstructured monolithic abstract:
        // just return the text...
        //
        return parseAbstractText(textElements.get(0));
    }

    private String parseAbstractText(Element element) {
        assertTagName(element, "AbstractText");
        return element.getTextNormalize();
    }

    private String parseStructuredAbstract(List<Element> textElements) {
        //
        // This is a structured multi-component abstract:  join
        // components into a single string separated by a space
        // character....
        //
        StringBuilder textBuilder = new StringBuilder();
        textBuilder.append(parseAbstractText(textElements.get(0)));

        for (int index = 1; index < textElements.size(); ++index) {
            textBuilder.append(" ");
            textBuilder.append(parseAbstractText(textElements.get(index)));
        }
        
        return textBuilder.toString();
    }

    /**
     * Extracts the article title from its XML element.
     *
     * @param element an {@code ArticleTitle} XML element.
     *
     * @return the title encoded in the given element.
     */
    public String parseArticleTitle(Element element) {
        assertTagName(element, "ArticleTitle");
        return element.getValue().trim();
    }

    /**
     * Extracts an author from its XML element.
     *
     * @param element an {@code Author} XML element.
     *
     * @return the author encoded in the element.
     */
    public PubmedAuthor parseAuthor(Element element) {
        assertTagName(element, "Author");

        if (hasChild(element, "LastName"))
            return parseSingleAuthor(element);
        else
            return parseCollectiveAuthor(element);
    }

    private PubmedAuthor parseCollectiveAuthor(Element element) {
        String collectiveName =
            getChildText(element, "CollectiveName");

        return PubmedAuthor.create(collectiveName, null, null, null, null);
    }

    private PubmedAuthor parseSingleAuthor(Element element) {
        String lastName = getChildText(element, "LastName");
        String foreName = getChildText(element, "ForeName");
        String initials = getChildText(element, "Initials");
        String suffix   = getChildText(element, "Suffix");
        ORCID  orcid    = parseORCID(element);

        return PubmedAuthor.create(lastName, foreName, initials, suffix, orcid);
    }

    private ORCID parseORCID(Element authorElement) {
        Element orcidElement =
            findChildElement(authorElement, "Identifier", "Source", "ORCID");

        if (orcidElement == null)
            return null;

        String orcidString =
            ORCID.normalize(getElementText(orcidElement));

        if (ORCID.isValid(orcidString))
            return ORCID.instance(orcidString);
        else
            return null;
    }

    /**
     * Extracts an author list from its XML element.
     *
     * @param element an {@code AuthorList} XML element.
     *
     * @return the authors encoded in the element.
     */
    public List<PubmedAuthor> parseAuthorList(Element element) {
        assertTagName(element, "AuthorList");

        List<Element> children = element.getChildren();
        List<PubmedAuthor> authors = new ArrayList<PubmedAuthor>(children.size());

        for (Element child : children)
            authors.add(parseAuthor(child));

        return authors;
    }

    /**
     * Extracts the chemical list from its XML element.
     *
     * @param element a {@code ChemicalList} XML element.
     *
     * @return the {@code MeSH} record keys of the substances named in
     * the chemical list.
     */
    public List<MeshRecordKey> parseChemicalList(Element element) {
        assertTagName(element, "ChemicalList");

        List<Element> chemicalElements =
            element.getChildren();

        List<MeshRecordKey> substances =
            new ArrayList<MeshRecordKey>(chemicalElements.size());

        for (Element chemicalElement : chemicalElements)
            substances.add(parseChemical(chemicalElement));

        return substances;
    }

    private MeshRecordKey parseChemical(Element element) {
        assertTagName(element, "Chemical");
        return parseNameOfSubstance(getRequiredChild(element, "NameOfSubstance"));
    }

    private MeshRecordKey parseNameOfSubstance(Element element) {
        assertTagName(element, "NameOfSubstance");
        return MeshRecordKey.instance(getRequiredAttribute(element, "UI"));
    }

    /**
     * Extracts the conflict-of-interest statement from its XML element.
     *
     * @param element a {@code CoiStatement} XML element.
     *
     * @return the conflic-of-interest statement contained in the element.
     */
    public String parseCOIStatement(Element element) {
        assertTagName(element, "CoiStatement");
        return element.getTextNormalize();
    }

    /**
     * Extracts the comments-corrections list from its XML element.
     *
     * @param element a {@code CommentsCorrectionsList} XML element.
     *
     * @return the comments-corrections records contained in the
     * element.
     */
    public List<CommentCorrection> parseCommentsCorrectionsList(Element element) {
        assertTagName(element, "CommentsCorrectionsList");

        List<Element> commCorrElements =
            element.getChildren();

        List<CommentCorrection>  commCorrObjects =
            new ArrayList<CommentCorrection>(commCorrElements.size());

        for (Element commCorrElement : commCorrElements) {
            CommentCorrectionType commCorrType =
                CommentCorrectionType.valueOf(commCorrElement.getAttributeValue("RefType"));

            Element PMIDElement = commCorrElement.getChild("PMID");

            if (PMIDElement == null)
                continue;

            PMID originalPMID = parsePMID(PMIDElement);
            commCorrObjects.add(CommentCorrection.create(originalPMID, commCorrType));
        }

        return commCorrObjects;
    }

    /**
     * Extracts the DOI from an article identifier list.
     *
     * @param articleIdListElement an {@code ArticleIdList} element.
     *
     * @return the DOI from the identifier list ({@code null} if there
     * is no DOI in the list).
     */
    public DOI parseDOI(Element articleIdListElement) {
        Element doiElement = findChildElement(articleIdListElement, "ArticleId", "IdType", "doi");

        if (doiElement != null)
            return DOI.instance(getElementText(doiElement));
        else
            return null;
    }

    /**
     * Extracts the {@code MeshHeading} records from their XML element.
     *
     * @param element a {@code MeshHeadingList} XML element.
     *
     * @return the {@code MeshHeading} records contained in the list.
     */
    public List<MeshHeading> parseMeshHeadingList(Element element) {
        assertTagName(element, "MeshHeadingList");

        List<Element> headingElements =
            element.getChildren();

        List<MeshHeading> headings =
            new ArrayList<MeshHeading>(headingElements.size());

        for (Element headingElement : headingElements)
            headings.add(parseMeshHeading(headingElement));

        return headings;
    }

    private MeshHeading parseMeshHeading(Element element) {
        assertTagName(element, "MeshHeading");
        List<Element> children = element.getChildren();

        MeshDescriptorKey descriptorKey = parseHeadingDescriptorName(children);
        List<MeshQualifierKey> qualifierKeys = parseHeadingQualifierKeys(children);

        return MeshHeading.create(descriptorKey, qualifierKeys);
    }

    private MeshDescriptorKey parseHeadingDescriptorName(List<Element> children) {
        //
        // The descriptor key is always the first element...
        //
        return MeshDescriptorKey.instance(getRequiredAttribute(children.get(0), "UI"));
    }

    private List<MeshQualifierKey> parseHeadingQualifierKeys(List<Element> children) {
        //
        // Elements [1, children.size()) are the qualifiers...
        //
        if (children.size() < 2)
            return List.of();

        List<MeshQualifierKey> qualifierKeys =
            new ArrayList<MeshQualifierKey>(children.size() - 1);

        for (int index = 1; index < children.size(); ++index)
            qualifierKeys.add(MeshQualifierKey.instance(getRequiredAttribute(children.get(index), "UI")));

        return qualifierKeys;
    }

    /**
     * Extracts keywords from their XML element.
     *
     * @param element a {@code KeywordList} XML element.
     *
     * @return the keywords contained in the keyword list (an empty
     * set if the element is {@code null}).
     */
    public List<String> parseKeywordList(Element element) {
        assertTagName(element, "KeywordList");

        List<Element> keywordElements =
            element.getChildren();

        List<String> keywords =
            new ArrayList<String>(keywordElements.size());

        for (Element keywordElement : keywordElements)
            keywords.add(parseKeyword(keywordElement));

        return keywords;
    }

    private String parseKeyword(Element element) {
        assertTagName(element, "Keyword");
        return element.getTextNormalize();
    }

    /**
     * Creates a {@code MedlineJournal} object from its XML element.
     *
     * @param element a {@code MedlineJournalInfo} XML element.
     *
     * @return the {@code MedlineJournal} object encoded in the given
     * element.
     */
    public MedlineJournal parseMedlineJournalInfo(Element element) {
        assertTagName(element, "MedlineJournalInfo");

        Element countryElement = element.getChild("Country");
        Element medlineTAElement = getRequiredChild(element, "MedlineTA");
        Element nlmUniqueIDElement = element.getChild("NlmUniqueID");
        Element issnElement = element.getChild("ISSNLinking");

        ISSN issn = parseISSNLinking(element);
        String country = parseMedlineCountry(element);
        MedlineTA medlineTA = parseMedlineTA(element);
        NlmUniqueID nlmUniqueID = parseNlmUniqueID(element);

        return MedlineJournal.create(medlineTA, nlmUniqueID, issn, country);
    }

    private ISSN parseISSNLinking(Element medlineJournalInfoElement) {
        Element issnLinkingElement =
            medlineJournalInfoElement.getChild("ISSNLinking");

        if (issnLinkingElement == null)
            return null;

        String issn = issnLinkingElement.getTextNormalize();

        if (ISSN.isValid(issn))
            return ISSN.instance(issn);
        else
            return null;
    }

    private String parseMedlineCountry(Element medlineJournalInfoElement) {
        Element countryElement =
            medlineJournalInfoElement.getChild("Country");

        if (countryElement == null)
            return null;
        else
            return countryElement.getTextNormalize();
    }

    private MedlineTA parseMedlineTA(Element medlineJournalInfoElement) {
        return MedlineTA.instance(getRequiredChild(medlineJournalInfoElement, "MedlineTA").getTextNormalize());
    }

    private NlmUniqueID parseNlmUniqueID(Element medlineJournalInfoElement) {
        Element nlmUniqueIDElement =
            medlineJournalInfoElement.getChild("NlmUniqueID");

        if (nlmUniqueIDElement == null)
            return null;

        String id = nlmUniqueIDElement.getTextNormalize();

        if (NlmUniqueID.isValid(id))
            return NlmUniqueID.instance(id);
        else
            return null;
    }

    /**
     * Extracts the {@code PMID} from its XML element.
     *
     * @param element a {@code PMID} XML element.
     *
     * @return the {@code PMID} encoded in the given element.
     */
    public PMID parsePMID(Element element) {
        assertTagName(element, "PMID");
        return PMID.instance(element.getTextNormalize());
    }

    /**
     * Extracts the publication date from an XML element.
     *
     * @param element an element containing a {@code PubmedDate} in
     * standard format.
     *
     * @return the publication date encoded in the given element.
     */
    public PubmedDate parsePubDate(Element element) {
        //
        // We do not check the name of the element because several
        // element types encode dates with the same representation...
        //
        int year  = Integer.parseInt(getRequiredChild(element, "Year").getTextNormalize());
        int month = Integer.parseInt(getRequiredChild(element, "Month").getTextNormalize());
        int day   = Integer.parseInt(getRequiredChild(element, "Day").getTextNormalize());

        if (PubmedDate.isValid(year, month, day))
            return PubmedDate.instance(year, month, day);
        else
            return null;
    }

    /**
     * Extracts publication types from their XML element.
     *
     * @param element a {@code PublicationTypeList} XML element.
     *
     * @return the descriptor keys publication keywords contained in the keyword list (an empty
     * set if the element is {@code null}).
     */
    public List<MeshDescriptorKey> parsePublicationTypeList(Element element) {
        assertTagName(element, "PublicationTypeList");

        List<Element> pubTypeElements =
            element.getChildren();

        List<MeshDescriptorKey> pubTypes =
            new ArrayList<MeshDescriptorKey>(pubTypeElements.size());

        for (Element pubTypeElement : pubTypeElements)
            pubTypes.add(parsePubType(pubTypeElement));

        return pubTypes;
    }

    private MeshDescriptorKey parsePubType(Element element) {
        assertTagName(element, "PublicationType");
        return MeshDescriptorKey.instance(getRequiredAttribute(element, "UI"));
    }

    /**
     * Extracts the {@code PubMed} identifier from a reference element.
     *
     * @param referenceElement a {@code Reference} element.
     *
     * @return the {@code PubMed} identifier encoded in the reference
     * element (or {@code null} if there is no encoded identifier).
     */
    public PMID parseReference(Element referenceElement) {
        assertTagName(referenceElement, "Reference");

        Element articleIdListElement =
            referenceElement.getChild("ArticleIdList");

        if (articleIdListElement == null)
            return null;

        Element pmidElement =
            findChildElement(articleIdListElement, "ArticleId", "IdType", "pubmed");

        if (pmidElement == null)
            return null;
        else
            return PMID.instance(getElementText(pmidElement));
    }

    /**
     * Extracts all {@code PubMed} identifiers from a reference list
     * element.
     *
     * @param referenceListElement a {@code ReferenceList} element.
     *
     * @return all {@code PubMed} identifiers encoded in the reference
     * list element.
     */
    public List<PMID> parseReferenceList(Element referenceListElement) {
        assertTagName(referenceListElement, "ReferenceList");
        List<PMID> pmids = new ArrayList<PMID>();

        for (Element referenceElement : referenceListElement.getChildren("Reference")) {
            PMID pmid = parseReference(referenceElement);

            if (pmid != null)
                pmids.add(pmid);
        }

        for (Element innerListElement : referenceListElement.getChildren("ReferenceList"))
            pmids.addAll(parseReferenceList(innerListElement));

        return pmids;
    }

    /**
     * Extracts the article version from a {@code PMID} element.
     *
     * @param element a {@code PMID} XML element.
     *
     * @return the article version encoded in the given element.
     */
    public int parseVersion(Element element) {
        assertTagName(element, "PMID");
        return Integer.parseInt(getRequiredAttribute(element, "Version"));
    }
}
