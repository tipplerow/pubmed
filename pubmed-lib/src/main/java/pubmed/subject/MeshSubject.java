
package pubmed.subject;

import java.util.HashMap;
import java.util.Map;

import pubmed.mesh.MeshDB;
import pubmed.mesh.MeshRecord;
import pubmed.mesh.MeshRecordKey;

/**
 * Represents a subject that are defined by a unique {@code MeSH}
 * record.
 */
public final class MeshSubject extends Subject {
    private final MeshRecord record;

    // Unique instances indexed by MeshRecord...
    private static final Map<MeshRecord, MeshSubject> instances =
        new HashMap<MeshRecord, MeshSubject>();

    static {
        MeshDB.load();
    }

    private MeshSubject(MeshRecord record) {
        super(record.getKeyString());
        this.record = record;
        addName(record.getNameString());
    }

    /**
     * The <em>neoplasms</em> subject.
     */
    public static final Subject NEOPLASMS = MeshSubject.instance("D009369");

    /**
     * Returns the {@code MeSH} subject for a specific record.
     *
     * @param recordKey the key of the {@code MeSH} record that
     * represents the subject.
     *
     * @return the {@code MeSH} subject defined by the specified
     * record.
     */
    public static MeshSubject instance(String recordKey) {
        return instance(MeshRecordKey.instance(recordKey));
    }

    /**
     * Returns the {@code MeSH} subject for a specific record.
     *
     * @param recordKey the key of the {@code MeSH} record that
     * represents the subject.
     *
     * @return the {@code MeSH} subject defined by the specified
     * record.
     */
    public static MeshSubject instance(MeshRecordKey recordKey) {
        return instance(MeshDB.record(recordKey));
    }

    /**
     * Returns the {@code MeSH} subject for a specific record.
     *
     * @param record the {@code MeSH} record that represents the
     * subject.
     *
     * @return the {@code MeSH} subject defined by the specified
     * record.
     */
    public static synchronized MeshSubject instance(MeshRecord record) {
        MeshSubject subject = instances.get(record);

        if (subject == null) {
            subject = new MeshSubject(record);
            instances.put(record, subject);
        }

        return subject;
    }

    @Override public MeshRecord getMeshRecord() {
        return record;
    }
}
