
package pubmed.sql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

import pubmed.article.PMID;
import pubmed.article.PubmedArticle;

/**
 * Maintains the {@code pubmed.keywords} table: a many-to-many mapping
 * between articles and keywords.
 */
public final class KeywordTable extends ArticleTextJoinTable<KeywordRecord> {
    private static KeywordTable instance = null;

    private KeywordTable() {
        super();
    }

    /**
     * The name of the {@code pubmed.keywords} table.
     */
    public static final String TABLE_NAME = "keywords";

    /**
     * The name of the {@code keyword} column.
     */
    public static final String KEYWORD_NAME = "keyword";

    /**
     * Returns the single instance.
     *
     * @return the single instance.
     */
    public static synchronized KeywordTable instance() {
        if (instance == null)
            instance = new KeywordTable();

        instance.require();
        return instance;
    }

    /**
     * Drops the physical database table.
     */
    public static synchronized void dropTable() {
        DbEnv.activeDb().dropTable(TABLE_NAME);
    }

    @Override public Collection<KeywordRecord> getBulkRecords(PubmedArticle article) {
        return KeywordRecord.list(article);
    }

    @Override public String getKey2Name() {
        return KEYWORD_NAME;
    }

    @Override public KeywordRecord getRecord(ResultSet resultSet) throws SQLException {
        PMID pmid = getPMID(resultSet, PMID_NAME);
        String keyword = getString(resultSet, KEYWORD_NAME);

        return KeywordRecord.create(pmid, keyword);
    }

    @Override public String getTableName() {
        return TABLE_NAME;
    }
}
