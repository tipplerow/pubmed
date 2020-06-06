
package pubmed.sql;

import java.sql.ResultSet;
import java.sql.SQLException;

import pubmed.article.PMID;
import pubmed.article.PubmedArticle;

/**
 * Maintains a table that defines a many-to-many mapping between
 * articles and lemmatized key words.
 */
public abstract class ArticleLemmaTable extends ArticleTextJoinTable<ArticleLemmaRecord> {
    /**
     * Creates a new joining lemma table with the default database
     * manager.
     */
    protected ArticleLemmaTable() {
        super();
    }

    /**
     * The name of the {@code lemma} column.
     */
    public static final String LEMMA_NAME = "lemma";
    
    @Override public String getKey2Name() {
        return LEMMA_NAME;
    }

    @Override public ArticleLemmaRecord getRecord(ResultSet resultSet) throws SQLException {
        PMID pmid = getPMID(resultSet, 1);
        String lemma = resultSet.getString(2);

        return ArticleLemmaRecord.create(pmid, lemma);
    }
}
