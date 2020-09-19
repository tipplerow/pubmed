
package pubmed.xml;

import org.jdom2.Element;

/**
 * Decorates {@code DateCompleted} elements in {@code PubMed} XML
 * documents with additional parsing methods.
 */ 
public final class DateCompletedElement extends DateElement {
    private DateCompletedElement(Element element) {
        super(element, TAG_NAME);
    }

    /**
     * The tag name for the element.
     */
    public static final String TAG_NAME = "DateCompleted";

    /**
     * Extracts the decorated element from its citation parent.
     *
     * @param parent the parent citation element.
     *
     * @return the decorated date element.
     */
    public static DateCompletedElement from(MedlineCitationElement parent) {
        Element element = parent.getOptionalChild(TAG_NAME);

        if (element != null)
            return new DateCompletedElement(element);
        else
            return null;
    }
}
