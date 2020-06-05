
package pubmed.sql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

import jam.sql.SQLColumn;

import pubmed.article.PMID;
import pubmed.article.PubmedArticle;

/**
 * Maintains the {@code pubmed.titles} table.
 */
public final class TitleTable extends ArticleTextAttrTable<TitleRecord> {
    private static TitleTable instance = null;

    private TitleTable() {
    }

    /**
     * The name of the {@code pubmed.titles} table.
     */
    public static final String TABLE_NAME = "titles";

    /**
     * The name of the {@code title} column.
     */
    public static final String TITLE_NAME = "title";

    /**
     * Returns the single instance.
     *
     * @return the single instance.
     */
    public static synchronized TitleTable instance() {
        if (instance == null)
            instance = new TitleTable();

        instance.require();
        return instance;
    }

    /**
     * Drops the physical database table.
     */
    public static synchronized void dropTable() {
        DbEnv.activeDb().dropTable(TABLE_NAME);
    }

    @Override public TitleRecord getBulkRecord(PubmedArticle article) {
        return TitleRecord.create(article);
    }

    @Override public TitleRecord getRow(ResultSet resultSet) throws SQLException {
        PMID pmid = getPMID(resultSet, PMID_NAME);
        String title = getString(resultSet, TITLE_NAME);

        return TitleRecord.create(pmid, title);
    }

    @Override public SQLColumn getTextColumn() {
        return super.getTextColumn().notNull();
    }

    @Override public String getTextColumnName() {
        return TITLE_NAME;
    }

    @Override public String getTableName() {
        return TABLE_NAME;
    }

    @Override public boolean hasBulkRecord(PubmedArticle article) {
        return article.hasTitle();
    }

    @Override public boolean updateRecords(Collection<TitleRecord> records) {
	//
	// Titles should not change...
	//
	return true;
    }
}
