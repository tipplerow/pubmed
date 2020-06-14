
package pubmed.sql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import jam.sql.SQLColumn;
import jam.sql.SQLDb;
import jam.sql.SQLKeyTable;
import jam.util.ListUtil;

import pubmed.mesh.MeshDB;
import pubmed.mesh.MeshRecord;
import pubmed.mesh.MeshRecordKey;
import pubmed.mesh.MeshRecordName;
import pubmed.mesh.MeshRecordType;

/**
 * Maintains the {@code mesh_concepts} table, which contains the key
 * and name of all {@code MeSH} <em>concept</em> records: descriptors,
 * qualifiers, and supplemental records.
 */
public final class MeshConceptTable extends SQLKeyTable<MeshRecordKey, MeshConceptRecord> {
    private static MeshConceptTable instance = null;

    private MeshConceptTable() {
        super(DbEnv.activeDb());
    }

    /**
     * The name of the {@code pubmed.mesh_concepts} table.
     */
    public static final String TABLE_NAME = "mesh_concepts";

    /**
     * The name of the {@code mesh_key} column.
     */
    public static final String MESH_KEY = "mesh_key";

    /**
     * The name of the {@code mesh_name} column.
     */
    public static final String MESH_NAME = "mesh_name";

    /**
     * The name of the {@code mesh_type} column.
     */
    public static final String MESH_TYPE = "mesh_type";

    /**
     * Meta-data for the {@code mesh_key} column.
     */
    public static final SQLColumn KEY_COLUMN =
        SQLColumn.create(MESH_KEY, "text")
        .primaryKey();

    /**
     * Meta-data for the {@code mesh_name} column.
     */
    public static final SQLColumn NAME_COLUMN =
        SQLColumn.create(MESH_NAME, "text")
        .unique()
        .notNull();

    /**
     * Meta-data for the {@code mesh_type} column.
     */
    public static final SQLColumn TYPE_COLUMN =
        SQLColumn.create(MESH_TYPE, "mesh_type_enum")
        .notNull()
        .withIndex();

    private static final List<SQLColumn> COLUMN_LIST =
        List.of(KEY_COLUMN, NAME_COLUMN, TYPE_COLUMN);

    /**
     * Returns the single instance.
     *
     * @return the single instance.
     */
    public static synchronized MeshConceptTable instance() {
        if (instance == null)
            instance = new MeshConceptTable();

        return instance;
    }

    /**
     * Populates the database table with all known {@code MeSH}
     * records unless the table already exists.
     *
     * @param db the active database manager.
     *
     * @throws RuntimeException if any errors occur.
     */
    public static synchronized void populate(SQLDb db) {
        if (db.tableExists(TABLE_NAME))
            return;
        
        MeshDB.load();

        List<MeshRecord> meshRecords = MeshRecord.list();
        meshRecords.sort(MeshRecord.KEY_COMPARATOR);

        List<MeshConceptRecord> conceptRecords =
            ListUtil.apply(meshRecords, x -> MeshConceptRecord.create(x));

        db.createEnum("mesh_type_enum", MeshRecordType.class);
        db.createTable(TABLE_NAME, COLUMN_LIST);
        db.bulkCopy(TABLE_NAME, conceptRecords);
    }

    @Override public List<SQLColumn> getColumns() {
        return COLUMN_LIST;
    }

    @Override public MeshRecordKey getKey(MeshConceptRecord record) {
        return record.getKey();
    }

    @Override public MeshRecordKey getKey(ResultSet resultSet, String columnLabel) throws SQLException {
        return MeshRecordKey.instance(getString(resultSet, columnLabel));
    }

    @Override public MeshConceptRecord getRow(ResultSet resultSet) throws SQLException {
        MeshRecordKey key = MeshRecordKey.instance(getString(resultSet, MESH_KEY));
        MeshRecordType type = MeshRecordType.valueOf(getString(resultSet, MESH_TYPE));
        MeshRecordName name = MeshRecordName.instance(getString(resultSet, MESH_NAME), type);

        return MeshConceptRecord.create(key, name, type);
    }

    @Override public String getTableName() {
        return TABLE_NAME;
    }

    @Override public void prepareColumn(PreparedStatement statement, int index,
                                        MeshConceptRecord record, String columnName) throws SQLException {
        throw new UnsupportedOperationException("The table must be populated with a bulk copy.");
        /*
        setString(statement, 1, record.getKeyString());
        setString(statement, 2, record.getNameString());
        statement.setObject(3, record.getTypeString(), Types.OTHER);
        */
    }

    @Override public void prepareKey(PreparedStatement statement, int index, MeshRecordKey key) throws SQLException {
        setString(statement, index, key.getKey());
    }

    private static void usage() {
        System.err.println("Usage pubmed.sql.MeshConceptTable PROD | TEST");
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
