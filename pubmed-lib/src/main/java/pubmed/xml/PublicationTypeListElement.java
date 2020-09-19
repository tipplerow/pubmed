
package pubmed.xml;

import java.util.Collections;
import java.util.List;

import org.jdom2.Element;

import jam.xml.JDOMElement;

/**
 * Decorates the {@code PublicationTypeList} element in a {@code PubMed}
 * XML document with additional parsing methods.
 */ 
public final class PublicationTypeListElement extends JDOMElement {
    private List<PublicationTypeElement> typeElements;

    private PublicationTypeListElement(Element element) {
        super(element, TAG_NAME);
    }

    /**
     * The tag name for the element.
     */
    public static final String TAG_NAME = "PublicationTypeList";

    /**
     * Extracts the publication type list from its parent article.
     *
     * @param parent the parent article.
     *
     * @return the decorated element.
     */
    public static PublicationTypeListElement from(ArticleElement parent) {
        return new PublicationTypeListElement(parent.getRequiredChild(TAG_NAME));
    }

    /**
     * Returns the publication types encoded in this list.
     *
     * @return the publication types encoded in this list.
     */
    public List<PublicationTypeElement> getPublicationTypeElements() {
        if (typeElements == null) {
            typeElements = PublicationTypeElement.from(this);
            typeElements = Collections.unmodifiableList(typeElements);
        }

        return typeElements;
    }
}
