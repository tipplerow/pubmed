
package pubmed.xml;

import java.time.LocalDate;

import org.jdom2.Element;

import jam.app.JamLogger;
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

        try {
            return LocalDate.of(year, month, day);
        }
        catch (RuntimeException ex) {
            JamLogger.warn("Invalid date: [%02d-%02d-%02d]", year, month, day);
            return approximateDate(year, month);
        }
    }

    private static LocalDate approximateDate(int year, int month) {
        if (month < 1 || month > 12)
            return LocalDate.of(year, 1, 1);
        else
            return LocalDate.of(year, month, 1);
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
