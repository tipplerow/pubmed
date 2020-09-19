
package pubmed.sql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import jam.sql.SQLColumn;
import jam.sql.SQLDb;
import jam.sql.SQLKeyTable;

import pubmed.mesh.MeshDB;
import pubmed.mesh.MeshRecord;
import pubmed.mesh.MeshRecordKey;

/**
 * Maintains the {@code mesh_notes} table, which contains the key and
 * note or scope note for the {@code MeSH} record.
 */
public final class MeshNoteTable extends SQLKeyTable<MeshRecordKey, MeshNoteRecord> {
    private static MeshNoteTable instance = null;

    private MeshNoteTable() {
        super(DbEnv.activeDb());
    }

    /**
     * The name of the {@code pubmed.mesh_notes} table.
     */
    public static final String TABLE_NAME = "mesh_notes";

    /**
     * The name of the {@code mesh_key} column.
     */
    public static final String MESH_KEY = "mesh_key";

    /**
     * The name of the {@code mesh_note} column.
     */
    public static final String MESH_NOTE = "mesh_note";

    /**
     * Meta-data for the {@code mesh_key} column.
     */
    public static final SQLColumn KEY_COLUMN =
        SQLColumn.create(MESH_KEY, "text")
        .primaryKey();

    /**
     * Meta-data for the {@code mesh_note} column.
     */
    public static final SQLColumn NOTE_COLUMN =
        SQLColumn.create(MESH_NOTE, "text")
        .notNull();

    private static final List<SQLColumn> COLUMN_LIST =
        List.of(KEY_COLUMN, NOTE_COLUMN);

    /**
     * Returns the single instance.
     *
     * @return the single instance.
     */
    public static synchronized MeshNoteTable instance() {
        if (instance == null)
            instance = new MeshNoteTable();

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

        List<MeshNoteRecord> noteRecords =
            new ArrayList<MeshNoteRecord>();

        for (MeshRecord meshRecord : meshRecords)
            if (meshRecord.getNote() != null)
                noteRecords.add(MeshNoteRecord.create(meshRecord));

        db.createTable(TABLE_NAME, COLUMN_LIST);
        db.bulkCopy(TABLE_NAME, noteRecords);
    }

    @Override public List<SQLColumn> getColumns() {
        return COLUMN_LIST;
    }

    @Override public MeshRecordKey getKey(MeshNoteRecord record) {
        return record.getKey();
    }

    @Override public MeshRecordKey getKey(ResultSet resultSet, String columnLabel) throws SQLException {
        return MeshRecordKey.instance(getString(resultSet, columnLabel));
    }

    @Override public MeshNoteRecord getRow(ResultSet resultSet) throws SQLException {
        MeshRecordKey key  = MeshRecordKey.instance(getString(resultSet, MESH_KEY));
        String        note = getString(resultSet, MESH_NOTE);

        return MeshNoteRecord.create(key, note);
    }

    @Override public String getTableName() {
        return TABLE_NAME;
    }

    @Override public void prepareColumn(PreparedStatement statement, int index,
                                        MeshNoteRecord record, String columnName) throws SQLException {
        throw new UnsupportedOperationException("The table must be populated with a bulk copy.");
    }

    @Override public void prepareKey(PreparedStatement statement, int index, MeshRecordKey key) throws SQLException {
        setString(statement, index, key.getKey());
    }

    private static void usage() {
        System.err.println("Usage pubmed.sql.MeshNoteTable PROD | TEST");
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
