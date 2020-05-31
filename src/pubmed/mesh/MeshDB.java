
package pubmed.mesh;

/**
 * Provides an entry point to access records in the {@code MeSH}
 * database.
 */
public final class MeshDB {
    private static boolean loaded = false;

    /**
     * Returns the previously created descriptor record with a given
     * key.
     *
     * @param key the unique identifier for the descriptor.
     *
     * @return the desired descriptor record.
     *
     * @throws RuntimeException unless the key refers to a previously
     * created descriptor record.
     */
    public static MeshDescriptor descriptor(MeshDescriptorKey key) {
        return MeshDescriptor.instance(key);
    }

    /**
     * Returns the previously created pharmacological action with a
     * given key.
     *
     * @param key the unique identifier for the action.
     *
     * @return the desired action record.
     *
     * @throws RuntimeException unless the key refers to a previously
     * created action record.
     */
    public static MeshPharmAction pharmAction(MeshDescriptorKey key) {
        return MeshPharmAction.instance(key);
    }

    /**
     * Returns the previously created qualifier record with a given
     * key.
     *
     * @param key the unique identifier for the qualifier.
     *
     * @return the desired qualifier record.
     *
     * @throws RuntimeException unless the key refers to a previously
     * created qualifier record.
     */
    public static MeshQualifier qualifier(MeshQualifierKey key) {
        return MeshQualifier.instance(key);
    }

    /**
     * Returns the previously created qualifier record with a given
     * key.
     *
     * @param key the unique identifier for the qualifier.
     *
     * @return the desired qualifier record.
     *
     * @throws RuntimeException unless the key refers to a previously
     * created qualifier record.
     */
    public static MeshRecord record(MeshRecordKey key) {
        return MeshRecord.instance(key);
    }

    /**
     * Returns the previously created supplemental record with a given
     * key.
     *
     * @param key the unique identifier for the supplemental record.
     *
     * @return the desired supplemental record.
     *
     * @throws RuntimeException unless the key refers to a previously
     * created supplemental record.
     */
    public static MeshSupplemental supplemental(MeshSupplementalKey key) {
        return MeshSupplemental.instance(key);
    }

    /**
     * Ensures that all records in the {@code MeSH} database have been
     * loaded, stored in the registry, and indexed by key and name.
     *
     * <p>When this method is called for the first time, the master
     * XML files are parsed and all records are loaded.  Subsequent
     * calls have no effect, so there is no performance penalty for
     * calling this method multiple times in the same application.
     */
    public static synchronized void load() {
        if (loaded)
            return;

        MeshDescriptor.load();
        MeshPharmAction.load();
        MeshQualifier.load();
        MeshSupplemental.load();

        loaded = true;
    }
}

