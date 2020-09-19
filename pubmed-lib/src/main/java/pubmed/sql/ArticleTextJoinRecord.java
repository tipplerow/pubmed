
package pubmed.sql;

import jam.sql.SQLPairRecord;

import pubmed.article.PMID;

/**
 * Provides a base class for table rows that represent one instance of
 * a many-to-many mapping between an article and another text attribute.
 */
public abstract class ArticleTextJoinRecord extends ArticleJoinRecord<String> {
    /**
     * Creates a new record with fixed keys.
     *
     * @param pmid the primary identifier for the article.
     *
     * @param text the second record key.
     */
    protected ArticleTextJoinRecord(PMID pmid, String text) {
        super(pmid, text);
    }

    @Override public String formatBulk() {
        return joinBulk(formatBulk(key1), formatBulk(key2));
    }
}
