
package pubmed.xml;

import org.jdom2.Element;

import jam.xml.JDOMElement;

import pubmed.article.ISSN;
import pubmed.article.PubmedJournal;

/**
 * Decorates the {@code Journal} element in a {@code PubMed} XML
 * document with additional parsing methods.
 */ 
public final class JournalElement extends JDOMElement {
    private final ISSN issn;
    private final String title;
    private final String abbrev;

    private JournalElement(Element element) {
        super(element, TAG_NAME);

        this.issn = parseISSN();
        this.title = getChildText("Title");
        this.abbrev = getChildText("ISOAbbreviation"); 
    }

    private ISSN parseISSN() {
        String issnText = getChildText("ISSN");

        if (issnText != null && ISSN.isValid(issnText))
            return ISSN.instance(issnText);
        else
            return null;
    }

    /**
     * The tag name for the element.
     */
    public static final String TAG_NAME = "Journal";

    /**
     * Extracts the journal element from the parent article.
     *
     * @param parent the parent article.
     *
     * @return the decorated journal element.
     */
    public static JournalElement from(ArticleElement parent) {
        return new JournalElement(parent.getRequiredChild(TAG_NAME));
    }

    /**
     * Returns the ISO abbreviation for the journal title.
     *
     * @return the ISO abbreviation for the journal title.
     */
    public String getISOAbbreviation() {
        return abbrev;
    }

    /**
     * Returns the {@code ISSN} for the journal.
     *
     * @return the {@code ISSN} for the journal.
     */
    public ISSN getISSN() {
        return issn;
    }

    /**
     * Returns the {@code PubmedJournal} record encoded in this
     * element.
     *
     * @return the {@code PubmedJournal} record encoded in this
     * element.
     */
    public PubmedJournal getJournalRecord() {
        return PubmedJournal.create(issn, title, abbrev);
    }

    /**
     * Returns the journal title.
     *
     * @return the journal title.
     */
    public String getTitle() {
        return title;
    }
}
