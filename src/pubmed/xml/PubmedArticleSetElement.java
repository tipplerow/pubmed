
package pubmed.xml;

import java.io.File;
import java.util.Collections;
import java.util.List;

import org.jdom2.Element;

import jam.xml.JDOMDocument;
import jam.xml.JDOMElement;

import pubmed.article.PMID;

/**
 * Decorates the root {@code PubmedArticleSet} element in a {@code PubMed}
 * XML document with additional parsing methods.
 */ 
public final class PubmedArticleSetElement extends JDOMElement {
    private final DeleteCitationElement deleteCitationElement;
    private final List<PubmedArticleElement> pubmedArticleElements;

    private PubmedArticleSetElement(Element element) {
        super(element, TAG_NAME);

        this.pubmedArticleElements = PubmedArticleElement.from(this);
        this.deleteCitationElement = DeleteCitationElement.from(this);
    }

    /**
     * The tag name for the element.
     */
    public static final String TAG_NAME = "PubmedArticleSet";

    /**
     * Creates a new element by parsing an XML file.
     *
     * @param xmlFile the XML file to parse.
     *
     * @return the decorated article set element (the root element in
     * the XML file).
     */
    public static PubmedArticleSetElement from(File xmlFile) {
        return from(JDOMDocument.parse(xmlFile));
    }

    /**
     * Creates a new element from the parsed XML document.
     *
     * @param document the parsed XML document.
     *
     * @return the decorated article set element.
     */
    public static PubmedArticleSetElement from(JDOMDocument document) {
        return new PubmedArticleSetElement(document.getRootElement());
    }

    /**
     * Returns the identifiers of articles that have been marked as
     * deleted in the XML document.
     *
     * @return the identifiers of articles that have been marked as
     * deleted in the XML document.
     */
    public List<PMID> getDeleted() {
        if (deleteCitationElement != null)
            return deleteCitationElement.getDeleted();
        else
            return List.of();
    }

    /**
     * Returns the single {@code DeleteCitation} element contained in
     * the document.
     *
     * @return the single {@code DeleteCitation} element contained in
     * the document ({@code null} if there is no such element).
     */
    public DeleteCitationElement getDeleteCitationElement() {
        return deleteCitationElement;
    }

    /**
     * Returns a read-only view of the {@code PubmedArticle} elements
     * contained in the document.
     *
     * @return a read-only view of the {@code PubmedArticle} elements
     * contained in the document.
     */
    public List<PubmedArticleElement> getPubmedArticleElements() {
        return Collections.unmodifiableList(pubmedArticleElements);
    }
}
