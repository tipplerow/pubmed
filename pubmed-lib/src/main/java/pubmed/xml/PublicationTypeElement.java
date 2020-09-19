
package pubmed.xml;

import java.util.List;

import org.jdom2.Element;

import jam.util.ListUtil;
import jam.xml.JDOMElement;

import pubmed.article.PublicationType;

/**
 * Decorates the {@code PublicationType} element in a {@code PubMed} XML
 * document with additional parsing methods.
 */ 
public final class PublicationTypeElement extends JDOMElement {
    private final PublicationType type;

    private PublicationTypeElement(Element element) {
        super(element, TAG_NAME);
        this.type = PublicationType.create(getRequiredAttribute("UI"));
    }

    /**
     * The tag name for the element.
     */
    public static final String TAG_NAME = "PublicationType";

    /**
     * Extracts the {@code PublicationType} elements from their parent
     * list.
     *
     * @param parent the parent list.
     *
     * @return the decorated {@code PublicationType} elements.
     */
    public static List<PublicationTypeElement> from(PublicationTypeListElement parent) {
        return ListUtil.apply(parent.getChildren(), child -> new PublicationTypeElement(child));
    }

    /**
     * Returns the publication type encoded in this element.
     *
     * @return the publication type encoded in this element.
     */
    public PublicationType getType() {
        return type;
    }
}
