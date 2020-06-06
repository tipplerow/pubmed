
package pubmed.sql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

import pubmed.article.PMID;
import pubmed.article.PubmedArticle;

/**
 * Maintains the {@code mesh_tree_numbers} table: a many-to-many
 * mapping between articles and the {@code MeSH} tree numbers of
 * descriptors in their {@code MeSH} headings.
 */
public final class MeshTreeNumberTable extends ArticleTextJoinTable<MeshTreeNumberRecord> {
    private static MeshTreeNumberTable instance = null;

    private MeshTreeNumberTable() {
        super();
    }

    /**
     * The name of the {@code chemicals} table.
     */
    public static final String TABLE_NAME = "mesh_tree_numbers";

    /**
     * The name of the {@code tree_number} column.
     */
    public static final String TREE_NUMBER_NAME = "tree_number";

    /**
     * Returns the single instance.
     *
     * @return the single instance.
     */
    public static synchronized MeshTreeNumberTable instance() {
        if (instance == null)
            instance = new MeshTreeNumberTable();

        instance.require();
        return instance;
    }

    /**
     * Drops the physical database table.
     */
    public static synchronized void dropTable() {
        DbEnv.activeDb().dropTable(TABLE_NAME);
    }

    @Override public Collection<MeshTreeNumberRecord> getBulkRecords(PubmedArticle article) {
        return MeshTreeNumberRecord.list(article);
    }

    @Override public String getKey2Name() {
        return TREE_NUMBER_NAME;
    }

    @Override public MeshTreeNumberRecord getRecord(ResultSet resultSet) throws SQLException {
        PMID pmid = getPMID(resultSet, PMID_NAME);
        String mkey = getString(resultSet, TREE_NUMBER_NAME);

        return MeshTreeNumberRecord.create(pmid, mkey);
    }

    @Override public String getTableName() {
        return TABLE_NAME;
    }
}
