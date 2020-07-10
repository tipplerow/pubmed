
package pubmed.xml;

import java.util.List;

import org.jdom2.Element;

import jam.util.ListUtil;
import jam.xml.JDOMElement;

import pubmed.article.DOI;

/**
 * Decorates the {@code ArticleId} element in a {@code PubMed} XML
 * document with additional parsing methods.
 */ 
public final class ArticleIdElement extends JDOMElement {
    private final String idType;
    private final String idString;

    private ArticleIdElement(Element element) {
        super(element, TAG_NAME);

        this.idType = getRequiredAttribute("IdType");
        this.idString = element.getTextNormalize();
    }

    /**
     * The tag name for the element.
     */
    public static final String TAG_NAME = "ArticleId";

    /**
     * Type code for Digital Object Identifiers.
     */
    public static final String DOI_TYPE = "doi";

    /**
     * Extracts the {@code ArticleId} elements from their parent list.
     *
     * @param parent the parent list.
     *
     * @return the decorated {@code ArticleId} elements.
     */
    public static List<ArticleIdElement> from(ArticleIdListElement parent) {
        return ListUtil.apply(parent.getChildren(), child -> new ArticleIdElement(child));
    }

    /**
     * Extracts the Digital Object Identifier from an identifier list.
     *
     * @param elements the {@code ArticleId} elements.
     *
     * @return the Digital Object Identifier encoded in the input list
     * ({@code null} if there is no {@code DOI} element in the list).
     */
    public static DOI getDOI(List<ArticleIdElement> elements) {
        for (ArticleIdElement element : elements)
            if (element.isDOI())
                return DOI.instance(element.idString);

        return null;
    }

    /**
     * Returns the article identifier string.
     *
     * @return the article identifier string.
     */
    public String getIDString() {
        return idString;
    }

    /**
     * Returns the article identifier type.
     *
     * @return the article identifier type.
     */
    public String getIDType() {
        return idType;
    }

    /**
     * Identifies Digital Object Identifier elements.
     *
     * @return {@code true} iff this element contains a Digital Object
     * Identifier (DOI).
     */
    public boolean isDOI() {
        return idType.equals(DOI_TYPE);
    }
}
