
package pubmed.article;

import jam.sql.BulkRecord;

/**
 * Represents the unique identifier for all {@code PubMed} records.
 */
public final class PMID implements BulkRecord, Comparable<PMID> {
    private final int id;

    private PMID(int id) {
        validateID(id);
        this.id = id;
    }

    private static void validateID(int id) {
        if (id < 1)
            throw new IllegalArgumentException("PMIDs must be positive.");
    }

    /**
     * The prefix required to form a URL from a DOI.
     */
    public static final String URL_PREFIX = "https://pubmed.ncbi.nlm.nih.gov/";

    /**
     * Returns the {@code PMID} identifer for a given integer value.
     *
     * @param id the integer key value.
     *
     * @return the {@code PMID} identifer for the given integer value.
     *
     * @throws RuntimeException unless the integer value is positive.
     */
    public static PMID instance(int id) {
        return new PMID(id);
    }

    /**
     * Returns the {@code PMID} identifer for a given string value.
     *
     * @param id the string key value.
     *
     * @return the {@code PMID} identifer for the given string value.
     *
     * @throws RuntimeException unless the string represents a valid
     * identifier.
     */
    public static PMID instance(String id) {
        return instance(Integer.parseInt(id));
    }

    /**
     * Returns the URL for this identifier.
     *
     * @return the URL for this identifier.
     */
    public String asURL() {
        return URL_PREFIX + Integer.toString(id);
    }

    /**
     * Returns the integer value of this identifier.
     *
     * @return the integer value of this identifier.
     */
    public int intValue() {
        return id;
    }

    @Override public String formatBulk() {
	return Integer.toString(id);
    }

    @Override public int compareTo(PMID that) {
        return Integer.compare(this.id, that.id);
    }

    @Override public boolean equals(Object obj) {
        return (obj instanceof PMID) && equalsPMID((PMID) obj);
    }

    private boolean equalsPMID(PMID that) {
        return this.id == that.id;
    }

    @Override public int hashCode() {
        return id;
    }

    @Override public String toString() {
        return String.format("PMID(%d)", id);
    }
}
