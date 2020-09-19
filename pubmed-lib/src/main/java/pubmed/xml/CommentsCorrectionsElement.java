
package pubmed.xml;

import java.util.List;

import org.jdom2.Element;

import jam.util.ListUtil;
import jam.xml.JDOMElement;

/**
 * Decorates the {@code CommentsCorrections} element in a {@code PubMed}
 * XML document with additional parsing methods.
 */ 
public final class CommentsCorrectionsElement extends JDOMElement {
    private final String refType;
    private final PMIDElement pmidElement;

    private CommentsCorrectionsElement(Element element) {
        super(element, TAG_NAME);

        this.refType = getRequiredAttribute("RefType");
        this.pmidElement = PMIDElement.from(this);
    }

    /**
     * The tag name for the element.
     */
    public static final String TAG_NAME = "CommentsCorrections";

    /**
     * Extracts the chemical element from the parent list.
     *
     * @param parent the parent chemical list.
     *
     * @return the decorated chemical element.
     */
    public static List<CommentsCorrectionsElement> from(CommentsCorrectionsListElement parent) {
        return ListUtil.apply(parent.getChildren(TAG_NAME), child -> new CommentsCorrectionsElement(child));
    }

    /**
     * Returns the reference type for this element.
     *
     * @return the reference type for this element.
     */
    public String getRefType() {
        return refType;
    }

    /**
     * Returns the {@code PMID} referenced by this element.
     *
     * @return the {@code PMID} referenced by this element.
     */
    public PMIDElement getPMIDElement() {
        return pmidElement;
    }
}
