
package pubmed.xml;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import org.jdom2.Element;

import jam.xml.JDOMElement;

/**
 * Decorates the {@code History} element in a {@code PubMed}
 * XML document with additional parsing methods.
 */ 
public final class HistoryElement extends JDOMElement {
    private List<PubMedPubDateElement> pubDates;

    private HistoryElement(Element element) {
        super(element, TAG_NAME);
    }

    /**
     * The tag name for the element.
     */
    public static final String TAG_NAME = "History";

    /**
     * Extracts the history element from its parent.
     *
     * @param parent the parent element.
     *
     * @return the decorated history element.
     */
    public static HistoryElement from(PubmedDataElement parent) {
        Element child = parent.getOptionalChild(TAG_NAME);

        if (child != null)
            return new HistoryElement(child);
        else
            return null;
    }

    /**
     * Returns the date in which the article appeared in the
     * {@code pubmed} database.
     *
     * @return the date in which the article appeared in the
     * {@code pubmed} database (possibly {@code null} if the
     * article does not contain a matching element).
     */
    public LocalDate getPubmedDate() {
        for (PubMedPubDateElement element : getPubMedPubDateElements())
            if (element.isPubmedDate())
                return element.getDate();

        return null;
    }

    /**
     * Returns the publication dates in this history element.
     *
     * @return the publication dates in this history element.
     */
    public List<PubMedPubDateElement> getPubMedPubDateElements() {
        if (pubDates == null) {
            pubDates = PubMedPubDateElement.from(this);
            pubDates = Collections.unmodifiableList(pubDates);
        }

        return pubDates;
    }
}
