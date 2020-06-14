
package pubmed.sql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import jam.sql.SQLColumn;
import jam.sql.SQLDb;
import jam.sql.SQLKeyTable;

import pubmed.mesh.MeshDB;
import pubmed.mesh.MeshRecordKey;
import pubmed.mesh.MeshTerm;

/**
 * Maintains the {@code mesh_terms} table, which contains the term
 * itself, the key of its preferred concept, and a boolean flag
 * indicating whether it is the preferred term for its concept.
 */
public final class MeshTermTable extends SQLKeyTable<String, MeshTerm> {
    private static MeshTermTable instance = null;

    private MeshTermTable() {
        super(DbEnv.activeDb());
    }

    /**
     * The name of the {@code pubmed.mesh_terms} table.
     */
    public static final String TABLE_NAME = "mesh_terms";

    /**
     * The name of the {@code mesh_term} column.
     */
    public static final String TERM_NAME = "mesh_term";

    /**
     * The name of the {@code concept_key} column.
     */
    public static final String CONCEPT_KEY_NAME = "concept_key";

    /**
     * The name of the {@code preferred} column.
     */
    public static final String PREFERRED_NAME = "preferred";

    /**
     * Meta-data for the {@code mesh_term} column.
     */
    public static final SQLColumn TERM_COLUMN =
        SQLColumn.create(TERM_NAME, "text")
        .primaryKey();

    /**
     * Meta-data for the {@code concept_key} column.
     */
    public static final SQLColumn CONCEPT_KEY_COLUMN =
        SQLColumn.create(CONCEPT_KEY_NAME, "text")
        .withIndex()
        .notNull();

    /**
     * Meta-data for the {@code preferred} column.
     */
    public static final SQLColumn PREFERRED_COLUMN =
        SQLColumn.create(PREFERRED_NAME, "boolean")
        .withIndex()
        .notNull();

    private static final List<SQLColumn> COLUMN_LIST =
        List.of(TERM_COLUMN, CONCEPT_KEY_COLUMN, PREFERRED_COLUMN);

    /**
     * Returns the single instance.
     *
     * @return the single instance.
     */
    public static synchronized MeshTermTable instance() {
        if (instance == null)
            instance = new MeshTermTable();

        return instance;
    }

    /**
     * Populates the database table with all known {@code MeSH} terms
     * unless the table already exists.
     *
     * @param db the active database manager.
     *
     * @throws RuntimeException if any errors occur.
     */
    public static synchronized void populate(SQLDb db) {
        if (db.tableExists(TABLE_NAME))
            return;
        
        MeshDB.load();

        List<MeshTerm> terms = MeshTerm.list();
        Collections.sort(terms);

        db.createTable(TABLE_NAME, COLUMN_LIST);
        db.bulkCopy(TABLE_NAME, terms);
    }

    @Override public List<SQLColumn> getColumns() {
        return COLUMN_LIST;
    }

    @Override public String getKey(MeshTerm term) {
        return term.getTerm();
    }

    @Override public String getKey(ResultSet resultSet, String columnLabel) throws SQLException {
        return getString(resultSet, columnLabel);
    }

    @Override public MeshTerm getRow(ResultSet resultSet) throws SQLException {
        String term = getString(resultSet, TERM_NAME);
        boolean preferred = resultSet.getBoolean(PREFERRED_NAME);
        MeshRecordKey conceptKey = MeshRecordKey.instance(getString(resultSet, CONCEPT_KEY_NAME));

        return MeshTerm.create(term, preferred, conceptKey);
    }

    @Override public String getTableName() {
        return TABLE_NAME;
    }

    @Override public void prepareColumn(PreparedStatement statement, int index,
                                        MeshTerm record, String columnName) throws SQLException {
        throw new UnsupportedOperationException("The table must be populated with a bulk copy.");
    }

    @Override public void prepareKey(PreparedStatement statement, int index, String key) throws SQLException {
        setString(statement, index, key);
    }

    private static void usage() {
        System.err.println("Usage pubmed.sql.MeshTermTable PROD | TEST");
        System.exit(1);
    }

    public static void main(String[] args) {
        if (args.length != 1)
            usage();

        DbEnv env = DbEnv.valueOf(args[0]);
        SQLDb db  = env.db();

        populate(db);
    }
}
