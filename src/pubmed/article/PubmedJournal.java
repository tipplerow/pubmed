
package pubmed.article;

import jam.lang.ObjectUtil;
import jam.sql.BulkRecord;

/**
 * Encapsulates journal information within the {@code PubMed}
 * database.
 */
public final class PubmedJournal implements BulkRecord {
    private final ISSN issn;
    private final String title;
    private final String isoAbbrev;

    private PubmedJournal(ISSN issn, String title, String isoAbbrev) {
        this.issn = issn;
        this.title = title;
        this.isoAbbrev = isoAbbrev;
    }

    /**
     * Creates a new journal record.
     *
     * @param issn the International Standard Serial Number (ISSN).
     *
     * @param title the full journal title.
     *
     * @param isoAbbrev the ISO title abbreviation.
     *
     * @return the new journal record.
     */
    public static PubmedJournal create(ISSN issn, String title, String isoAbbrev) {
        return new PubmedJournal(issn, title, isoAbbrev);
    }

    /**
     * Returns the ISO title abbreviation for this journal.
     *
     * @return the ISO title abbreviation for this journal.
     */
    public String getISOAbbreviation() {
        return isoAbbrev;
    }

    /**
     * Returns the International Standard Serial Number (ISSN) for
     * this journal.
     *
     * @return the International Standard Serial Number (ISSN) for
     * this journal.
     */
    public ISSN getISSN() {
        return issn;
    }

    /**
     * Returns the full title of this journal.
     *
     * @return the full title of this journal.
     */
    public String getTitle() {
        return title;
    }

    @Override public String formatBulk() {
        return joinBulk(formatBulk(issn),
                        formatBulk(title),
                        formatBulk(isoAbbrev));
    }
}
