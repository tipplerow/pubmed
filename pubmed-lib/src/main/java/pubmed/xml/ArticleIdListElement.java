
package pubmed.xml;

import java.util.Collections;
import java.util.List;

import org.jdom2.Element;

import jam.xml.JDOMElement;

import pubmed.article.DOI;

/**
 * Decorates the {@code ArticleIdList} element in a {@code PubMed}
 * XML document with additional parsing methods.
 */ 
public final class ArticleIdListElement extends JDOMElement {
    private List<ArticleIdElement> idElements;

    private ArticleIdListElement(Element element) {
        super(element, TAG_NAME);
    }

    /**
     * The tag name for the element.
     */
    public static final String TAG_NAME = "ArticleIdList";

    /**
     * Extracts the article ID list from its parent.
     *
     * @param parent the parent element.
     *
     * @return the decorated article ID list.
     */
    public static ArticleIdListElement from(PubmedDataElement parent) {
        return new ArticleIdListElement(parent.getRequiredChild(TAG_NAME));
    }

    /**
     * Returns the elements contained in this list.
     *
     * @return the elements contained in this list.
     */
    public List<ArticleIdElement> getArticleIdElements() {
        if (idElements == null) {
            idElements = ArticleIdElement.from(this);
            idElements = Collections.unmodifiableList(idElements);
        }

        return idElements;
    }

    /**
     * Extracts the Digital Object Identifier from this list.
     *
     * @return the Digital Object Identifier encoded in this list
     * ({@code null} if there is no {@code DOI} element).
     */
    public DOI getDOI() {
        return ArticleIdElement.getDOI(getArticleIdElements());
    }
}
