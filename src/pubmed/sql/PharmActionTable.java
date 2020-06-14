
package pubmed.sql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import jam.sql.SQLDb;
import jam.sql.SQLPairTable;

import pubmed.mesh.MeshDB;
import pubmed.mesh.MeshDescriptorKey;
import pubmed.mesh.MeshRecordKey;

/**
 * Maintains the {@code pubmed.pharm_actions} table, which contains
 * substances and their known pharmacological actions.
 */
public final class PharmActionTable
    extends SQLPairTable<MeshRecordKey, MeshDescriptorKey, PharmActionRecord> implements PubmedResource {

    private static PharmActionTable instance = null;

    private PharmActionTable() {
        this(DbEnv.activeDb());
    }

    private PharmActionTable(SQLDb db) {
        super(db);
    }

    /**
     * The name of the {@code pubmed.pharm_actions} table.
     */
    public static final String TABLE_NAME = "pharm_actions";

    /**
     * The name of the {@code substance_key} column.
     */
    public static final String SUBSTANCE_NAME = "substance_key";

    /**
     * The type of the {@code substance_key} column.
     */
    public static final String SUBSTANCE_TYPE = "text";

    /**
     * The name of the {@code action_key} column.
     */
    public static final String ACTION_NAME = "action_key";

    /**
     * The type of the {@code action_key} column.
     */
    public static final String ACTION_TYPE = "text";

    /**
     * Returns the single instance of the {@code pubmed.pharm_actions}
     * table.
     *
     * @return the single instance of the {@code pubmed.pharm_actions}
     * table.
     */
    public static synchronized PharmActionTable instance() {
        if (instance == null)
            instance = new PharmActionTable();

        instance.require();
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
        PharmActionTable table = new PharmActionTable(db);

        if (table.exists())
            return;
        else
            table.require();
        
        MeshDB.load();
        db.bulkCopy(TABLE_NAME, PharmActionRecord.list());
    }

    @Override public String getKey1Name() {
        return SUBSTANCE_NAME;
    }

    @Override public String getKey1Type() {
        return SUBSTANCE_TYPE;
    }

    @Override public String getKey2Name() {
        return ACTION_NAME;
    }

    @Override public String getKey2Type() {
        return ACTION_TYPE;
    }

    @Override public PharmActionRecord getRecord(ResultSet resultSet) throws SQLException {
        MeshRecordKey substanceKey = getRecordKey(resultSet, SUBSTANCE_NAME);
        MeshDescriptorKey actionKey = getDescriptorKey(resultSet, ACTION_NAME);

        return PharmActionRecord.create(substanceKey, actionKey);
    }

    @Override public String getTableName() {
        return TABLE_NAME;
    }

    @Override public void setKey1(PreparedStatement statement, int index, MeshRecordKey key) throws SQLException {
        setKeyedObject(statement, index, key);
    }

    @Override public void setKey2(PreparedStatement statement, int index, MeshDescriptorKey key) throws SQLException {
        setKeyedObject(statement, index, key);
    }

    private static void usage() {
        System.err.println("Usage pubmed.sql.PharmActionTable PROD | TEST");
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
