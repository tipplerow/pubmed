
package pubmed.sql;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import jam.app.JamLogger;
import jam.sql.SQLPairTable;

import pubmed.article.PMID;
import pubmed.article.PubmedArticle;

/**
 * Provides a base class for {@code pubmed} database tables that store
 * many-to-many mappings between articles and other attributes and/or
 * subjects.
 */
public abstract class ArticleJoinTable<K2, V extends ArticleJoinRecord<K2>>
    extends SQLPairTable<PMID, K2, V> implements BulkFileTarget<V>, PubmedResource {

    /**
     * Creates a new joining table with the default database manager.
     */
    protected ArticleJoinTable() {
        super(DbEnv.activeDb());
    }

    /**
     * Extracts the table records from a single article.
     *
     * @param article an article to be stored or updated.
     *
     * @return the database records for the specified article.
     */
    public abstract Collection<V> getBulkRecords(PubmedArticle article);

    @Override public Collection<V> bulkRecords(Collection<PubmedArticle> articles) {
        Collection<V> records =
            new ArrayList<V>();

        for (PubmedArticle article : articles)
            records.addAll(getBulkRecords(article));

        return records;
    }

    @Override public boolean deleteCitations(Collection<PMID> pmids) {
        try {
            for (PMID pmid : pmids)
                removeKey1(pmid);

            return true;
        }
        catch (Exception ex) {
            JamLogger.error(ex.getMessage());
            return false;
        }
    }

    @Override public String getKey1Name() {
        return PMID_NAME;
    }

    @Override public String getKey1Type() {
        return PMID_TYPE;
    }

    @Override public void setKey1(PreparedStatement statement, int index, PMID pmid) throws SQLException {
        setPMID(statement, index, pmid);
    }

    @Override public boolean updateArticles(Collection<PubmedArticle> articles) {
        //
        // It is very unlikely that a revision to the article will
        // change its joining relationships, so this is a no-op...
        //
        return true;
    }

    @Override public boolean updateRecords(Collection<V> records) {
        //
        // It is very unlikely that a revision to the article will
        // change its joining relationships, so this is a no-op...
        //
        return true;
    }
}
