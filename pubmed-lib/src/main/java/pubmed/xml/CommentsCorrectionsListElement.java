
package pubmed.xml;

import java.util.Collections;
import java.util.List;

import org.jdom2.Element;

import jam.xml.JDOMElement;

/**
 * Decorates the {@code CommentsCorrectionsList} element in a {@code PubMed}
 * XML document with additional parsing methods.
 */ 
public final class CommentsCorrectionsListElement extends JDOMElement {
    private List<CommentsCorrectionsElement> ccElements;

    private CommentsCorrectionsListElement(Element element) {
        super(element, TAG_NAME);
    }

    /**
     * The tag name for the element.
     */
    public static final String TAG_NAME = "CommentsCorrectionsList";

    /**
     * Extracts the comments/corrections list element from the parent
     * citation.
     *
     * @param parent the parent citation.
     *
     * @return the decorated comments/corrections list element.
     */
    public static CommentsCorrectionsListElement from(MedlineCitationElement parent) {
        Element child = parent.getOptionalChild(TAG_NAME);

        if (child != null)
            return new CommentsCorrectionsListElement(child);
        else
            return null;
    }

    /**
     * Returns the chemical elements in this list.
     *
     * @return the chemical elements in this list.
     */
    public List<CommentsCorrectionsElement> getCommentsCorrections() {
        if (ccElements == null) {
            ccElements = CommentsCorrectionsElement.from(this);
            ccElements = Collections.unmodifiableList(ccElements);
        }

        return ccElements;
    }
}
