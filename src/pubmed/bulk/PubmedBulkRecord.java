
package pubmed.bulk;

import jam.sql.BulkRecord;

import pubmed.article.PMID;
import pubmed.article.PubmedDate;

/**
 * A {@code pubmed} database record that may be written to a delimited
 * flat file to allow a bulk import (or {@code PostgreSQL COPY}).
 */
public interface PubmedBulkRecord extends BulkRecord {
    /**
     * Formats a (possibly {@code null}) identifier.
     *
     * @param pmid the (possibly {@code null}) identifier.
     *
     * @return the string value of the identifier, if it is not
     * {@code null}, otherwise the {@code NULL_STRING}.
     */
    public default String formatBulk(PMID pmid) {
        return (pmid != null) ? Integer.toString(pmid.intValue()) : NULL_STRING;
    }

    /**
     * Formats a (possibly {@code null}) publication date.
     *
     * @param date the (possibly {@code null}) publication date.
     *
     * @return the string value of the date, if it is not
     * {@code null}, otherwise the {@code NULL_STRING}.
     */
    public default String formatBulk(PubmedDate date) {
        return (date != null) ? date.format() : NULL_STRING;
    }
}
