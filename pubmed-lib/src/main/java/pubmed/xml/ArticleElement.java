
package pubmed.xml;

import java.util.Collections;
import java.util.List;

import org.jdom2.Element;

import jam.xml.JDOMElement;

/**
 * Decorates the {@code Article} element in a {@code PubMed} XML
 * document with additional parsing methods.
 */ 
public final class ArticleElement extends JDOMElement {
    private JournalElement journalElement;
    private AbstractElement abstractElement;
    private ArticleTitleElement articleTitleElement;
    private List<ArticleDateElement> articleDateElements;
    private PublicationTypeListElement publicationTypeListElement;

    private ArticleElement(Element element) {
        super(element, TAG_NAME);
    }

    /**
     * The tag name for the element.
     */
    public static final String TAG_NAME = "Article";

    /**
     * Extracts a new article element from its parent citation.
     *
     * @param parent the parent citation element.
     *
     * @return the new article element.
     */
    public static ArticleElement from(MedlineCitationElement parent) {
        return new ArticleElement(parent.getRequiredChild(TAG_NAME));
    }

    /**
     * Returns the article abstract element.
     *
     * @return the article abstract element.
     */
    public AbstractElement getAbstractElement() {
        if (abstractElement == null)
            abstractElement = AbstractElement.from(this);

        return abstractElement;
    }

    /**
     * Returns the article date elements for the article.
     *
     * @return the article date elements for the article.
     */
    public List<ArticleDateElement> getArticleDateElements() {
        if (articleDateElements == null) {
            articleDateElements = ArticleDateElement.from(this);
            articleDateElements = Collections.unmodifiableList(articleDateElements);
        }

        return articleDateElements;
    }

    /**
     * Returns the article title element.
     *
     * @return the article title element.
     */
    public ArticleTitleElement getArticleTitleElement() {
        if (articleTitleElement == null)
            articleTitleElement = ArticleTitleElement.from(this);

        return articleTitleElement;
    }

    /**
     * Returns the journal element for the article.
     *
     * @return the journal element for the article.
     */
    public JournalElement getJournalElement() {
        if (journalElement == null)
            journalElement = JournalElement.from(this);

        return journalElement;
    }

    /**
     * Returns the publication types for the article.
     *
     * @return the publication types for the article.
     */
    public PublicationTypeListElement getPublicationTypeListElement() {
        if (publicationTypeListElement == null)
            publicationTypeListElement = PublicationTypeListElement.from(this);

        return publicationTypeListElement;
    }
}
