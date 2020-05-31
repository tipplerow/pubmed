
package pubmed.mesh;

import jam.lang.JamException;
import jam.lang.KeyedObject;

/**
 * Represents the unique identifier for all {@code MeSH} records.
 */
public abstract class MeshRecordKey extends KeyedObject<String> {
    /**
     * Creates a new record key.
     *
     * @param key the key string.
     */
    protected MeshRecordKey(String key) {
        super(key);
        getType().validateKey(key);
    }

    /**
     * Returns the unique record identifier for a given key string.
     *
     * @param key the string value of the record key.
     *
     * @return the unique record identifier for the given key string.
     *
     * @throws RuntimeException unless the key string begins with a
     * known prefix, which is used to determine the record type.
     */
    public static MeshRecordKey instance(String key) {
        MeshRecordType type = MeshRecordType.resolveKeyType(key);

        switch (type) {
        case DESCRIPTOR:
            return MeshDescriptorKey.instance(key);

        case QUALIFIER:
            return MeshQualifierKey.instance(key);

        case SUPPLEMENTAL:
            return MeshSupplementalKey.instance(key);

        default:
            throw JamException.runtime("Unsupported record type: [%s].", type);
        }
    }

    /**
     * Returns the enumerated type of this record key.
     *
     * @return the enumerated type of this record key.
     */
    public abstract MeshRecordType getType();

    /**
     * Identifies {@code MeSH} descriptor keys.
     *
     * @return {@code true} iff this is the key of a {@code Mesh}
     * descriptor record.
     */
    public boolean isDescriptorKey() {
        return getType().equals(MeshRecordType.DESCRIPTOR);
    }

    /**
     * Identifies {@code MeSH} qualifier keys.
     *
     * @return {@code true} iff this is the key of a {@code Mesh}
     * qualifier record.
     */
    public boolean isQualifierKey() {
        return getType().equals(MeshRecordType.QUALIFIER);
    }

    /**
     * Identifies {@code MeSH} supplemental record keys.
     *
     * @return {@code true} iff this is the key of a {@code Mesh}
     * supplemental record.
     */
    public boolean isSupplementalKey() {
        return getType().equals(MeshRecordType.SUPPLEMENTAL);
    }
}
