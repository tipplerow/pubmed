
package pubmed.flat;

import jam.flat.JoinRecord;
import jam.lang.ObjectUtil;

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

    @Override public boolean equalsData(Object record) {
        //
        // Subclasses with additional data fields must override...
        //
        return true;
    }

    @Override public boolean equals(Object obj) {
        return ObjectUtil.equalsClass(this, obj) && equalsRecord((PubmedJoinRecord) obj);
    }

    private boolean equalsRecord(PubmedJoinRecord that) {
        return this.pmid.equals(that.pmid) && this.fkey.equals(that.fkey) && equalsData(that);
    }

    @Override public int hashCode() {
        return 137 * pmid.intValue() + fkey.hashCode();
    }

    @Override public String toString() {
        return String.format("%s(%s, %s)", getClass().getSimpleName(), pmid.toString(), fkey.toString());
    }
}
