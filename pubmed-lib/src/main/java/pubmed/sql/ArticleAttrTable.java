
package pubmed.sql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import jam.app.JamLogger;
import jam.sql.SQLKeyTable;

import pubmed.article.PMID;
import pubmed.article.PubmedArticle;

/**
 * Manages tables that represent a one-to-one mapping between
 * {@code PubMed} articles and unique attributes or records.
 */
public abstract class ArticleAttrTable<V extends ArticleAttrRecord>
    extends SQLKeyTable<PMID, V> implements BulkFileTarget<V>, PubmedResource {

    /**
     * Creates a new table with the default database manager.
     */
    protected ArticleAttrTable() {
        super(DbEnv.activeDb());
    }

    /**
     * Extracts the table record from a single article.
     *
     * @param article an article to be stored or updated.
     *
     * @return the database record for the specified article.
     */
    public abstract V getBulkRecord(PubmedArticle article);

    /**
     * Determines whether an article contains a (non-null) record.
     *
     * @param article an article to be stored or updated.
     *
     * @return {@code true} iff the specified article has a non-null
     * record.
     */
    public abstract boolean hasBulkRecord(PubmedArticle article);

    @Override public Collection<V> bulkRecords(Collection<PubmedArticle> articles) {
        Collection<V> records =
            new ArrayList<V>(articles.size());

        for (PubmedArticle article : articles)
            if (hasBulkRecord(article))
                records.add(getBulkRecord(article));

        return records;
    }

    @Override public PMID getKey(V record) {
        return record.getPMID();
    }

    @Override public PMID getKey(ResultSet resultSet, String columnLabel) throws SQLException {
        return getPMID(resultSet, columnLabel);
    }

    @Override public void prepareKey(PreparedStatement statement, int index, PMID pmid) throws SQLException {
        setPMID(statement, index, pmid);
    }

    @Override public boolean updateRecords(Collection<V> records) {
        if (records.isEmpty())
            return true;

        JamLogger.info("Updating [%d] records in [%s]...", records.size(), getTableName());

        try {
            update(records);
            return true;
        }
        catch (Exception ex) {
            JamLogger.error(ex.getMessage());
            return false;
        }
    }
}
