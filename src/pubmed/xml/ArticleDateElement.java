
package pubmed.xml;

import java.util.List;

import org.jdom2.Element;

import jam.util.ListUtil;

/**
 * Decorates {@code ArticleDate} elements in {@code PubMed} XML
 * documents with additional parsing methods.
 */ 
public final class ArticleDateElement extends DateElement {
    private ArticleDateElement(Element element) {
        super(element, TAG_NAME);
    }

    /**
     * The tag name for the element.
     */
    public static final String TAG_NAME = "ArticleDate";

    /**
     * Extracts the decorated elements from their article parent.
     *
     * @param parent the parent article element.
     *
     * @return the decorated date elements.
     */
    public static List<ArticleDateElement> from(ArticleElement parent) {
        return ListUtil.apply(parent.getChildren(TAG_NAME), child -> new ArticleDateElement(child));
    }
}
