
package pubmed.sql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import jam.sql.SQLColumn;
import jam.sql.SQLKeyTable;

import pubmed.medline.ISSN;
import pubmed.medline.MedlineJournal;
import pubmed.medline.MedlineTA;
import pubmed.medline.NlmUniqueID;

/**
 * Maintains the {@code pubmed.journals} table.
 */
public final class JournalTable extends SQLKeyTable<MedlineTA, MedlineJournal> implements PubmedResource {
    private static JournalTable instance = null;

    private JournalTable() {
        super(DbManager.instance());
    }

    /**
     * The name of the {@code pubmed.journals} table.
     */
    public static final String TABLE_NAME = "journals";

    /**
     * The name of the {@code medline_ta} column.
     */
    public static final String MED_TA_NAME = "medline_ta";

    /**
     * The name of the {@code nlm_id} column.
     */
    public static final String NLM_ID_NAME = "nlm_id";

    /**
     * The name of the {@code issn} column.
     */
    public static final String ISSN_NAME = "issn";

    /**
     * The name of the {@code country} column.
     */
    public static final String COUNTRY_NAME = "country";

    /**
     * Meta-data for the {@code medline_ta} column.
     */
    public static final SQLColumn MED_TA_COLUMN =
        SQLColumn.create(MED_TA_NAME, "text")
        .primaryKey();

    /**
     * Meta-data for the {@code nlm_id} column.
     */
    public static final SQLColumn NLM_ID_COLUMN =
        SQLColumn.create(NLM_ID_NAME, "text")
        .withIndex();

    /**
     * Meta-data for the {@code issn} column.
     */
    public static final SQLColumn ISSN_COLUMN =
        SQLColumn.create(ISSN_NAME, "text")
        .withIndex();

    /**
     * Meta-data for the {@code country} column.
     */
    public static final SQLColumn COUNTRY_COLUMN =
        SQLColumn.create(COUNTRY_NAME, "text")
        .withIndex();

    /**
     * Meta-data for the table columns.
     */
    public static final List<SQLColumn> COLUMN_LIST =
        List.of(MED_TA_COLUMN, NLM_ID_COLUMN, ISSN_COLUMN, COUNTRY_COLUMN);

    /**
     * Returns the single instance.
     *
     * @return the single instance.
     */
    public static synchronized JournalTable instance() {
        if (instance == null)
            instance = new JournalTable();

        createTable();
        return instance;
    }

    /**
     * Creates the physical database table (unless it already exists).
     */
    public static synchronized void createTable() {
        DbManager.instance().createTable(TABLE_NAME, COLUMN_LIST);
    }

    /**
     * Drops the physical database table.
     */
    public static synchronized void dropTable() {
        DbManager.instance().dropTable(TABLE_NAME);
    }

    /**
     * Adds a collection of journals to this table.
     *
     * @param journals the journals to add.
     *
     * @throws RuntimeException if any journals are already contained
     * in the database table.
     */
    public void insert(Collection<MedlineJournal> journals) {
        db().bulkCopy(TABLE_NAME, journals);
    }

    @Override public List<SQLColumn> getColumns() {
        return COLUMN_LIST;
    }

    @Override public MedlineTA getKey(MedlineJournal journal) {
        return journal.getMedlineTA();
    }

    @Override public MedlineTA getKey(ResultSet resultSet, String columnLabel) throws SQLException {
        return getMedlineTA(resultSet, columnLabel);
    }

    @Override public MedlineJournal getRow(ResultSet resultSet) throws SQLException {
        ISSN issn = getISSN(resultSet, ISSN_NAME);
        String country = getString(resultSet, COUNTRY_NAME);
        MedlineTA medlineTA = getMedlineTA(resultSet, MED_TA_NAME);
        NlmUniqueID nlmUniqueID = getNlmUniqueID(resultSet, NLM_ID_NAME);

        return MedlineJournal.create(medlineTA, nlmUniqueID, issn, country);
    }

    @Override public String getTableName() {
        return TABLE_NAME;
    }

    @Override public void prepareColumn(PreparedStatement statement, int index,
                                        MedlineJournal journal, String columnName) throws SQLException {
        switch (columnName) {
        case MED_TA_NAME:
            setKeyedObject(statement, index, journal.getMedlineTA());
            break;

        case NLM_ID_NAME:
            setKeyedObject(statement, index, journal.getNlmUniqueID());
            break;

        case ISSN_NAME:
            setKeyedObject(statement, index, journal.getISSN());
            break;

        case COUNTRY_NAME:
            setString(statement, index, journal.getCountry());
            break;

        default:
            throw invalidColumn(columnName);
        }
    }

    @Override public void prepareKey(PreparedStatement statement, int index, MedlineTA ta) throws SQLException {
        setString(statement, index, ta.getKey());
    }
}
