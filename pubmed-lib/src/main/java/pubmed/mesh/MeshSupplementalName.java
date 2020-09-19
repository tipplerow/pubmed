
package pubmed.mesh;

import java.util.HashMap;
import java.util.Map;

/**
 * The common name for {@code MeSH SupplementalRecord}s.
 */
public final class MeshSupplementalName extends MeshRecordName {
    private static final Map<String, MeshSupplementalName> instances =
        new HashMap<String, MeshSupplementalName>();

    private MeshSupplementalName(String name) {
        super(name);
    }

    /**
     * Returns the name object for a given name string.
     *
     * @param name the name string.
     *
     * @return the name object for the given name string.
     */
    public static MeshSupplementalName instance(String name) {
        MeshSupplementalName instance = instances.get(name);

        if (instance == null) {
            instance = new MeshSupplementalName(name);
            instances.put(name, instance);
        }

        return instance;
    }
}
