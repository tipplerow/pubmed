
package pubmed.sql;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import jam.sql.SQLColumn;

import pubmed.article.PMID;

/**
 * Maintains a database table with exactly two columns: a {@code PMID}
 * article identifier and a text column.
 */
public abstract class ArticleTextAttrTable<V extends ArticleTextAttrRecord> extends ArticleAttrTable<V> {
    /**
     * Creates a new table with the default database manager.
     */
    protected ArticleTextAttrTable() {
        super();
    }

    /**
     * Returns the name of the column containing the text attribute.
     *
     * @return the name of the column containing the text attribute.
     */
    public abstract String getTextColumnName();

    /**
     * Returns the meta-data for the column containing the text
     * attribute.
     *
     * @return the meta-data for the column containing the text
     * attribute.
     */
    public SQLColumn getTextColumn() {
        return SQLColumn.create(getTextColumnName(), "text");
    }
    
    @Override public List<SQLColumn> getColumns() {
        return List.of(PMID_PRIMARY_KEY_COLUMN, getTextColumn());
    }

    @Override public void prepareColumn(PreparedStatement statement, int index, V record, String colName) throws SQLException {
        if (colName.equals(PMID_NAME)) {
            setPMID(statement, index, record.getPMID());
            return;
        }

        if (colName.equals(getTextColumnName())) {
            setString(statement, index, record.getText());
            return;
        }

        throw invalidColumn(colName);
    }
}
