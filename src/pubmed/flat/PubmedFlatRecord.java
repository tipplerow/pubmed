
package pubmed.flat;

import jam.flat.FlatRecord;

import pubmed.article.PMID;

/**
 * A data record that may be written to a delimited flat file.
 */
public interface PubmedFlatRecord<K> extends FlatRecord<K> {
    /**
     * Formats a (possibly {@code null}) identifier.
     *
     * @param pmid the (possibly {@code null}) identifier.
     *
     * @return the string value of the identifier, if it is not
     * {@code null}, otherwise the {@code NULL_STRING}.
     */
    public default String format(PMID pmid) {
        return (pmid != null) ? Integer.toString(pmid.intValue()) : NULL_STRING;
    }
}
