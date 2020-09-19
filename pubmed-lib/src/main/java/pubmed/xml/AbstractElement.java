
package pubmed.xml;

import java.util.List;

import org.jdom2.Element;

import jam.report.LineBuilder;
import jam.xml.JDOMElement;

/**
 * Decorates the {@code Abstract} element in a {@code PubMed} XML
 * document with additional parsing methods.
 */ 
public final class AbstractElement extends JDOMElement {
    private final String text;

    private AbstractElement(Element element) {
        super(element, TAG_NAME);
        this.text = parseText();
    }

    private String parseText() {
        LineBuilder builder = new LineBuilder(" ");
        List<Element> children = element.getChildren("AbstractText");

        for (Element child : children)
            builder.append(child.getTextNormalize());

        return builder.toString();
    }

    /**
     * The tag name for the element.
     */
    public static final String TAG_NAME = "Abstract";

    /**
     * Extracts the abstract element from its parent article.
     *
     * @param parent the parent article.
     *
     * @return the decorated abstract element (or {@code null} if the
     * parent does not contain an abstract element).
     */
    public static AbstractElement from(ArticleElement parent) {
        Element element = parent.getOptionalChild(TAG_NAME);

        if (element != null)
            return new AbstractElement(element);
        else
            return null;
    }

    /**
     * Returns the abstract text.
     *
     * @return the abstract text.
     */
    public String getText() {
        return text;
    }
}
