
package pubmed.mesh;

/**
 * Represents a {@code MeSH Qualifier} record.
 */
public final class MeshQualifier extends MeshTreeRecord {
    private static boolean loaded = false;

    private MeshQualifier(MeshQualifierKey   key,
                          MeshQualifierName  name,
                          String             note,
                          MeshTermList       terms,
                          MeshTreeNumberList numbers) {
        super(key, name, note, terms, numbers);
    }

    /**
     * Returns the {@code agonists} qualifier.
     *
     * @return the {@code agonists} qualifier.
     */
    public static MeshQualifier agonists() {
        return instance(MeshQualifierKey.AGONISTS);
    }

    /**
     * Returns the {@code antagonists &amp; inhibitors} qualifier.
     *
     * @return the {@code antagonists &amp; inhibitors} qualifier.
     */
    public static MeshQualifier antagonists() {
        return instance(MeshQualifierKey.ANTAGONISTS);
    }

    /**
     * Creates a new qualifier record and indexes it by key and name.
     *
     * @param key the unique identifier for the qualifier.
     *
     * @param name the common name for the qualifier.
     *
     * @param note the note associated with the record in the XML file.
     *
     * @param terms the terms associated with the qualifier.
     *
     * @param numbers the {@code MeSH} tree numbers associated with
     * the qualifier.
     *
     * @return the new qualifier record.
     */
    public static MeshQualifier create(MeshQualifierKey   key,
                                       MeshQualifierName  name,
                                       String             note,
                                       MeshTermList       terms,
                                       MeshTreeNumberList numbers) {
        return new MeshQualifier(key, name, note, terms, numbers);
    }

    /**
     * Returns the previously created qualifier record with a given
     * key.
     *
     * @param key the unique identifier for the qualifier.
     *
     * @return the desired qualifier record.
     *
     * @throws RuntimeException unless the requested record exists.
     */
    public static MeshQualifier instance(MeshQualifierKey key) {
        return (MeshQualifier) MeshRecord.instance(key);
    }

    /**
     * Ensures that all {@code MeSH} qualifiers have been loaded,
     * stored in the registry, and indexed by key and name.
     *
     * <p>When this method is called for the first time, the master
     * XML file is parsed and all records are loaded.  Subsequent
     * calls have no effect, so there is no performance penalty for
     * calling this method multiple times in the same application.
     */
    public static synchronized void load() {
        if (!loaded) {
            MeshQualifierXmlDocument.parseMaster();
            loaded = true;
        }
    }

    @Override public MeshQualifierKey getKey() {
        return (MeshQualifierKey) super.getKey();
    }

    @Override public MeshQualifierName getName() {
        return (MeshQualifierName) super.getName();
    }

    @Override public MeshRecordType getType() {
        return MeshRecordType.QUALIFIER;
    }
}
