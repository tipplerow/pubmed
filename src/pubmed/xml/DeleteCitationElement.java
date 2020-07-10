
package pubmed.xml;

import java.util.Collections;
import java.util.List;

import org.jdom2.Element;

import jam.util.ListUtil;
import jam.xml.JDOMElement;

import pubmed.article.PMID;
/**
 * Decorates the {@code DeleteCitation} element in a {@code PubMed}
 * XML document with additional parsing methods.
 */ 
public final class DeleteCitationElement extends JDOMElement {
    private List<PMID> deleted;

    private DeleteCitationElement(Element element) {
        super(element, TAG_NAME);
    }

    /**
     * The tag name for the element.
     */
    public static final String TAG_NAME = "DeleteCitation";

    /**
     * Extracts a new {@code DeleteCitation} element from its parent
     * article.
     *
     * @param parent the parent article.
     *
     * @return the new element ({@code null} if the parent article
     * does not contain a {@code DeleteCitation} element).
     */
    public static DeleteCitationElement from(PubmedArticleSetElement parent) {
        Element child = parent.getOptionalChild(TAG_NAME);

        if (child != null)
            return new DeleteCitationElement(child);
        else
            return null;
    }

    /**
     * Returns a read-only view of the identifiers for the deleted
     * articles.
     *
     * @return an unmodifiable list containing the identifiers for
     * the deleted articles.
     */
    public List<PMID> getDeleted() {
        if (deleted == null) {
            deleted = ListUtil.apply(PMIDElement.from(this), element -> element.getPMID());
            deleted = Collections.unmodifiableList(deleted);
        }

        return deleted;
    }
}
