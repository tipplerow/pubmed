
package pubmed.sql;

import jam.sql.BulkRecord;

import pubmed.mesh.MeshRecord;
import pubmed.mesh.MeshRecordKey;
import pubmed.mesh.MeshRecordName;
import pubmed.mesh.MeshRecordType;

/**
 * Represents a row in the {@code pubmed.mesh_concepts} table,
 * which contains the key, name, and type of a {@code MeSH}
 * descriptor, qualifier, or supplemental record.
 */
public final class MeshConceptRecord implements BulkRecord {
    private final MeshRecordKey key;
    private final MeshRecordName name;
    private final MeshRecordType type;

    private MeshConceptRecord(MeshRecordKey key,
                              MeshRecordName name,
                              MeshRecordType type) {
        this.key = key;
        this.name = name;
        this.type = type;
    }

    /**
     * Creates the {@code pubmed.mesh_concepts} record for a
     * {@code MeSH} record.
     *
     * @param record the {@code MeSH} record to store.
     *
     * @return the {@code pubmed.mesh_concepts} record for the
     * specified record.
     */
    public static MeshConceptRecord create(MeshRecord record) {
        return create(record.getKey(), record.getName(), record.getType());
    }

    /**
     * Creates a new {@code pubmed.mesh_concepts} record from its
     * fields.
     *
     * @param key the key of the {@code MeSH} concept.
     *
     * @param name the name of the {@code MeSH} concept.
     *
     * @param type the enumerated type of the {@code MeSH} concept.
     *
     * @return the {@code pubmed.mesh_concepts} record with the
     * specified fields.
     */
    public static MeshConceptRecord create(MeshRecordKey key,
                                           MeshRecordName name,
                                           MeshRecordType type) {
        return new MeshConceptRecord(key, name, type);
    }

    /**
     * Returns the key of the {@code MeSH} concept.
     *
     * @return the key of the {@code MeSH} concept.
     */
    public MeshRecordKey getKey() {
        return key;
    }

    /**
     * Returns the key of the {@code MeSH} concept.
     *
     * @return the key of the {@code MeSH} concept.
     */
    public String getKeyString() {
        return key.getKey();
    }

    /**
     * Returns the name of the {@code MeSH} concept.
     *
     * @return the name of the {@code MeSH} concept.
     */
    public MeshRecordName getName() {
        return name;
    }

    /**
     * Returns the name of the {@code MeSH} concept.
     *
     * @return the name of the {@code MeSH} concept.
     */
    public String getNameString() {
        return name.getName();
    }

    /**
     * Returns the type of the {@code MeSH} concept.
     *
     * @return the type of the {@code MeSH} concept.
     */
    public MeshRecordType getType() {
        return type;
    }

    /**
     * Returns the type of the {@code MeSH} concept.
     *
     * @return the type of the {@code MeSH} concept.
     */
    public String getTypeString() {
        return type.name();
    }

    @Override public String formatBulk() {
        return joinBulk(formatBulk(key), formatBulk(name.getName()), formatBulk(type));
    }
}
