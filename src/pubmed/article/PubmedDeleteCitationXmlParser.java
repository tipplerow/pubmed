
package pubmed.article;

import java.util.ArrayList;
import java.util.List;

import org.jdom2.Element;

/**
 * Extracts the unique identifiers of deleted articles from their
 * {@code DeleteCitation} XML element.
 */
public final class PubmedDeleteCitationXmlParser extends PubmedXmlParser {
    private final Element deleteCitationElement;

    private PubmedDeleteCitationXmlParser(Element deleteCitationElement) {
        this.deleteCitationElement = deleteCitationElement;
    }

    /**
     * Extracts the unique identifiers of deleted articles from
     * their {@code DeleteCitation} XML element.
     *
     * @param deleteCitationElement an XML element encoding a
     * {@code DeleteCitation} identifier list.
     *
     * @return the identifiers encoded in the element.
     *
     * @throws RuntimeException if any parsing errors occur.
     */
    public static List<PMID> parse(Element deleteCitationElement) {
        PubmedDeleteCitationXmlParser parser =
            new PubmedDeleteCitationXmlParser(deleteCitationElement);

        return parser.parse();
    }

    private List<PMID> parse() {
        assertTagName(deleteCitationElement, "DeleteCitation");

        List<Element> deleteCitationChildren =
            deleteCitationElement.getChildren();

        List<PMID> pmids =
            new ArrayList<PMID>(deleteCitationChildren.size());

        for (Element pmidElement : deleteCitationChildren)
            pmids.add(parsePMID(pmidElement));

        return pmids;
    }
}
