
package pubmed.sql;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import pubmed.article.PMID;

/**
 * Provides a base class for {@code pubmed} database tables that store
 * many-to-many mappings between articles and other string attributes.
 */
public abstract class ArticleTextJoinTable<V extends ArticleTextJoinRecord> extends ArticleJoinTable<String, V> {
    /**
     * Creates a new joining table with the default database manager.
     */
    protected ArticleTextJoinTable() {
        super();
    }

    @Override public String getKey2Type() {
        return "text";
    }

    @Override public void setKey2(PreparedStatement statement, int index, String text) throws SQLException {
        setString(statement, index, text);
    }
}
