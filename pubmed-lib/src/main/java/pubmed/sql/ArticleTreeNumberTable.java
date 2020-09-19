
package pubmed.sql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

import pubmed.article.PMID;
import pubmed.article.PubmedArticle;

/**
 * Maintains the {@code article_tree_numbers} table: a many-to-many
 * mapping between articles and the {@code MeSH} tree numbers of the
 * descriptors in their {@code MeSH} headings.
 */
public final class ArticleTreeNumberTable extends ArticleTextJoinTable<ArticleTreeNumberRecord> {
    private static ArticleTreeNumberTable instance = null;

    private ArticleTreeNumberTable() {
        super();
    }

    /**
     * The name of the {@code chemicals} table.
     */
    public static final String TABLE_NAME = "article_tree_numbers";

    /**
     * The name of the {@code tree_number} column.
     */
    public static final String TREE_NUMBER_NAME = "tree_number";

    /**
     * Returns the single instance.
     *
     * @return the single instance.
     */
    public static synchronized ArticleTreeNumberTable instance() {
        if (instance == null)
            instance = new ArticleTreeNumberTable();

        instance.require();
        return instance;
    }

    /**
     * Drops the physical database table.
     */
    public static synchronized void dropTable() {
        DbEnv.activeDb().dropTable(TABLE_NAME);
    }

    @Override public Collection<ArticleTreeNumberRecord> getBulkRecords(PubmedArticle article) {
        return ArticleTreeNumberRecord.list(article);
    }

    @Override public String getKey2Name() {
        return TREE_NUMBER_NAME;
    }

    @Override public ArticleTreeNumberRecord getRecord(ResultSet resultSet) throws SQLException {
        PMID pmid = getPMID(resultSet, PMID_NAME);
        String mkey = getString(resultSet, TREE_NUMBER_NAME);

        return ArticleTreeNumberRecord.create(pmid, mkey);
    }

    @Override public String getTableName() {
        return TABLE_NAME;
    }
}
