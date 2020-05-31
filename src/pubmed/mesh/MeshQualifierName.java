
package pubmed.mesh;

import java.util.HashMap;
import java.util.Map;

/**
 * The common name for {@code MeSH Qualifier} records.
 */
public final class MeshQualifierName extends MeshRecordName {
    private static final Map<String, MeshQualifierName> instances =
        new HashMap<String, MeshQualifierName>();

    private MeshQualifierName(String name) {
        super(name);
    }

    /**
     * Returns the name object for a given name string.
     *
     * @param name the name string.
     *
     * @return the name object for the given name string.
     */
    public static MeshQualifierName instance(String name) {
        MeshQualifierName instance = instances.get(name);

        if (instance == null) {
            instance = new MeshQualifierName(name);
            instances.put(name, instance);
        }

        return instance;
    }
}
