
package pubmed.xml;

import java.time.LocalDate;

import org.jdom2.Element;

import jam.xml.JDOMElement;

/**
 * Provides a base class for date elements in {@code PubMed} XML
 * documents.
 */ 
public abstract class DateElement extends JDOMElement {
    private final LocalDate date;

    /**
     * Creates a new date element from the original source.
     *
     * @param element the original source element.
     *
     * @param tagName the expected tag name of the element.
     */
    protected DateElement(Element element, String tagName) {
        super(element, tagName);
        this.date = parseDate();
    }

    private LocalDate parseDate() {
        int year  = Integer.parseInt(getRequiredChild("Year").getTextNormalize());
        int month = Integer.parseInt(getRequiredChild("Month").getTextNormalize());
        int day   = Integer.parseInt(getRequiredChild("Day").getTextNormalize());

        return LocalDate.of(year, month, day);
    }

    /**
     * Returns the parsed date.
     *
     * @return the parsed date.
     */
    public LocalDate getDate() {
        return date;
    }
}
