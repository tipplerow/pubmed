
package pubmed.mesh;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jam.lang.JamException;
import jam.util.MapUtil;

/**
 * Provides a base class for all {@code MeSH} records.
 */
public abstract class MeshRecord {
    private final String note;
    private final MeshRecordKey key;
    private final MeshRecordName name;
    private final MeshTermList termStrings;
    private final List<MeshTerm> termObjects;

    private static final Map<MeshRecordKey, MeshRecord> keyIndex =
        new HashMap<MeshRecordKey, MeshRecord>();

    private static final Map<MeshRecordName, MeshRecord> nameIndex =
        new HashMap<MeshRecordName, MeshRecord>();

    /**
     * Creates a new {@code MeSH} record and indexes it by key and
     * name.
     *
     * @param key the unique key for the record.
     *
     * @param name the common name for the record.
     *
     * @param note the note associated with the record in the XML file.
     *
     * @param termStrings the keywords and phrases associated with the
     * record.
     */
    protected MeshRecord(MeshRecordKey key, MeshRecordName name, String note, MeshTermList termStrings) {
        this.key = key;
        this.name = name;
        this.note = note;

        this.termStrings = termStrings;
        this.termObjects = createTerms();

        mapRecord(this);
    }

    private List<MeshTerm> createTerms() {
        List<MeshTerm> objects =
            new ArrayList<MeshTerm>(termStrings.size());

        objects.add(MeshTerm.create(termStrings.getPreferred(), true, key));

        for (String term : termStrings.getAlternate())
            objects.add(MeshTerm.create(term, false, key));

        return Collections.unmodifiableList(objects);
    }

    private static synchronized void mapRecord(MeshRecord record) {
        MapUtil.putUnique(keyIndex, record.key, record);
        MapUtil.putUnique(nameIndex, record.name, record);
    }

    /**
     * Orders records by their unique key.
     */
    public static final Comparator<MeshRecord> KEY_COMPARATOR =
        new Comparator<MeshRecord>() {
            @Override public int compare(MeshRecord rec1, MeshRecord rec2) {
                return rec1.getKeyString().compareTo(rec2.getKeyString());
            }
        };

    /**
     * Orders records by their common name.
     */
    public static final Comparator<MeshRecord> NAME_COMPARATOR =
        new Comparator<MeshRecord>() {
            @Override public int compare(MeshRecord rec1, MeshRecord rec2) {
                return rec1.getNameString().compareTo(rec2.getNameString());
            }
        };

    /**
     * Identifies previously created records.
     *
     * @param key the unique identifier for the record.
     *
     * @return {@code true} iff a record with the specified key was
     * previously created and resides in the registry.
     */
    public static boolean exists(MeshRecordKey key) {
        return keyIndex.containsKey(key);
    }

    /**
     * Identifies previously created records.
     *
     * @param name the common name for the record.
     *
     * @return {@code true} iff a record with the specified name was
     * previously created and resides in the registry.
     */
    public static boolean exists(MeshRecordName name) {
        return nameIndex.containsKey(name);
    }

    /**
     * Returns the previously created record with a given key.
     *
     * @param key the unique identifier for the record.
     *
     * @return the desired record.
     *
     * @throws RuntimeException unless the requested record exists.
     */
    public static MeshRecord instance(MeshRecordKey key) {
        MeshRecord record = lookup(key);

        if (record != null)
            return record;
        else
            throw JamException.runtime("Missing record: [%s].", key);
    }

    /**
     * Returns the previously created record with a given name.
     *
     * @param name the unique identifier for the record.
     *
     * @return the desired record.
     *
     * @throws RuntimeException unless the requested record exists.
     */
    public static MeshRecord instance(MeshRecordName name) {
        MeshRecord record = lookup(name);

        if (record != null)
            return record;
        else
            throw JamException.runtime("Missing record: [%s].", name);
    }

    /**
     * Returns a new list of all records (in no particular order).
     *
     * @return a new list of all records (in no particular order).
     */
    public static List<MeshRecord> list() {
        return new ArrayList<MeshRecord>(keyIndex.values());
    }

    /**
     * Returns the previously created record with a given key.
     *
     * @param key the unique identifier for the record.
     *
     * @return the desired record (or {@code null} if no matching
     * record exists).
     */
    public static MeshRecord lookup(MeshRecordKey key) {
        return keyIndex.get(key);
    }

    /**
     * Returns the previously created record with a given name.
     *
     * @param name the unique identifier for the record.
     *
     * @return the desired record (or {@code null} if no matching
     * record exists).
     */
    public static MeshRecord lookup(MeshRecordName name) {
        return nameIndex.get(name);
    }

    /**
     * Returns the previously created record with a given keyword or
     * phrase (which may be the record key).
     *
     * @param keyword the identifying keyword or phrase (or key).
     *
     * @return the desired record (or {@code null} if no matching
     * record exists).
     */
    public static MeshRecord lookup(String keyword) {
        MeshTerm term = MeshTerm.lookup(keyword);

        if (term != null)
            return lookup(term.getRecordKey());
        else
            return lookup(MeshRecordKey.instance(keyword));
    }

    /**
     * Returns the enumerated type of this record.
     *
     * @return the enumerated type of this record.
     */
    public abstract MeshRecordType getType();

    /**
     * Returns the unique identifier for this record.
     *
     * @return the unique identifier for this record.
     */
    public MeshRecordKey getKey() {
        return key;
    }

    /**
     * Returns the unique identifier for this record.
     *
     * @return the unique identifier for this record.
     */
    public String getKeyString() {
        return key.getKey();
    }

    /**
     * Returns the common name of this record.
     *
     * @return the common name of this record.
     */
    public MeshRecordName getName() {
        return name;
    }

    /**
     * Returns the common name of this record.
     *
     * @return the common name of this record.
     */
    public String getNameString() {
        return name.getName();
    }

    /**
     * Returns the note that accompanied this record in the XML
     * document.
     *
     * @return the note that accompanied this record in the XML
     * document.
     */
    public String getNote() {
        return note;
    }

    /**
     * Returns the keywords and phrases associated with this record in
     * a list of {@code MeshTerm} objects.
     *
     * @return the keywords and phrases associated with this record in
     * a list of {@code MeshTerm} objects.
     */
    public List<MeshTerm> termObjects() {
        return termObjects;
    }

    /**
     * Returns the keywords and phrases associated with this record.
     *
     * @return the keywords and phrases associated with this record.
     */
    public MeshTermList termStrings() {
        return termStrings;
    }

    @Override public boolean equals(Object obj) {
        return this.getClass().equals(obj.getClass()) && equalsRecord((MeshRecord) obj);
    }

    private boolean equalsRecord(MeshRecord that) {
        return this.key.equals(that.key)
            && this.name.equals(that.name)
            && this.termStrings.equals(that.termStrings);
    }

    @Override public int hashCode() {
        return key.hashCode();
    }

    @Override public String toString() {
        return String.format("%s(%s|%s|%s)", getClass().getSimpleName(), getKeyString(), getNameString(), termStrings.join());
    }
}
