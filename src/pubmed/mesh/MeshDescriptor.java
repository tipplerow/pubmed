
package pubmed.mesh;

/**
 * Represents a {@code MeSH Descriptor} record.
 */
public final class MeshDescriptor extends MeshTreeRecord {
    private static boolean loaded = false;

    private MeshDescriptor(MeshDescriptorKey  key,
                           MeshDescriptorName name,
                           String             note,
                           MeshTermList       terms,
                           MeshTreeNumberList numbers) {
        super(key, name, note, terms, numbers);
    }

    /**
     * Creates a new descriptor record and indexes it by key and name.
     *
     * @param key the unique identifier for the descriptor.
     *
     * @param name the common name for the descriptor.
     *
     * @param note the note associated with the record in the XML file.
     *
     * @param terms the terms associated with the descriptor.
     *
     * @param numbers the {@code MeSH} tree numbers associated with
     * the descriptor.
     *
     * @return the new descriptor record.
     */
    public static MeshDescriptor create(MeshDescriptorKey  key,
                                        MeshDescriptorName name,
                                        String             note,
                                        MeshTermList       terms,
                                        MeshTreeNumberList numbers) {
        return new MeshDescriptor(key, name, note, terms, numbers);
    }

    /**
     * Returns the previously created descriptor record with a given
     * key.
     *
     * @param key the unique identifier for the descriptor.
     *
     * @return the desired descriptor record.
     *
     * @throws RuntimeException unless the requested record exists.
     */
    public static MeshDescriptor instance(MeshDescriptorKey key) {
        return (MeshDescriptor) MeshRecord.instance(key);
    }

    /**
     * Ensures that all {@code MeSH} descriptors have been loaded,
     * stored in the registry, and indexed by key and name.
     *
     * <p>When this method is called for the first time, the master
     * XML file is parsed and all records are loaded.  Subsequent
     * calls have no effect, so there is no performance penalty for
     * calling this method multiple times in the same application.
     */
    public static synchronized void load() {
        if (!loaded) {
            MeshDescriptorXmlDocument.parseMaster();
            loaded = true;
        }
    }

    @Override public MeshDescriptorKey getKey() {
        return (MeshDescriptorKey) super.getKey();
    }

    @Override public MeshDescriptorName getName() {
        return (MeshDescriptorName) super.getName();
    }

    @Override public MeshRecordType getType() {
        return MeshRecordType.DESCRIPTOR;
    }
}
