
package pubmed.xml;

import org.jdom2.Element;

import jam.xml.JDOMElement;

import pubmed.article.ISSN;
import pubmed.medline.MedlineTA;
import pubmed.medline.NlmUniqueID;

/**
 * Decorates the {@code MedlineJournalInfo} element in a {@code PubMed}
 * XML document with additional parsing methods.
 */ 
public final class MedlineJournalInfoElement extends JDOMElement {
    private ISSN issn;
    private String country;
    private MedlineTA medlineTA;
    private NlmUniqueID nlmUniqueID;

    private MedlineJournalInfoElement(Element element) {
        super(element, TAG_NAME);

        this.issn = parseISSN();
        this.country = getChildText("Country");
        this.medlineTA = parseMedlineTA();
        this.nlmUniqueID = parseNlmUniqueId();
    }

    private ISSN parseISSN() {
        String issnText = getChildText("ISSN");

        if (issnText != null && ISSN.isValid(issnText))
            return ISSN.instance(issnText);
        else
            return null;
    }

    private MedlineTA parseMedlineTA() {
        return MedlineTA.instance(getChildText("MedlineTA"));
    }

    private NlmUniqueID parseNlmUniqueId() {
        String idText = getChildText("NlmUniqueID");

        if (idText != null && NlmUniqueID.isValid(idText))
            return NlmUniqueID.instance(idText);
        else
            return null;
    }

    /**
     * The tag name for the element.
     */
    public static final String TAG_NAME = "MedlineJournalInfo";

    /**
     * Extracts the journal element from the parent citation.
     *
     * @param parent the parent citation.
     *
     * @return the decorated journal information element.
     */
    public static MedlineJournalInfoElement from(MedlineCitationElement parent) {
        return new MedlineJournalInfoElement(parent.getRequiredChild(TAG_NAME));
    }

    /**
     * Returns the country of publication for the journal.
     *
     * @return the country of publication for the journal.
     */
    public String getCountry() {
        return country;
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
     * Returns the {@code MedlineTA} (title abbreviation) for the journal.
     *
     * @return the {@code MedlineTA} (title abbreviation) for the journal.
     */
    public MedlineTA getMedlineTA() {
        return medlineTA;
    }

    /**
     * Returns the {@code NlmUniqueID} for the journal.
     *
     * @return the {@code NlmUniqueID} for the journal.
     */
    public NlmUniqueID getNlmUniqueID() {
        return nlmUniqueID;
    }
}
