
package pubmed.mesh;

import jam.lang.JamException;

/**
 * Enumerates the type of {@code MeSH} records.
 */
public enum MeshRecordType {
    DESCRIPTOR("D"),
    QUALIFIER("Q"),
    SUPPLEMENTAL("C");

    private final String keyPrefix;

    private MeshRecordType(String keyPrefix) {
        this.keyPrefix = keyPrefix;
    }

    /**
     * Infers the record type for a given key string by matching the
     * key prefix.
     *
     * @param key the key string to categorize.
     *
     * @return the enumerated record type for the given key string.
     *
     * @throws RuntimeException unless the key string prefix matches a
     * known prefix.
     */
    public static MeshRecordType resolveKeyType(String key) {
        for (MeshRecordType type : values())
            if (key.startsWith(type.keyPrefix))
                return type;

        throw JamException.runtime("No matching key type: [%s].", key);
    }

    /**
     * Returns the key prefix for records of this type.
     *
     * <p>All records of this type have keys that start with the
     * returned string.
     *
     * @return the key prefix for records of this type.
     */
    public String getKeyPrefix() {
        return keyPrefix;
    }

    /**
     * Ensures that a record key has the prefix for this record type.
     *
     * @param key the key string to validate.
     *
     * @throws RuntimeException unless the key string has the prefix
     * that matches this record type.
     */
    public void validateKey(String key) {
        if (!key.startsWith(keyPrefix))
            throw JamException.runtime("Invalid %s key: [%s].", name(), key);
    }
}
