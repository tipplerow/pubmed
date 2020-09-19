
package pubmed.xml;

import org.jdom2.Element;

/**
 * Decorates {@code DateRevised} elements in {@code PubMed} XML
 * documents with additional parsing methods.
 */ 
public final class DateRevisedElement extends DateElement {
    private DateRevisedElement(Element element) {
        super(element, TAG_NAME);
    }

    /**
     * The tag name for the element.
     */
    public static final String TAG_NAME = "DateRevised";

    /**
     * Extracts the decorated element from its citation parent.
     *
     * @param parent the parent citation element.
     *
     * @return the decorated date element.
     */
    public static DateRevisedElement from(MedlineCitationElement parent) {
        Element element = parent.getOptionalChild(TAG_NAME);

        if (element != null)
            return new DateRevisedElement(element);
        else
            return null;
    }
}
