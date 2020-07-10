
package pubmed.xml;

import org.jdom2.Element;

import jam.xml.JDOMElement;

import pubmed.article.DOI;

/**
 * Decorates the {@code PubmedData} element in a {@code PubMed}
 * XML document with additional parsing methods.
 */ 
public final class PubmedDataElement extends JDOMElement {
    private HistoryElement historyElement;
    private ArticleIdListElement articleIdListElement;

    private PubmedDataElement(Element element) {
        super(element, TAG_NAME);
    }

    /**
     * The tag name for the element.
     */
    public static final String TAG_NAME = "PubmedData";

    /**
     * Extracts a new citation element from its parent article.
     *
     * @param parent the parent article.
     *
     * @return the new citation element.
     */
    public static PubmedDataElement from(PubmedArticleElement parent) {
        Element child = parent.getOptionalChild(TAG_NAME);

        if (child != null)
            return new PubmedDataElement(child);
        else
            return null;
    }

    /**
     * Returns the article identifier list element.
     *
     * @return the article identifier list element.
     */
    public ArticleIdListElement getArticleIdListElement() {
        if (articleIdListElement == null)
            articleIdListElement = ArticleIdListElement.from(this);

        return articleIdListElement;
    }

    /**
     * Extracts the Digital Object Identifier from this element.
     *
     * @return the Digital Object Identifier encoded in this element
     * (or {@code null} if there is no {@code DOI} in the identifier
     * list).
     */
    public DOI getDOI() {
        return getArticleIdListElement().getDOI();
    }

    /**
     * Returns the history element.
     *
     * @return the history element.
     */
    public HistoryElement getHistoryElement() {
        if (historyElement == null)
            historyElement = HistoryElement.from(this);

        return historyElement;
    }
}
