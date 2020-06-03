
package pubmed.sql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;

import jam.lang.JamException;
import jam.sql.SQLColumn;

import pubmed.article.DOI;
import pubmed.article.PMID;
import pubmed.article.PubmedDate;
import pubmed.medline.MedlineTA;
import pubmed.medline.NlmUniqueID;
import pubmed.medline.ISSN;
import pubmed.mesh.MeshDescriptorKey;
import pubmed.mesh.MeshRecordKey;

/**
 * Provides shared functionality for tables and records in the
 * {@code pubmed} database.
 */
public interface PubmedResource {
    /**
     * The name for the {@code pmid} column.
     */
    public static final String PMID_NAME = "pmid";

    /**
     * The PostgreSQL type for the {@code pmid} column.
     */
    public static final String PMID_TYPE = "integer";

    /**
     * The column descriptor for the {@code PMID} primary key.
     */
    public static final SQLColumn PMID_PRIMARY_KEY_COLUMN =
        SQLColumn.create(PMID_NAME, PMID_TYPE)
        .primaryKey();

    /**
     * Extracts a Digital Object Identifier from a result set.
     *
     * @param resultSet an open result set.
     *
     * @param columnName the name of the column containing the
     * identifier.
     *
     * @return the {@code DOI} value stored in the specified column
     * (or {@code null} if the column entry is SQL {@code NULL}).
     *
     * @throws SQLException unless the result set is open and contains
     * a valid identifier in the specified column.
     */
    public default DOI getDOI(ResultSet resultSet, String columnName) throws SQLException {
        String str = resultSet.getString(columnName);

        if (str != null)
            return DOI.instance(str);
        else
            return null;
    }

    /**
     * Extracts the key for a {@code MeSH} descriptor record from a
     * result set.
     *
     * @param resultSet an open result set.
     *
     * @param columnName the name of the column containing the key.
     *
     * @return the {@code MeshDescriptorKey} value stored in the
     * specified column (or {@code null} if the column entry is SQL
     * {@code NULL}).
     *
     * @throws SQLException unless the result set is open and contains
     * a valid key in the specified column.
     */
    public default MeshDescriptorKey getDescriptorKey(ResultSet resultSet, String columnName) throws SQLException {
        String str = resultSet.getString(columnName);

        if (str != null)
            return MeshDescriptorKey.instance(str);
        else
            return null;
    }

    /**
     * Extracts an {@code ISSN} identifier from a result set.
     *
     * @param resultSet an open result set.
     *
     * @param columnName the name of the column containing the
     * ISSN value.
     *
     * @return the ISSN value stored in the specified column (or
     * {@code null} if the column entry is SQL {@code NULL}).
     *
     * @throws SQLException unless the result set is open and contains
     * a valid identifier in the specified column.
     */
    public default ISSN getISSN(ResultSet resultSet, String columnName) throws SQLException {
        String str = resultSet.getString(columnName);

        if (str != null)
            return ISSN.instance(str);
        else
            return null;
    }

    /**
     * Extracts a {@code MEDLINE} title abbreviation from a result
     * set.
     *
     * @param resultSet an open result set.
     *
     * @param columnName the name of the column containing the title
     * abbreviation.
     *
     * @return the title abbreviation value stored in the specified
     * column ({@code null} if the column entry is SQL {@code NULL}).
     *
     * @throws SQLException unless the result set is open and contains
     * a valid abbreviation in the specified column.
     */
    public default MedlineTA getMedlineTA(ResultSet resultSet, String columnName) throws SQLException {
        String str = resultSet.getString(columnName);

        if (str != null)
            return MedlineTA.instance(str);
        else
            return null;
    }

    /**
     * Extracts an {@code NLM} identifier from a result set.
     *
     * @param resultSet an open result set.
     *
     * @param columnName the name of the column containing the
     * identifier.
     *
     * @return the {@code NLM} identifier stored in the specified
     * column ({@code null} if the column entry is SQL {@code NULL}).
     *
     * @throws SQLException unless the result set is open and contains
     * a valid identifier in the specified column.
     */
    public default NlmUniqueID getNlmUniqueID(ResultSet resultSet, String columnName) throws SQLException {
        String str = resultSet.getString(columnName);

        if (str != null)
            return NlmUniqueID.instance(str);
        else
            return null;
    }

    /**
     * Extracts a {@code PubMed} identifier from a result set.
     *
     * @param resultSet an open result set.
     *
     * @param columnName the name of the column containing the
     * identifier.
     *
     * @return the {@code PMID} stored in the specified column (or
     * {@code null} if the column entry is SQL {@code NULL}).
     *
     * @throws SQLException unless the result set is open and contains
     * a valid identifier in the specified column.
     */
    public default PMID getPMID(ResultSet resultSet, String columnName) throws SQLException {
        int id = resultSet.getInt(columnName);

        if (id > 0)
            return PMID.instance(id);
        else
            return null;
    }

    /**
     * Extracts a {@code PubmedDate} from a result set.
     *
     * @param resultSet an open result set.
     *
     * @param columnName the name of the column containing the date.
     *
     * @return the {@code PubmedDate} stored in the specified column
     * (or {@code null} if the column entry is SQL {@code NULL}).
     *
     * @throws SQLException unless the result set is open and contains
     * a valid date in the specified column.
     */
    public default PubmedDate getPubmedDate(ResultSet resultSet, String columnName) throws SQLException {
        LocalDate localDate =
            resultSet.getObject(columnName, LocalDate.class);

        if (localDate != null)
            return PubmedDate.instance(localDate);
        else
            return null;
    }

    /**
     * Extracts the key for a {@code MeSH} descriptor, qualifier, or
     * supplemental record from a result set.
     *
     * @param resultSet an open result set.
     *
     * @param columnName the name of the column containing the key.
     *
     * @return the {@code MeshRecordKey} value stored in the specified
     * column (or {@code null} if the column entry is SQL {@code NULL}).
     *
     * @throws SQLException unless the result set is open and contains
     * a valid key in the specified column.
     */
    public default MeshRecordKey getRecordKey(ResultSet resultSet, String columnName) throws SQLException {
        String str = resultSet.getString(columnName);

        if (str != null)
            return MeshRecordKey.instance(str);
        else
            return null;
    }

    /**
     * Returns the single {@code PMID} from a result set (which is
     * still positioned before the first row).
     *
     * @param resultSet the result set to examine.
     *
     * @param index the index of the {@code PMID} column.
     *
     * @return the single {@code PMID} from the specified result set.
     *
     * @throws RuntimeException unless the result set is positioned
     * before a single {@code PMID}.
     */
    public default PMID getSinglePMID(ResultSet resultSet, int index) {
	try {
	    if (!resultSet.isBeforeFirst())
		throw JamException.runtime("Result set is not positioned before the first row.");

	    resultSet.next();
	    int id = resultSet.getInt(1);

	    if (resultSet.next())
		throw JamException.runtime("Result set contains more than one row.");

	    if (id > 0)
		return PMID.instance(id);
	    else
		return null;
	}
	catch (SQLException ex) {
	    throw JamException.runtime(ex.getMessage());
	}
    }

    /**
     * Assigns a {@code PubMed} identifier to a prepared statement.
     *
     * @param statement the statement to populate.
     *
     * @param index the index of the identifier in the statement.
     *
     * @param pmid the identifier to assign.
     *
     * @throws SQLException unless the statement is open and accepts
     * an identifier in the specified column.
     */
    public default void setPMID(PreparedStatement statement, int index, PMID pmid) throws SQLException {
        if (pmid != null)
            statement.setInt(index, pmid.intValue());
        else
            statement.setNull(index, Types.INTEGER);
    }

    /**
     * Assigns a {@code PubmedDate} to a prepared statement.
     *
     * @param statement the statement to populate.
     *
     * @param index the index of the date in the statement.
     *
     * @param date the date to assign.
     *
     * @throws SQLException unless the statement is open and accepts
     * a date in the specified column.
     */
    public default void setPubmedDate(PreparedStatement statement, int index, PubmedDate date) throws SQLException {
        if (date != null)
            statement.setObject(index, date.getLocalDate());
        else
            statement.setObject(index, null);
    }
}
