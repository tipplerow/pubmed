
package pubmed.sql;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import jam.lang.JamException;
import jam.sql.QueryResult;
import jam.sql.SQLColumn;
import jam.sql.SQLDb;
import jam.sql.SQLSchema;

/**
 * Maintains the {@code process_history} tables, which stores the time
 * at which each {@code pubmed} database table was populated with data
 * from a given bulk file.
 */
public final class ProcessHistoryTable {
    private final SQLDb db;

    private static ProcessHistoryTable instance = null;

    private ProcessHistoryTable() {
        this.db = DbEnv.activeDb();
    }

    /**
     * The name of the {@code pubmed.process_history} table.
     */
    public static final String TABLE_NAME = "process_history";

    /**
     * The name of the {@code bulk_file} column.
     */
    public static final String BULK_FILE = "bulk_file";

    /**
     * The name of the {@code pubmed_table} column.
     */
    public static final String PUBMED_TABLE = "pubmed_table";

    /**
     * The name of the {@code time_stamp} column.
     */
    public static final String TIME_STAMP = "time_stamp";

    /**
     * Meta-data for the {@code bulk_file} column.
     */
    public static final SQLColumn BULK_FILE_COLUMN =
        SQLColumn.create(BULK_FILE, "text")
        .compositeKey();

    /**
     * Meta-data for the {@code pubmed_table} column.
     */
    public static final SQLColumn PUBMED_TABLE_COLUMN =
        SQLColumn.create(PUBMED_TABLE, "text")
        .compositeKey();

    /**
     * Meta-data for the {@code time_stamp} column.
     */
    public static final SQLColumn TIME_STAMP_COLUMN =
        SQLColumn.create(TIME_STAMP, "timestamp")
        .notNull();

    /**
     * Meta-data for the table columns.
     */
    public static final List<SQLColumn> COLUMN_LIST =
        List.of(BULK_FILE_COLUMN, PUBMED_TABLE_COLUMN, TIME_STAMP_COLUMN);

    /**
     * The complete table schema.
     */
    public static final SQLSchema SCHEMA =
        SQLSchema.create(TABLE_NAME, COLUMN_LIST);

    /**
     * Returns the single history table.
     *
     * @return the single history table.
     */
    public static synchronized ProcessHistoryTable instance() {
        if (instance == null) {
            createTable();
            instance = new ProcessHistoryTable();
        }

        return instance;
    }

    /**
     * Creates the physical database table (unless it already exists).
     */
    public static synchronized void createTable() {
        SCHEMA.createTable(DbEnv.activeDb());
    }

    /**
     * Drops the physical database table.
     */
    public static synchronized void dropTable() {
        DbEnv.activeDb().dropTable(TABLE_NAME);
    }

    /**
     * Returns the names of the tables that have been populated with
     * data from a particular bulk file.
     *
     * @param bulkFile the bulk file in question.
     *
     * @return a set containing the names of all tables that have been
     * populated with data from the specified bulk file.
     */
    public Set<String> fetchProcessedTables(File bulkFile) {
        String queryStr = formatFetchProcessedTables(bulkFile);

        try (QueryResult queryResult = db.executeQuery(queryStr)) {
            return fetchProcessedTables(queryResult.getResultSet());
        }
        catch (SQLException ex) {
            throw JamException.runtime(ex);
        }
    }

    private String formatFetchProcessedTables(File bulkFile) {
        return String.format("SELECT %s FROM %s WHERE %s = '%s'",
                             PUBMED_TABLE, TABLE_NAME, BULK_FILE, formatFile(bulkFile));
    }

    private String formatFile(File bulkFile) {
        try {
            return bulkFile.getCanonicalPath();
        }
        catch (IOException ex) {
            throw JamException.runtime(ex);
        }
    }

    private Set<String> fetchProcessedTables(ResultSet resultSet) throws SQLException {
        Set<String> processed = new TreeSet<String>();

        while (resultSet.next())
            processed.add(fetchProcessedTable(resultSet));

        return processed;
    }

    private String fetchProcessedTable(ResultSet resultSet) throws SQLException {
        return resultSet.getString(PUBMED_TABLE);
    }

    /**
     * Notes that a table has been successfully populated (just this
     * instant) with data from a given bulk file.
     *
     * @param bulkFile the bulk file that was processed.
     *
     * @param pubmedTable the name of the table that was populated.
     */
    public void markAsProcessed(File bulkFile, String pubmedTable) {
        String updateStr = formatMarkAsProcessed(bulkFile, pubmedTable);
        db.executeUpdate(updateStr);
    }

    private String formatMarkAsProcessed(File bulkFile, String pubmedTable) {
        return String.format("INSERT INTO %s VALUES('%s', '%s', '%s')",
                             TABLE_NAME,
                             formatFile(bulkFile),
                             pubmedTable,
                             LocalDateTime.now().toString());
    }
}
