
package pubmed.flat;

import jam.flat.JoinRecord;

import pubmed.article.PMID;

/**
 * Provides a base class for article joining data rows keyed by {@code PMID}.
 */
public abstract class PubmedJoinRecord<K2> extends PubmedFlatRecord implements JoinRecord<PMID, K2> {
    /**
     * The foreign key for the record.
     */
    protected final K2 fkey;

    /**
     * Creates a new record with fixed keys.
     *
     * @param pmid the primary key for the record.
     *
     * @param fkey the foreign key for the record.
     */
    protected PubmedJoinRecord(PMID pmid, K2 fkey) {
        super(pmid);
        this.fkey = fkey;
        validateForeignKey();
    }

    private void validateForeignKey() {
        if (fkey == null)
            throw new NullPointerException("Null foreign key.");
    }

    @Override public K2 getForeignKey() {
        return fkey;
    }

    @Override public int hashCode() {
        return 137 * pmid.intValue() + fkey.hashCode();
    }
}
