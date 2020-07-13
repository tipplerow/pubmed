
package pubmed.flat;

import jam.flat.FlatRecord;

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
        if (FlatRecord.isNull(field))
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

    @Override public int hashCode() {
        return pmid.intValue();
    }
}
