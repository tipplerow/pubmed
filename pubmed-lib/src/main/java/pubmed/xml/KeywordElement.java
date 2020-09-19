
package pubmed.xml;

import java.util.List;

import org.jdom2.Element;

import jam.util.ListUtil;
import jam.xml.JDOMElement;

/**
 * Decorates the {@code Keyword} element in a {@code PubMed} XML
 * document with additional parsing methods.
 */ 
public final class KeywordElement extends JDOMElement {
    private final String keyword;
    private final boolean isMajorTopic;

    private KeywordElement(Element element) {
        super(element, TAG_NAME);

        this.keyword = element.getTextNormalize();
        this.isMajorTopic = getBooleanAttribute("MajorTopicYN", false);
    }

    /**
     * The tag name for the element.
     */
    public static final String TAG_NAME = "Keyword";

    /**
     * Extracts the keyword elements from the parent list.
     *
     * @param parent the parent keyword list.
     *
     * @return the decorated keyword element.
     */
    public static List<KeywordElement> from(KeywordListElement parent) {
        return ListUtil.apply(parent.getChildren(TAG_NAME), child -> new KeywordElement(child));
    }

    /**
     * Returns the keyword text.
     *
     * @return the keyword text.
     */
    public String getKeyword() {
        return keyword;
    }

    /**
     * Identifies major topic keywords.
     *
     * @return {@code true} iff the keyword is a major topic in the
     * keyword list.
     */
    public boolean isMajorTopic() {
        return isMajorTopic;
    }
}
