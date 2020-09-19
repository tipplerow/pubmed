
package pubmed.mesh;

import java.util.HashMap;
import java.util.Map;

import jam.lang.JamException;

/**
 * The unique identifier for {@code MeSH SupplementalRecord}s.
 */
public final class MeshSupplementalKey extends MeshRecordKey {
    private static final Map<String, MeshSupplementalKey> instances =
        new HashMap<String, MeshSupplementalKey>();

    private MeshSupplementalKey(String key) {
        super(key);
    }

    /**
     * Returns the unique identifier for a given key string.
     *
     * @param key the string value of the supplemental key.
     *
     * @return the unique identifier for the given key string.
     */
    public static MeshSupplementalKey instance(String key) {
        MeshSupplementalKey instance = instances.get(key);

        if (instance == null) {
            instance = new MeshSupplementalKey(key);
            instances.put(key, instance);
        }

        return instance;
    }

    @Override public MeshRecordType getType() {
        return MeshRecordType.SUPPLEMENTAL;
    }
}
