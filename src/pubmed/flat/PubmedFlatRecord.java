
package pubmed.flat;

import jam.flat.FlatRecord;

import pubmed.article.PMID;

/**
 * Provides a base class for data rows containing article attributes
 * keyed by {@code PMID}.
 */
public abstract class PubmedFlatRecord implements FlatRecord<PMID>, FlatRecordBase {
    /**
     * The {@code PubMed} identifier for the article.
     */
    protected final PMID pmid;

    /**
     * Creates a new record with a fixed primary key.
     *
     * @param pmid the primary key for the record.
     */
    protected PubmedFlatRecord(PMID pmid) {
        this.pmid = pmid;
    }

    /**
     * Returns the {@code PubMed} identifier for the article.
     *
     * @return the {@code PubMed} identifier for the article.
     */
    public PMID getPMID() {
        return pmid;
    }

    @Override public PMID getPrimaryKey() {
        return pmid;
    }

    @Override public int hashCode() {
        return pmid.intValue();
    }
}
