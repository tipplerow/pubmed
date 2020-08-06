
package pubmed.flat;

import jam.flat.FlatRecord;
import jam.lang.ObjectUtil;

import pubmed.article.ISSN;
import pubmed.article.PMID;
import pubmed.mesh.MeshDescriptorKey;
import pubmed.mesh.MeshQualifierKey;
import pubmed.mesh.MeshRecordKey;

/**
 * Provides a base class for data rows containing article attributes
 * keyed by {@code PMID}.
 */
public abstract class PubmedFlatRecord implements FlatRecord<PMID>, FlatRecordBase {
    /**
     * The {@code PubMed} identifier for the article.
     */
    protected final PMID pmid;

    /**
     * Creates a new record with a fixed primary key.
     *
     * @param pmid the primary key for the record.
     */
    protected PubmedFlatRecord(PMID pmid) {
        this.pmid = pmid;
        validatePMID();
    }

    private void validatePMID() {
        if (pmid == null)
            throw new NullPointerException("Null article identifier.");
    }

    /**
     * String used to identify {@code null} or missing qualifiers.
     */
    public static final String MISSING_QUALIFIER = "-";

    /**
     * Determines whether the data (non-key) fields in another record
     * are identical to those in this record.
     *
     * <p>The overriding method may assume that the input record can
     * be cast to the same runtime type as the implementing subclass.
     *
     * @param record the record to test for equality.
     *
     * @return {@code true} iff the data (non-key) fields in the input
     * record are identical to those in this record.
     */
    public abstract boolean equalsData(Object record);

    /**
     * Formats a qualifier key.
     *
     * @param qualKey the qualifier key to format.
     *
     * @return the formatted key.
     */
    public String formatQualifierKey(MeshQualifierKey qualKey) {
        if (qualKey != null)
            return format(qualKey);
        else
            return MISSING_QUALIFIER;
    }

    /**
     * Parses the descriptor key encoded in a string field.
     *
     * @param field the field to parse.
     *
     * @return the descriptor key encoded in the string field.
     */
    public static MeshDescriptorKey parseDescriptorKey(String field) {
        if (FlatRecord.isNull(field))
            return null;
        else
            return MeshDescriptorKey.instance(field);
    }

    /**
     * Parses the journal identifier encoded in a string field.
     *
     * @param field the field to parse.
     *
     * @return the journal identifier encoded in the string field.
     */
    public static ISSN parseISSN(String field) {
        if (FlatRecord.isNull(field))
            return null;
        else
            return ISSN.instance(field);
    }

    /**
     * Parses the article identifier encoded in a string field.
     *
     * @param field the field to parse.
     *
     * @return the article identifier encoded in the string field.
     */
    public static PMID parsePMID(String field) {
        if (FlatRecord.isNull(field))
            return null;
        else
            return PMID.instance(field);
    }

    /**
     * Parses the qualifier key encoded in a string field.
     *
     * @param field the field to parse.
     *
     * @return the qualifier key encoded in the string field.
     */
    public static MeshQualifierKey parseQualifierKey(String field) {
        if (FlatRecord.isNull(field) || field.equals(MISSING_QUALIFIER))
            return null;
        else
            return MeshQualifierKey.instance(field);
    }

    /**
     * Parses the record key encoded in a string field.
     *
     * @param field the field to parse.
     *
     * @return the record key encoded in the string field.
     */
    public static MeshRecordKey parseRecordKey(String field) {
        if (FlatRecord.isNull(field))
            return null;
        else
            return MeshRecordKey.instance(field);
    }

    /**
     * Returns the {@code PubMed} identifier for the article.
     *
     * @return the {@code PubMed} identifier for the article.
     */
    public PMID getPMID() {
        return pmid;
    }

    @Override public PMID getPrimaryKey() {
        return pmid;
    }

    @Override public boolean equals(Object obj) {
        return ObjectUtil.equalsClass(this, obj) && equalsRecord((PubmedFlatRecord) obj);
    }

    private boolean equalsRecord(PubmedFlatRecord that) {
        return this.pmid.equals(that.pmid) && equalsData(that);
    }

    @Override public int hashCode() {
        return pmid.intValue();
    }

    @Override public String toString() {
        return String.format("%s(%s)", getClass().getSimpleName(), pmid.toString());
    }
}
