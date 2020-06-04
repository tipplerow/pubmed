
package pubmed.sql;

import jam.sql.SQLPairRecord;

import pubmed.article.PMID;

/**
 * Provides a base class for table rows that represent one instance of
 * a many-to-many mapping between an article and another attribute or
 * subject.
 */
public abstract class ArticleJoinRecord<K2> extends SQLPairRecord<PMID, K2> implements PubmedBulkRecord {
    /**
     * Creates a new record with fixed keys.
     *
     * @param pmid the primary identifier for the article.
     *
     * @param key2 the second record key.
     */
    protected ArticleJoinRecord(PMID pmid, K2 key2) {
        super(pmid, key2);
    }

    /**
     * Returns the unique {@code PubMed} identifier for the article.
     *
     * @return the unique {@code PubMed} identifier for the article.
     */
    public PMID getPMID() {
        return key1;
    }
}
