
package pubmed.mesh;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jam.lang.JamException;
import jam.sql.BulkRecord;
import jam.util.MapUtil;

/**
 * Represents a unique keyword or phrase in the {@code MeSH} taxonomy.
 */
public final class MeshTerm implements BulkRecord, Comparable<MeshTerm> {
    private final String term;
    private final boolean preferred;
    private final MeshRecordKey recordKey;

    private static final Map<String, MeshTerm> instances =
        new HashMap<String, MeshTerm>();

    private MeshTerm(String term, boolean preferred, MeshRecordKey recordKey) {
        this.term = term;
        this.preferred = preferred;
        this.recordKey = recordKey;

        mapTerm(this);
    }

    private static synchronized void mapTerm(MeshTerm termObj) {
        MapUtil.putUnique(instances, termObj.getTerm(), termObj);
    }

    /**
     * Creates a {@code MeshTerm} object with a fixed term and record
     * key.
     *
     * @param term the term keyword or phrase.
     *
     * @param preferred whether the term is the preferred one for its
     * containing record.
     *
     * @param recordKey the key of the record that contains the term.
     *
     * @return a new {@code MeshTerm} object with the specified term
     * and record key.
     */
    public static MeshTerm create(String term,
                                  boolean preferred,
                                  MeshRecordKey recordKey) {
        return new MeshTerm(term, preferred, recordKey);
    }

    /**
     * Returns the {@code MeshTerm} object with a given keyword or
     * phrase.
     *
     * @param term the term keyword or phrase.
     *
     * @return the {@code MeshTerm} object with the given keyword or
     * phrase.
     *
     * @throws RuntimeException if there is no matching term).
     */
    public static MeshTerm instance(String term) {
        MeshTerm instance = lookup(term);

        if (instance != null)
            return instance;
        else
            throw JamException.runtime("Missing term: [%s].", term);
    }

    /**
     * Returns a new list of all known terms (in no particular order).
     *
     * @return a new list of all known terms (in no particular order).
     */
    public static List<MeshTerm> list() {
        return new ArrayList<MeshTerm>(instances.values());
    }

    /**
     * Returns the {@code MeshTerm} object with a given keyword or
     * phrase.
     *
     * @param term the term keyword or phrase.
     *
     * @return the {@code MeshTerm} object with the given keyword or
     * phrase ({@code null} if there is no matching term).
     */
    public static MeshTerm lookup(String term) {
        return instances.get(term);
    }

    /**
     * Returns the keyword or phrase for this term.
     *
     * @return the keyword or phrase for this term.
     */
    public String getTerm() {
        return term;
    }

    /**
     * Returns the key of the record that contains this term.
     *
     * @return the key of the record that contains this term.
     */
    public MeshRecordKey getRecordKey() {
        return recordKey;
    }

    /**
     * Identifies preferred terms (as opposed to synonyms).
     *
     * @return {@code true} iff this term is the preferred one for its
     * containing record.
     */
    public boolean isPreferred() {
        return preferred;
    }

    @Override public String formatBulk() {
        return joinBulk(formatBulk(term), formatBulk(recordKey), formatBulk(preferred));
    }

    @Override public int compareTo(MeshTerm that) {
        int cmp = this.term.compareTo(that.term);

        if (cmp != 0)
            return cmp;

        cmp = this.recordKey.compareTo(that.recordKey);

        if (cmp != 0)
            return cmp;
        else
            return Boolean.compare(this.preferred, that.preferred);
    }

    @Override public boolean equals(Object obj) {
        return (obj instanceof MeshTerm) && equalsTerm((MeshTerm) obj);
    }

    private boolean equalsTerm(MeshTerm that) {
        return this.term.equals(that.term)
            && this.recordKey.equals(that.recordKey)
            && this.preferred == that.preferred;
    }

    @Override public int hashCode() {
        return term.hashCode();
    }

    @Override public String toString() {
        return String.format("MeshTerm(%s, %s, %s)", term, recordKey.getKey(), Boolean.toString(preferred));
    }
}
