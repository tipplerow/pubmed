
package pubmed.xml;

import java.util.List;

import org.jdom2.Element;

import jam.util.ListUtil;

/**
 * Decorates {@code PubMedPubDate} elements in {@code PubMed} XML
 * documents with additional parsing methods.
 */ 
public final class PubMedPubDateElement extends DateElement {
    private final String status;

    private PubMedPubDateElement(Element element) {
        super(element, TAG_NAME);
        this.status = getRequiredAttribute("PubStatus");
    }

    /**
     * The tag name for the element.
     */
    public static final String TAG_NAME = "PubMedPubDate";

    /**
     * Extracts the decorated date elements from their parent.
     *
     * @param parent the parent history element.
     *
     * @return the decorated dates elements contained in the parent.
     */
    public static List<PubMedPubDateElement> from(HistoryElement parent) {
        return ListUtil.apply(parent.getChildren(), child -> new PubMedPubDateElement(child));
    }

    /**
     * Returns the publication status code for this date.
     *
     * @return the publication status code for this date.
     */
    public String getStatus() {
        return status;
    }

    /**
     * Identifies elements with {@code pubmed} status codes.
     *
     * @return {@code true} iff this element has the {@code pubmed}
     * status code.
     */
    public boolean isPubmedDate() {
        return status.equals("pubmed");
    }
}
