
package pubmed.mesh;

import java.util.Collection;
import java.util.List;

import jam.app.JamLogger;
import jam.util.FixedList;

/**
 * Represents {@code MeSH} heading records that are associated with
 * {@code PubMed} articles.
 */
public final class MeshHeading {
    private final MeshDescriptorKey descriptorKey;
    private final List<MeshQualifierKey> qualifierKeys;

    static {
        JamLogger.info("MeshHeading: Loading MeSH descriptors...");
        MeshDescriptor.load();
    }

    private MeshHeading(MeshDescriptorKey descriptorKey, List<MeshQualifierKey> qualifierKeys) {
        this.descriptorKey = descriptorKey;
        this.qualifierKeys = FixedList.create(qualifierKeys);
    }

    /**
     * Creates a new heading record.
     *
     * @param descriptorKey the key of the descriptor in the heading.
     *
     * @param qualifierKeys the keys of the qualifiers for the heading
     * descriptor.
     *
     * @return the new heading record.
     */
    public static MeshHeading create(MeshDescriptorKey descriptorKey,
                                     List<MeshQualifierKey> qualifierKeys) {
        return new MeshHeading(descriptorKey, qualifierKeys);
    }

    /**
     * Returns the descriptor record for this heading.
     *
     * <p>The descriptor database must be loaded before calling this
     * method.
     *
     * @return the descriptor record for this heading.
     */
    public MeshDescriptor getDescriptor() {
        return MeshDescriptor.instance(descriptorKey);
    }

    /**
     * Returns the key of the descriptor for this heading.
     *
     * @return the key of the descriptor for this heading.
     */
    public MeshDescriptorKey getDescriptorKey() {
        return descriptorKey;
    }

    /**
     * Returns the {@code MeSH} number list for the descriptor of
     * this heading.
     *
     * <p>The descriptor XML file must be parsed before calling this
     * method as the descriptor object must be dereferenced.
     *
     * @return the {@code MeSH} tree number for the descriptor of
     * this heading.
     */
    public MeshTreeNumberList getNumberList() {
        return getDescriptor().getNumberList();
    }

    /**
     * Identifies qualifiers for the heading descriptor.
     *
     * @param qualifierKey a qualifier key to query.
     *
     * @return {@code true} iff the heading descriptor is qualified by
     * the specified qualifier.
     */
    public boolean hasQualifier(MeshQualifierKey qualifierKey) {
        return qualifierKeys.contains(qualifierKey);
    }

    /**
     * Determines whether the descriptor for this heading lies on a
     * particular subtree of the full {@code MeSH} tree: whether it
     * refers to the same or narrower concept as the subtree root.
     *
     * @param subRoot the root of the subtree.
     *
     * @return {@code true} iff the descriptor of this heading lies on
     * the same subtree of the specified number: whether it describes
     * the same or a narrower concept as the subtree root.
     */
    public boolean onSubTree(MeshTreeNumber subRoot) {
        MeshTreeNumberList numberList = getNumberList();

        for (MeshTreeNumber treeNumber : numberList)
            if (treeNumber.onSubTree(subRoot))
                return true;

        return false;
    }

    /**
     * Determines whether the descriptor for this heading lies on one
     * or more subtrees of the full {@code MeSH} tree:  whether it
     * refers to the same or narrower concept as any of the subtree
     * roots.
     *
     * @param subRoots the root of the subtrees.
     *
     * @return {@code true} iff the descriptor of this heading lies on
     * the same subtree as any of the subroots: whether it describes
     * the same or a narrower concept as any of the subtree roots.
     */
    public boolean onSubTree(Collection<MeshTreeNumber> subRoots) {
        for (MeshTreeNumber subRoot : subRoots)
            if (onSubTree(subRoot))
                return true;

        return false;
    }

    /**
     * Returns the keys of the qualifiers for this heading (if any).
     *
     * @return the keys of the qualifiers for this heading (if any).
     */
    public List<MeshQualifierKey> viewQualifierKeys() {
        return qualifierKeys;
    }
}
