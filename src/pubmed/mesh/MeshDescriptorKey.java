
package pubmed.mesh;

import java.util.HashMap;
import java.util.Map;

import jam.lang.JamException;

/**
 * The unique identifier for {@code MeSH Descriptor} records.
 */
public final class MeshDescriptorKey extends MeshRecordKey {
    private static final Map<String, MeshDescriptorKey> instances =
        new HashMap<String, MeshDescriptorKey>();

    private MeshDescriptorKey(String key) {
        super(key);
    }

    /**
     * The publication type descriptor for comments.
     */
    public static final MeshDescriptorKey CASE_REPORT =
        MeshDescriptorKey.instance("D002363");

    /**
     * The publication type descriptor for comments.
     */
    public static final MeshDescriptorKey COMMENT =
        MeshDescriptorKey.instance("D016420");

    /**
     * The publication type descriptor for journal articles.
     */
    public static final MeshDescriptorKey JOURNAL_ARTICLE =
        MeshDescriptorKey.instance("D016428");

    /**
     * The publication type descriptor for letters.
     */
    public static final MeshDescriptorKey LETTER =
        MeshDescriptorKey.instance("D016422");

    /**
     * The publication type descriptor for meta-analyses.
     */
    public static final MeshDescriptorKey META_ANALYSIS_TOPIC =
        MeshDescriptorKey.instance("D015201");

    /**
     * The publication type descriptor for meta-analyses.
     */
    public static final MeshDescriptorKey META_ANALYSIS_TYPE =
        MeshDescriptorKey.instance("D017418");

    /**
     * The publication type descriptor for published errata.
     */
    public static final MeshDescriptorKey PUBLISHED_ERRATUM =
        MeshDescriptorKey.instance("D016425");

    /**
     * The publication type descriptor for reviews.
     */
    public static final MeshDescriptorKey REVIEW_TOPIC =
        MeshDescriptorKey.instance("D012196");

    /**
     * The publication type descriptor for reviews.
     */
    public static final MeshDescriptorKey REVIEW_TYPE =
        MeshDescriptorKey.instance("D016454");

    /**
     * Returns the unique identifier for a given key string.
     *
     * @param key the string value of the descriptor key.
     *
     * @return the unique identifier for the given key string.
     */
    public static MeshDescriptorKey instance(String key) {
        MeshDescriptorKey instance = instances.get(key);

        if (instance == null) {
            instance = new MeshDescriptorKey(key);
            instances.put(key, instance);
        }

        return instance;
    }

    @Override public MeshRecordType getType() {
        return MeshRecordType.DESCRIPTOR;
    }
}
