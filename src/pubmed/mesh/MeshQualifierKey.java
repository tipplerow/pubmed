
package pubmed.mesh;

import java.util.HashMap;
import java.util.Map;

import jam.lang.JamException;

/**
 * The unique identifier for {@code MeSH Qualifier} records.
 */
public final class MeshQualifierKey extends MeshRecordKey {
    private static final Map<String, MeshQualifierKey> instances =
        new HashMap<String, MeshQualifierKey>();

    private MeshQualifierKey(String key) {
        super(key);
    }

    /**
     * The unique identifier for the {@code administration &amp; dosage} qualifier.
     */
    public static final MeshQualifierKey ADMIN_DOSAGE = instance("Q000008");

    /**
     * The unique identifier for the {@code adverse effects} qualifier.
     */
    public static final MeshQualifierKey ADVERSE_EFFECTS = instance("Q000009");

    /**
     * The unique identifier for the {@code agonists} qualifier.
     */
    public static final MeshQualifierKey AGONISTS = instance("Q000819");

    /**
     * The unique identifier for the {@code antagonists &amp; inhibitors} qualifier.
     */
    public static final MeshQualifierKey ANTAGONISTS = instance("Q000037");

    /**
     * The unique identifier for the {@code biosynthesis} qualifier.
     */
    public static final MeshQualifierKey BIOSYNTHESIS = instance("Q000096");

    /**
     * The unique identifier for the {@code diet therapy} qualifier.
     */
    public static final MeshQualifierKey DIET_THERAPY = instance("Q000178");

    /**
     * The unique identifier for the {@code drug effects} qualifier.
     */
    public static final MeshQualifierKey DRUG_EFFECTS = instance("Q000187");

    /**
     * The unique identifier for the {@code drug therapy} qualifier.
     */
    public static final MeshQualifierKey DRUG_THERAPY = instance("Q000188");

    /**
     * The unique identifier for the {@code enzymology} qualifier.
     */
    public static final MeshQualifierKey ENZYMOLOGY = instance("Q000201");

    /**
     * The unique identifier for the {@code etiology} qualifier.
     */
    public static final MeshQualifierKey ETIOLOGY = instance("Q000209");

    /**
     * The unique identifier for the {@code genetics} qualifier.
     */
    public static final MeshQualifierKey GENETICS = instance("Q000235");

    /**
     * The unique identifier for the {@code metabolism} qualifier.
     */
    public static final MeshQualifierKey METABOLISM = instance("Q000378");

    /**
     * The unique identifier for the {@code pharmacokinetics} qualifier.
     */
    public static final MeshQualifierKey PHARMACOKINETICS = instance("Q000493");

    /**
     * The unique identifier for the {@code pharmacology} qualifier.
     */
    public static final MeshQualifierKey PHARMACOLOGY = instance("Q000494");

    /**
     * The unique identifier for the {@code poisoning} qualifier.
     */
    public static final MeshQualifierKey POISONING = instance("Q000506");

    /**
     * The unique identifier for the {@code therapeutic use} qualifier.
     */
    public static final MeshQualifierKey THERAPEUTIC_USE = instance("Q000627");

    /**
     * The unique identifier for the {@code therapy} qualifier.
     */
    public static final MeshQualifierKey THERAPY = instance("Q000628");

    /**
     * The unique identifier for the {@code toxicity} qualifier.
     */
    public static final MeshQualifierKey TOXICITY = instance("Q000633");

    /**
     * Returns the unique identifier for a given key string.
     *
     * @param key the string value of the qualifier key.
     *
     * @return the unique identifier for the given key string.
     */
    public static MeshQualifierKey instance(String key) {
        MeshQualifierKey instance = instances.get(key);

        if (instance == null) {
            instance = new MeshQualifierKey(key);
            instances.put(key, instance);
        }

        return instance;
    }

    @Override public MeshRecordType getType() {
        return MeshRecordType.QUALIFIER;
    }
}
