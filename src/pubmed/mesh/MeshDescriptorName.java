
package pubmed.mesh;

import java.util.HashMap;
import java.util.Map;

/**
 * The common name for {@code MeSH Descriptor} records.
 */
public final class MeshDescriptorName extends MeshRecordName {
    private static final Map<String, MeshDescriptorName> instances =
        new HashMap<String, MeshDescriptorName>();

    private MeshDescriptorName(String name) {
        super(name);
    }

    /**
     * Returns the name object for a given name string.
     *
     * @param name the name string.
     *
     * @return the name object for the given name string.
     */
    public static MeshDescriptorName instance(String name) {
        MeshDescriptorName instance = instances.get(name);

        if (instance == null) {
            instance = new MeshDescriptorName(name);
            instances.put(name, instance);
        }

        return instance;
    }
}
