
package pubmed.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import jam.app.JamLogger;
import jam.sql.BulkRecord;
import jam.sql.QueryResult;
import jam.util.ListUtil;

import pubmed.article.PMID;
import pubmed.article.PubmedArticle;

/**
 * Identifies a {@code pubmed} database table as one that is populated
 * from bulk {@code PubMed} XML files.
 */
public interface BulkFileTarget<V extends BulkRecord> extends PubmedResource {
    /**
     * Converts a collection of articles into a list of records to be
     * stored or updated in this table.
     *
     * @param articles a collection of articles to be stored or updated.
     *
     * @return a (possibly filtered) list of database records for the
     * articles that will be stored or updated.
     */
    public abstract Collection<V> bulkRecords(Collection<PubmedArticle> articles);

    /**
     * Closes any database resources created by this table.
     */
    public default void close() {};

    /**
     * Removes an article from this table.
     *
     * @param article the article to remove.
     *
     * @return {@code true} iff the article was removed successfully.
     */
    public default boolean deleteArticle(PubmedArticle article) {
        return deleteCitations(List.of(article.getPMID()));
    }

    /**
     * Removes all records associated with a collection of articles.
     *
     * @param articles the articles to remove.
     *
     * @return {@code true} iff all records were removed successfully.
     */
    public default boolean deleteArticles(Collection<PubmedArticle> articles) {
        return deleteCitations(ListUtil.apply(articles, article -> article.getPMID()));
    }

    /**
     * Removes all records matching a collection of identifiers.
     *
     * @param pmids the identifiers of the records to remove.
     *
     * @return {@code true} iff all records were removed successfully.
     */
    public default boolean deleteCitations(Collection<PMID> pmids) {
        if (pmids.isEmpty())
            return true;

        JamLogger.info("Deleting [%d] citations from [%s]...", pmids.size(), getTableName());

        String deleteCommand =
            String.format("DELETE FROM %s WHERE %s = ?",
                          getTableName(), PMID_NAME);

        try (Connection connection = DbEnv.activeDb().openConnection(false)) {
            PreparedStatement statement =
                connection.prepareStatement(deleteCommand);

            for (PMID pmid : pmids) {
                setPMID(statement, 1, pmid);
                statement.executeUpdate();
            }

            connection.commit();
            return true;
        }
        catch (SQLException ex) {
            JamLogger.error(ex.getMessage());
            return false;
        }
    }

    /**
     * Returns the maximum {@code PMID} in this table (so that
     * previously processed articles may be identified).
     *
     * @return the maximum {@code PMID} in this table.
     */
    public default PMID getMaxPMID() {
	String queryString =
	    String.format("SELECT MAX(%s) FROM %s", PMID_NAME, getTableName());

	try (QueryResult queryResult = DbEnv.activeDb().executeQuery(queryString)) {
	    return getSinglePMID(queryResult.getResultSet(), 1);
	}
    }

    /**
     * Returns the name of this table.
     *
     * @return the name of this table.
     */
    public abstract String getTableName();

    /**
     * Inserts a single article into this table.
     *
     * @param article the original article to insert.
     *
     * @return {@code true} iff all article was added successfully.
     */
    public default boolean insertArticle(PubmedArticle article) {
        return insertArticles(List.of(article));
    }

    /**
     * Populates this table using the original articles loaded from a
     * bulk data file.
     *
     * @param articles the original articles contained in the bulk
     * data file.
     *
     * @return {@code true} iff all articles were added successfully.
     */
    public default boolean insertArticles(Collection<PubmedArticle> articles) {
        Collection<V> records = bulkRecords(articles);

        if (records.isEmpty())
            return true;
        else
            return insertRecords(records);
    }

    /**
     * Inserts bulk records into this table.
     *
     * @param records the records to insert.
     *
     * @return {@code true} iff all records were inserted
     * successfully.
     */
    public default boolean insertRecords(Collection<V> records) {
        if (records.isEmpty())
            return true;

        JamLogger.info("Inserting [%d] records into [%s]...",
                       records.size(), getTableName());

        return DbEnv.activeDb().bulkCopy(getTableName(), records);
    }

    /**
     * Processes the latest articles contained in a bulk data file.
     *
     * @param articles the latest articles contained in a bulk data
     * file.
     *
     * @return {@code true} iff all articles were processed successfully.
     */
    public default boolean processLatest(Collection<PubmedArticle> articles) {
	PMID maxPMID = getMaxPMID();

	if (maxPMID == null)
            return insertArticles(articles);

        List<PubmedArticle> originals = new ArrayList<PubmedArticle>();
        List<PubmedArticle> revisions = new ArrayList<PubmedArticle>();

        for (PubmedArticle article : articles)
            if (article.getPMID().intValue() > maxPMID.intValue())
                originals.add(article);
            else
                revisions.add(article);

        return updateArticles(revisions) && insertArticles(originals);
    }

    /**
     * Updates a single article in this table.
     *
     * @param article the revised article to update.
     *
     * @return {@code true} iff the article was updated successfully.
     */
    public default boolean updateArticle(PubmedArticle article) {
        return updateArticles(List.of(article));
    }

    /**
     * Updates the records in this table with revised articles loaded
     * from a bulk data file.
     *
     * @param articles the revised articles contained in the bulk data
     * file.
     *
     * @return {@code true} iff all articles were updated successfully.
     */
    public default boolean updateArticles(Collection<PubmedArticle> articles) {
        Collection<V> records = bulkRecords(articles);

        if (records.isEmpty())
            return true;
        else
            return updateRecords(records);
    }

    /**
     * Updates existing records in this table.
     *
     * @param records the records to update.
     *
     * @return {@code true} iff all records were updated successfully.
     */
    public abstract boolean updateRecords(Collection<V> records);
}
