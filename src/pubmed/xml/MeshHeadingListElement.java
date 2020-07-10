
package pubmed.xml;

import java.util.Collections;
import java.util.List;

import org.jdom2.Element;

import jam.xml.JDOMElement;

/**
 * Decorates the {@code MeshHeadingList} element in a {@code PubMed}
 * XML document with additional parsing methods.
 */ 
public final class MeshHeadingListElement extends JDOMElement {
    private List<MeshHeadingElement> headings;

    private MeshHeadingListElement(Element element) {
        super(element, TAG_NAME);
    }

    /**
     * The tag name for the element.
     */
    public static final String TAG_NAME = "MeshHeadingList";

    /**
     * Extracts the {@code MeSH} heading list element from the parent
     * citation.
     *
     * @param parent the parent citation.
     *
     * @return the decorated heading list element.
     */
    public static MeshHeadingListElement from(MedlineCitationElement parent) {
        Element child = parent.getOptionalChild(TAG_NAME);

        if (child != null)
            return new MeshHeadingListElement(child);
        else
            return null;
    }

    /**
     * Returns the heading elements in this list.
     *
     * @return the heading elements in this list.
     */
    public List<MeshHeadingElement> getMeshHeadings() {
        if (headings == null) {
            headings = MeshHeadingElement.from(this);
            headings = Collections.unmodifiableList(headings);
        }

        return headings;
    }
}
