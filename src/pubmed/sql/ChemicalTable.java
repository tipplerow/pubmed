
package pubmed.sql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

import pubmed.article.PMID;
import pubmed.article.PubmedArticle;

/**
 * Maintains the {@code pubmed.chemicals} table: a many-to-many mapping
 * between articles and substances in their chemical lists.
 */
public final class ChemicalTable extends ArticleTextJoinTable<ChemicalRecord> {
    private static ChemicalTable instance = null;

    private ChemicalTable() {
        super();
    }

    /**
     * The name of the {@code pubmed.chemicals} table.
     */
    public static final String TABLE_NAME = "chemicals";

    /**
     * The name of the {@code mesh_key} column.
     */
    public static final String MESH_KEY_NAME = "mesh_key";

    /**
     * Returns the single instance.
     *
     * @return the single instance.
     */
    public static synchronized ChemicalTable instance() {
        if (instance == null)
            instance = new ChemicalTable();

        instance.require();
        return instance;
    }

    /**
     * Drops the physical database table.
     */
    public static synchronized void dropTable() {
        DbEnv.activeDb().dropTable(TABLE_NAME);
    }

    @Override public Collection<ChemicalRecord> getBulkRecords(PubmedArticle article) {
        return ChemicalRecord.list(article);
    }

    @Override public String getKey2Name() {
        return MESH_KEY_NAME;
    }

    @Override public ChemicalRecord getRecord(ResultSet resultSet) throws SQLException {
        PMID pmid = getPMID(resultSet, PMID_NAME);
        String mkey = getString(resultSet, MESH_KEY_NAME);

        return ChemicalRecord.create(pmid, mkey);
    }

    @Override public String getTableName() {
        return TABLE_NAME;
    }
}
