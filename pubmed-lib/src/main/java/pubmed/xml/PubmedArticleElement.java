
package pubmed.xml;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import org.jdom2.Element;

import jam.util.ListUtil;
import jam.xml.JDOMElement;

import pubmed.article.CommentCorrection;
import pubmed.article.CommentCorrectionType;
import pubmed.article.DOI;
import pubmed.article.PMID;
import pubmed.article.PublicationType;
import pubmed.article.PubmedJournal;
import pubmed.mesh.MeshHeading;
import pubmed.mesh.MeshRecordKey;

/**
 * Decorates the {@code PubmedArticle} element in a {@code PubMed} XML
 * document with additional parsing methods.
 */ 
public final class PubmedArticleElement extends JDOMElement {
    private PubmedDataElement pubmedDataElement;
    private MedlineCitationElement medlineCitationElement;

    private PubmedArticleElement(Element element) {
        super(element, TAG_NAME);
    }

    private static LocalDate getDate(DateElement element) {
        if (element != null)
            return element.getDate();
        else
            return null;
    }

    private AbstractElement getAbstractElement() {
        return getArticleElement().getAbstractElement();
    }

    private ArticleElement getArticleElement() {
        return getMedlineCitationElement().getArticleElement();
    }

    private ArticleTitleElement getArticleTitleElement() {
        return getArticleElement().getArticleTitleElement();
    }

    private HistoryElement getHistoryElement() {
        return getPubmedDataElement().getHistoryElement();
    }

    private JournalElement getJournalElement() {
        return getArticleElement().getJournalElement();
    }

    private MedlineCitationElement getMedlineCitationElement() {
        if (medlineCitationElement == null)
            medlineCitationElement = MedlineCitationElement.from(this);

        return medlineCitationElement;
    }

    private PublicationTypeListElement getPublicationTypeListElement() {
        return getArticleElement().getPublicationTypeListElement();
    }

    private PubmedDataElement getPubmedDataElement() {
        if (pubmedDataElement == null)
            pubmedDataElement = PubmedDataElement.from(this);

        return pubmedDataElement;
    }

    /**
     * The tag name for the element.
     */
    public static final String TAG_NAME = "PubmedArticle";

    /**
     * Extracts the {@code PubmedArticle} elements from the parent
     * set.
     *
     * @param parent the parent element set.
     *
     * @return the {@code PubmedArticle} elements contained in the
     * parent set.
     */
    public static List<PubmedArticleElement> from(PubmedArticleSetElement parent) {
        //
        // At least one bulk XML file contains duplicate PMIDs (with
        // different version numbers).  We add article elements to a
        // LinkedHashMap, which will eliminate the duplicate records
        // while maintaining the order in the bulk file...
        //
        Map<PMID, PubmedArticleElement> articles =
            new LinkedHashMap<PMID, PubmedArticleElement>();

        for (Element child : parent.getChildren(TAG_NAME)) {
            PubmedArticleElement article = new PubmedArticleElement(child);
            articles.put(article.getPMID(), article);
        }

        return new ArrayList<PubmedArticleElement>(articles.values());
    }

    /**
     * Returns the abstract for the article.
     *
     * @return the abstract for the article ({@code null} if there is
     * no abstract).
     */
    public String getAbstract() {
        AbstractElement element = getAbstractElement();

        if (element != null)
            return element.getText();
        else
            return null;
    }

    /**
     * Returns the article dates (electronic publishing dates) for the
     * article.
     *
     * @return the article dates (electronic publishing dates) for the
     * article (empty for articles that did not appear electronically).
     */
    public List<LocalDate> getArticleDates() {
        return ListUtil.apply(getArticleElement().getArticleDateElements(), date -> date.getDate());
    }

    /**
     * Returns the title of the article.
     *
     * @return the title of the article.
     */
    public String getArticleTitle() {
        return getArticleTitleElement().getText();
    }

    /**
     * Returns the completion date for the article.
     *
     * @return the completion date for the article (may be
     * {@code null} if none is specified in the document).
     */
    public LocalDate getDateCompleted() {
        return getDate(getMedlineCitationElement().getDateCompletedElement());
    }

    /**
     * Returns the {@code MeSH} record keys for the chemical
     * substances of interest in the article.
     *
     * @return the {@code MeSH} record keys for the chemical
     * substances of interest in the article.
     */
    public List<MeshRecordKey> getChemicalList() {
        ChemicalListElement chemicalListElement =
            getMedlineCitationElement().getChemicalListElement();

        if (chemicalListElement != null)
            return ListUtil.apply(chemicalListElement.getChemicals(),
                                  chemical -> chemical.getRecordKey());
        else
            return List.of();
    }

    /**
     * Returns the revision date for the article.
     *
     * @return the revision date for the article (may be
     * {@code null} if none is specified in the document).
     */
    public LocalDate getDateRevised() {
        return getDate(getMedlineCitationElement().getDateRevisedElement());
    }

    /**
     * Returns the Digital Object Identifier for the article.
     *
     * @return the Digital Object Identifier for the article (may be
     * {@code null} if none is present).
     */
    public DOI getDOI() {
        return getPubmedDataElement().getDOI();
    }

    /**
     * Returns the keywords for the article.
     *
     * @return the keywords for the article.
     */
    public List<String> getKeywordList() {
        List<KeywordListElement> keywordListElements =
            getMedlineCitationElement().getKeywordListElements();

        if (keywordListElements.isEmpty())
            return List.of();

        List<String> keywords = new ArrayList<String>();

        for (KeywordListElement keywordListElement : keywordListElements)
            for (KeywordElement keywordElement : keywordListElement.getKeywords())
                keywords.add(keywordElement.getKeyword());

        return keywords;
    }

    /**
     * Returns the {@code MeSH} heading records in the article.
     *
     * @return the {@code MeSH} heading records in the article.
     */
    public List<MeshHeading> getMeshHeadingList() {
        MeshHeadingListElement headingListElement =
            getMedlineCitationElement().getMeshHeadingListElement();

        if (headingListElement != null)
            return ListUtil.apply(headingListElement.getMeshHeadings(),
                                  heading -> heading.getMeshHeading());
        else
            return List.of();
    }

    /**
     * Returns the {@code PubMed} identifier for the article.
     *
     * @return the {@code PubMed} identifier for the article.
     */
    public PMID getPMID() {
        return getMedlineCitationElement().getPMID();
    }

    /**
     * Returns the publication types for the article.
     *
     * @return the publication types for the article.
     */
    public List<PublicationType> getPublicationTypes() {
        List<PublicationTypeElement> elements =
            getPublicationTypeListElement().getPublicationTypeElements();

        return ListUtil.notNull(ListUtil.apply(elements, element -> element.getType()));
    }

    /**
     * Returns the date in which the article appeared in the
     * {@code pubmed} database.
     *
     * @return the date in which the article appeared in the
     * {@code pubmed} database (possibly {@code null} if the
     * article does not contain a matching date element).
     */
    public LocalDate getPubmedDate() {
        return getHistoryElement().getPubmedDate();
    }

    /**
     * Returns the journal record for the article.
     *
     * @return the journal record for the article.
     */
    public PubmedJournal getPubmedJournal() {
        return getJournalElement().getJournalRecord();
    }

    /**
     * Finds the best date to assign as the publication date of the
     * article.
     *
     * @return the earliest of the publication date references in the
     * XML document ({@code null} if there are none).
     */
    public LocalDate resolvePublicationDate() {
        TreeSet<LocalDate> dates = new TreeSet<LocalDate>();

        LocalDate pubmedDate    = getPubmedDate();
        LocalDate dateCompleted = getDateCompleted();
        LocalDate dateRevised   = getDateRevised();

        if (pubmedDate != null)
            dates.add(pubmedDate);

        if (dateCompleted != null)
            dates.add(dateCompleted);

        if (dateRevised != null)
            dates.add(dateRevised);

        for (LocalDate date : getArticleDates())
            if (date != null)
                dates.add(date);

        if (dates.isEmpty())
            return null;
        else
            return dates.first();
    }
}
