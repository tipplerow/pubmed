
package pubmed.xml;

import org.jdom2.Element;
import jam.xml.JDOMElement;

/**
 * Decorates the {@code ArticleTitle} element in a {@code PubMed}
 * XML document with additional parsing methods.
 */ 
public final class ArticleTitleElement extends JDOMElement {
    private final String text;

    private ArticleTitleElement(Element element) {
        super(element, TAG_NAME);
        this.text = element.getTextNormalize();
    }

    /**
     * The tag name for the element.
     */
    public static final String TAG_NAME = "ArticleTitle";

    /**
     * Extracts the title element from its parent article.
     *
     * @param parent the parent article.
     *
     * @return the decorated title element (or {@code null} if the
     * parent does not contain an title element).
     */
    public static ArticleTitleElement from(ArticleElement parent) {
        return new ArticleTitleElement(parent.getRequiredChild(TAG_NAME));
    }

    /**
     * Returns the title text.
     *
     * @return the title text.
     */
    public String getText() {
        return text;
    }
}
