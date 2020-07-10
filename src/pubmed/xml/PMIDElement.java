
package pubmed.xml;

import java.util.List;

import org.jdom2.Element;

import jam.util.ListUtil;
import jam.xml.JDOMElement;

import pubmed.article.PMID;

/**
 * Decorates the {@code PMID} element in a {@code PubMed} XML
 * document with additional parsing methods.
 */ 
public final class PMIDElement extends JDOMElement {
    private final PMID pmid;

    private PMIDElement(Element element) {
        super(element, TAG_NAME);
        this.pmid = PMID.instance(element.getTextNormalize());
    }

    /**
     * The tag name for the element.
     */
    public static final String TAG_NAME = "PMID";

    /**
     * Extracts the {@code PMID} elements from a {@code DeleteCitation} element.
     *
     * @param parent the {@code DeleteCitation} element.
     *
     * @return the {@code PMID} elements contained in the parent.
     */
    public static List<PMIDElement> from(DeleteCitationElement parent) {
        return ListUtil.apply(parent.getChildren(), child -> new PMIDElement(child));
    }

    /**
     * Extracts the {@code PMID} element from the parent article.
     *
     * @param parent the parent article.
     *
     * @return the decorated {@code PMID} element.
     */
    public static PMIDElement from(MedlineCitationElement parent) {
        return new PMIDElement(parent.getRequiredChild(TAG_NAME));
    }

    /**
     * Extracts the {@code PMID} element from a comment or correction.
     *
     * @param parent the parent comment/correction element.
     *
     * @return the decorated {@code PMID} element.
     */
    public static PMIDElement from(CommentsCorrectionsElement parent) {
        Element child = parent.getOptionalChild(TAG_NAME);

        if (child != null)
            return new PMIDElement(child);
        else
            return null;
    }

    /**
     * Returns the identifier encoded in this element.
     *
     * @return the identifier encoded in this element.
     */
    public PMID getPMID() {
        return pmid;
    }
}
