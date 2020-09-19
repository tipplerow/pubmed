
package pubmed.medline;

import jam.lang.ObjectUtil;
import jam.sql.BulkRecord;

import pubmed.article.ISSN;

/**
 * Encapsulates {@code MEDLINE} journal information within the
 * {@code PubMed} database.
 */
public final class MedlineJournal implements BulkRecord {
    private final ISSN issn;
    private final String country;
    private final MedlineTA medlineTA;
    private final NlmUniqueID nlmUniqueID;

    private MedlineJournal(MedlineTA medlineTA, NlmUniqueID nlmUniqueID, ISSN issn, String country) {
        this.issn = issn;
        this.country = country;
        this.medlineTA = medlineTA;
        this.nlmUniqueID = nlmUniqueID;
    }

    /**
     * Creates a new {@code MEDLINE} journal.
     *
     * @param medlineTA the {@code MEDLINE} title abbreviation.
     *
     * @param nlmUniqueID the National Library of Medicine identifier.
     *
     * @param issn the International Standard Serial Number.
     *
     * @param country the country of publication.
     *
     * @return the new {@code MEDLINE} journal.
     */
    public static MedlineJournal create(MedlineTA medlineTA,
                                        NlmUniqueID nlmUniqueID,
                                        ISSN issn,
                                        String country) {
        return new MedlineJournal(medlineTA, nlmUniqueID, issn, country);
    }

    /**
     * Returns the country where this journal is published.
     *
     * @return the country where this journal is published.
     */
    public String getCountry() {
        return country;
    }

    /**
     * Returns the International Standard Serial Number for this
     * journal.
     *
     * @return the International Standard Serial Number for this
     * journal.
     */
    public ISSN getISSN() {
        return issn;
    }

    /**
     * Returns the {@code MEDLINE} title abbreviation for this
     * journal.
     *
     * @return the {@code MEDLINE} title abbreviation for this
     * journal.
     */
    public MedlineTA getMedlineTA() {
        return medlineTA;
    }

    /**
     * Returns the National Library of Medicine identifier for this
     * journal.
     *
     * @return the National Library of Medicine identifier for this
     * journal.
     */
    public NlmUniqueID getNlmUniqueID() {
        return nlmUniqueID;
    }

    @Override public String formatBulk() {
        return joinBulk(formatBulk(medlineTA),
                        formatBulk(nlmUniqueID),
                        formatBulk(issn),
                        formatBulk(country));
    }

    @Override public boolean equals(Object obj) {
        return (obj instanceof MedlineJournal) && equalsJournal((MedlineJournal) obj);
    }

    private boolean equalsJournal(MedlineJournal that) {
        return ObjectUtil.equals(this.medlineTA,   that.medlineTA)
            && ObjectUtil.equals(this.nlmUniqueID, that.nlmUniqueID)
            && ObjectUtil.equals(this.issn,        that.issn)
            && ObjectUtil.equals(this.country,     that.country);
    }

    @Override public int hashCode() {
        return medlineTA.hashCode();
    }

    @Override public String toString() {
        return String.format("MedlineJournal(%s)", medlineTA.getKey());
    }
}
