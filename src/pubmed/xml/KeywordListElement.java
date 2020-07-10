
package pubmed.xml;

import java.util.Collections;
import java.util.List;

import org.jdom2.Element;

import jam.util.ListUtil;
import jam.xml.JDOMElement;

/**
 * Decorates the {@code KeywordList} element in a {@code PubMed}
 * XML document with additional parsing methods.
 */ 
public final class KeywordListElement extends JDOMElement {
    private List<KeywordElement> keywords;

    private KeywordListElement(Element element) {
        super(element, TAG_NAME);
    }

    /**
     * The tag name for the element.
     */
    public static final String TAG_NAME = "KeywordList";

    /**
     * Extracts the keyword lists from the parent citation.
     *
     * @param parent the parent citation.
     *
     * @return the decorated keyword lists.
     */
    public static List<KeywordListElement> from(MedlineCitationElement parent) {
        return ListUtil.apply(parent.getChildren(TAG_NAME), child -> new KeywordListElement(child));
    }

    /**
     * Returns the keyword elements in this list.
     *
     * @return the keyword elements in this list.
     */
    public List<KeywordElement> getKeywords() {
        if (keywords == null) {
            keywords = KeywordElement.from(this);
            keywords = Collections.unmodifiableList(keywords);
        }

        return keywords;
    }
}
