
package pubmed.sql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

import jam.sql.SQLColumn;

import pubmed.article.PMID;
import pubmed.article.PubmedArticle;

/**
 * Maintains the {@code pubmed.abstracts} table.
 */
public final class AbstractTable extends ArticleTextAttrTable<AbstractRecord> {
    private static AbstractTable instance = null;

    private AbstractTable() {
    }

    /**
     * The name of the {@code pubmed.abstracts} table.
     */
    public static final String TABLE_NAME = "abstracts";

    /**
     * The name of the {@code abstract} column.
     */
    public static final String ABSTRACT_NAME = "abstract";

    /**
     * Returns the single instance.
     *
     * @return the single instance.
     */
    public static synchronized AbstractTable instance() {
        if (instance == null)
            instance = new AbstractTable();

        instance.require();
        return instance;
    }

    /**
     * Drops the physical database table.
     */
    public static synchronized void dropTable() {
        DbEnv.activeDb().dropTable(TABLE_NAME);
    }

    @Override public AbstractRecord getBulkRecord(PubmedArticle article) {
        return AbstractRecord.create(article);
    }

    @Override public AbstractRecord getRow(ResultSet resultSet) throws SQLException {
        PMID pmid = getPMID(resultSet, PMID_NAME);
        String text = getString(resultSet, getTextColumnName());

        return AbstractRecord.create(pmid, text);
    }

    @Override public SQLColumn getTextColumn() {
        return super.getTextColumn().notNull();
    }

    @Override public String getTextColumnName() {
        return ABSTRACT_NAME;
    }

    @Override public String getTableName() {
        return TABLE_NAME;
    }

    @Override public boolean hasBulkRecord(PubmedArticle article) {
        return article.hasAbstract();
    }

    @Override public boolean updateRecords(Collection<AbstractRecord> records) {
	//
	// Abstracts should not change...
	//
	return true;
    }
}
