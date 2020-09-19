
package pubmed.mesh;

import jam.lang.JamException;

/**
 * Represents the common name for all {@code MeSH} records.
 */
public abstract class MeshRecordName {
    private final String name;

    /**
     * Creates a new name object.
     *
     * @param name the object name.
     */
    protected MeshRecordName(String name) {
        this.name = name;
    }

    /**
     * Returns the name object for a given bare string.
     *
     * @param name the name string.
     *
     * @param type the type of record to be named.
     *
     * @return the name object for the given bare string.
     *
     * @throws RuntimeException unless the key string begins with a
     * known prefix, which is used to determine the record type.
     */
    public static MeshRecordName instance(String name, MeshRecordType type) {
        switch (type) {
        case DESCRIPTOR:
            return MeshDescriptorName.instance(name);

        case QUALIFIER:
            return MeshQualifierName.instance(name);

        case SUPPLEMENTAL:
            return MeshSupplementalName.instance(name);

        default:
            throw JamException.runtime("Unsupported record type: [%s].", type);
        }
    }

    /**
     * Returns the name of this object.
     *
     * @return the name of this object.
     */
    public final String getName() {
        return name;
    }

    @Override public boolean equals(Object obj) {
        return this.getClass().equals(obj.getClass()) && equalsName((MeshRecordName) obj);
    }

    private boolean equalsName(MeshRecordName that) {
        return this.name.equals(that.name);
    }

    @Override public int hashCode() {
        return name.hashCode();
    }

    @Override public String toString() {
        return String.format("%s(%s)", getClass().getSimpleName(), name);
    }
}
