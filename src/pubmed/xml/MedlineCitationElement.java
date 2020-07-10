
package pubmed.xml;

import java.util.Collections;
import java.util.List;

import org.jdom2.Element;

import jam.xml.JDOMElement;

import pubmed.article.PMID;

/**
 * Decorates the {@code MedlineCitation} element in a {@code PubMed}
 * XML document with additional parsing methods.
 */ 
public final class MedlineCitationElement extends JDOMElement {
    private PMIDElement pmidElement;
    private DateElement dateCompletedElement;
    private DateElement dateRevisedElement;
    private ArticleElement articleElement;
    private ChemicalListElement chemicalListElement;
    private MeshHeadingListElement meshHeadingListElement;
    private MedlineJournalInfoElement journalInfoElement;
    private List<KeywordListElement> keywordListElements;
    private CommentsCorrectionsListElement commentsCorrectionsListElement;

    private MedlineCitationElement(Element element) {
        super(element, TAG_NAME);
    }

    /**
     * The tag name for the element.
     */
    public static final String TAG_NAME = "MedlineCitation";

    /**
     * Extracts a new citation element from its parent article.
     *
     * @param parent the parent article.
     *
     * @return the new citation element.
     */
    public static MedlineCitationElement from(PubmedArticleElement parent) {
        return new MedlineCitationElement(parent.getRequiredChild(TAG_NAME));
    }

    /**
     * Returns the {@code Article} element for this citation.
     *
     * @return the {@code Article} element for this citation.
     */
    public ArticleElement getArticleElement() {
        if (articleElement == null)
            articleElement = ArticleElement.from(this);

        return articleElement;
    }

    /**
     * Returns the {@code ChemicalList} element for this citation.
     *
     * @return the {@code ChemicalList} element for this citation.
     */
    public ChemicalListElement getChemicalListElement() {
        if (chemicalListElement == null)
            chemicalListElement = ChemicalListElement.from(this);

        return chemicalListElement;
    }

    /**
     * Returns the {@code CommentsCorrectionsList} element for this citation.
     *
     * @return the {@code CommentsCorrectionsList} element for this citation.
     */
    public CommentsCorrectionsListElement getCommentsCorrectionsListElement() {
        if (commentsCorrectionsListElement == null)
            commentsCorrectionsListElement = CommentsCorrectionsListElement.from(this);

        return commentsCorrectionsListElement;
    }

    /**
     * Returns the {@code DateCompleted} element for this citation.
     *
     * @return the {@code DateCompleted} element for this citation
     * (may be {@code null} if missing from the original source).
     */
    public DateElement getDateCompletedElement() {
        if (dateCompletedElement == null)
            dateCompletedElement = DateCompletedElement.from(this);

        return dateCompletedElement;
    }

    /**
     * Returns the {@code DateRevised} element for this citation.
     *
     * @return the {@code DateRevised} element for this citation
     * (may be {@code null} if missing from the original source).
     */
    public DateElement getDateRevisedElement() {
        if (dateRevisedElement == null)
            dateRevisedElement = DateRevisedElement.from(this);

        return dateRevisedElement;
    }

    /**
     * Returns the {@code KeywordList} elements for this citation.
     *
     * @return the {@code KeywordList} elements for this citation.
     */
    public List<KeywordListElement> getKeywordListElements() {
        if (keywordListElements == null) {
            keywordListElements = KeywordListElement.from(this);
            keywordListElements = Collections.unmodifiableList(keywordListElements);
        }

        return keywordListElements;
    }

    /**
     * Returns the {@code MedlineJournalInfo} element for this
     * citation.
     *
     * @return the {@code MedlineJournalInfo} element for this
     * citation.
     */
    public MedlineJournalInfoElement getMedlineJournalInfoElement() {
        if (journalInfoElement == null)
            journalInfoElement = MedlineJournalInfoElement.from(this);

        return journalInfoElement;
    }

    /**
     * Returns the {@code MeshHeadingList} element for this citation.
     *
     * @return the {@code MeshHeadingList} element for this citation.
     */
    public MeshHeadingListElement getMeshHeadingListElement() {
        if (meshHeadingListElement == null)
            meshHeadingListElement = MeshHeadingListElement.from(this);

        return meshHeadingListElement;
    }

    /**
     * Returns the {@code PubMed} identifier for the article.
     *
     * @return the {@code PubMed} identifier for the article.
     */
    public PMID getPMID() {
        return getPMIDElement().getPMID();
    }

    /**
     * Returns the {@code PMID} element for the article. 
     *
     * @return the {@code PMID} element for the article. 
     */
    public PMIDElement getPMIDElement() {
        if (pmidElement == null)
            pmidElement = PMIDElement.from(this);

        return pmidElement;
    }
}
