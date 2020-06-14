
package pubmed.subject;

import pubmed.mesh.MeshDB;
import pubmed.mesh.MeshRecord;
import pubmed.mesh.MeshRecordKey;

/**
 * Represents a subject that are defined by a unique {@code MeSH}
 * record.
 */
public final class MeshSubject extends Subject {
    private final MeshRecord record;

    static {
        MeshDB.load();
    }

    private MeshSubject(MeshRecord record) {
        super(record.getKeyString(), record.getNameString());
        this.record = record;
    }

    /**
     * The <em>neoplasms</em> subject.
     */
    public static final Subject NEOPLASMS = MeshSubject.create("D009369");

    /**
     * Creates a new {@code MeSH} subject for a specific record.
     *
     * @param recordKey the key of the {@code MeSH} record that
     * represents the subject.
     *
     * @return the {@code MeSH} subject defined by the specified
     * record.
     */
    public static MeshSubject create(String recordKey) {
        return create(MeshRecordKey.instance(recordKey));
    }

    /**
     * Creates a new {@code MeSH} subject for a specific record.
     *
     * @param recordKey the key of the {@code MeSH} record that
     * represents the subject.
     *
     * @return the {@code MeSH} subject defined by the specified
     * record.
     */
    public static MeshSubject create(MeshRecordKey recordKey) {
        MeshDB.load();
        return create(MeshDB.record(recordKey));
    }

    /**
     * Creates a new {@code MeSH} subject for a specific record.
     *
     * @param record the {@code MeSH} record that represents the
     * subject.
     *
     * @return the {@code MeSH} subject defined by the specified
     * record.
     */
    public static MeshSubject create(MeshRecord record) {
        return new MeshSubject(record);
    }

    @Override public MeshRecord getMeshRecord() {
        return record;
    }
}
