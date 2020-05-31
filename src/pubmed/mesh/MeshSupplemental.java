
package pubmed.mesh;

/**
 * Represents a {@code MeSH SupplementalRecord}.
 */
public final class MeshSupplemental extends MeshRecord {
    private static boolean loaded = false;

    private MeshSupplemental(MeshSupplementalKey  key,
                             MeshSupplementalName name,
                             String               note,
                             MeshTermList         terms) {
        super(key, name, note, terms);
    }

    /**
     * Creates a new supplemental record and indexes it by key and
     * name.
     *
     * @param key the unique identifier for the supplemental record.
     *
     * @param name the common name for the supplemental record.
     *
     * @param note the note associated with the record in the XML file.
     *
     * @param terms the terms associated with the supplemental record.
     *
     * @return the new supplemental record.
     */
    public static MeshSupplemental create(MeshSupplementalKey  key,
                                          MeshSupplementalName name,
                                          String               note,
                                          MeshTermList         terms) {
        return new MeshSupplemental(key, name, note, terms);
    }

    /**
     * Returns the previously created supplemental record with a given
     * key.
     *
     * @param key the unique identifier for the supplemental record.
     *
     * @return the desired supplemental record.
     *
     * @throws RuntimeException unless the requested record exists.
     */
    public static MeshSupplemental instance(MeshSupplementalKey key) {
        return (MeshSupplemental) MeshRecord.instance(key);
    }

    /**
     * Ensures that all {@code MeSH} supplemental records have been
     * loaded, stored in the registry, and indexed by key and name.
     *
     * <p>When this method is called for the first time, the master
     * XML file is parsed and all records are loaded.  Subsequent
     * calls have no effect, so there is no performance penalty for
     * calling this method multiple times in the same application.
     */
    public static synchronized void load() {
        if (!loaded) {
            MeshSupplementalXmlDocument.parseMaster();
            loaded = true;
        }
    }

    @Override public MeshSupplementalKey getKey() {
        return (MeshSupplementalKey) super.getKey();
    }

    @Override public MeshSupplementalName getName() {
        return (MeshSupplementalName) super.getName();
    }

    @Override public MeshRecordType getType() {
        return MeshRecordType.SUPPLEMENTAL;
    }
}
