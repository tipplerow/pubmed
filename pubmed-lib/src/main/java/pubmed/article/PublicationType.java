
package pubmed.article;

import jam.app.JamLogger;

import pubmed.mesh.MeshDescriptor;
import pubmed.mesh.MeshDescriptorKey;

/**
 * Defines distinct article publication types.
 */ 
public final class PublicationType {
    private final MeshDescriptorKey descriptorKey;

    private PublicationType(MeshDescriptorKey descriptorKey) {
        this.descriptorKey = descriptorKey;
    }

    /**
     * Creates a new publication type with a fixed descriptor key.
     *
     * @param descriptorKey the key of the {@code MeSH} descriptor for
     * this publication type.
     *
     * @return the decorated {@code PublicationType} elements.
     */
    public static PublicationType create(String descriptorKey) {
        try {
            return create(MeshDescriptorKey.instance(descriptorKey));
        }
        catch (RuntimeException ex) {
            JamLogger.warn(ex);
            return null;
        }
    }

    /**
     * Creates a new publication type with a fixed descriptor key.
     *
     * @param descriptorKey the key of the {@code MeSH} descriptor for
     * this publication type.
     *
     * @return the decorated {@code PublicationType} elements.
     */
    public static PublicationType create(MeshDescriptorKey descriptorKey) {
        return new PublicationType(descriptorKey);
    }

    /**
     * Returns the {@code MeSH} descriptor for this publication type.
     *
     * @return the {@code MeSH} descriptor for this publication type.
     */
    public MeshDescriptor getDescriptor() {
        return MeshDescriptor.instance(descriptorKey);
    }

    /**
     * Returns the key of the {@code MeSH} descriptor for this
     * publication type.
     *
     * @return the key of the {@code MeSH} descriptor for this
     * publication type.
     */
    public MeshDescriptorKey getDescriptorKey() {
        return descriptorKey;
    }

    /**
     * Returns the name of this publication type.
     *
     * @return the name of this publication type.
     */
    public String getTypeName() {
        return getDescriptor().getName().getName();
    }
}
