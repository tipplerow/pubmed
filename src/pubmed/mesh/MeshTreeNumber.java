
package pubmed.mesh;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import jam.lang.JamException;
import jam.lang.KeyedObject;
import jam.math.IntRange;
import jam.util.MapUtil;
import jam.util.RegexUtil;

/**
 * Uniquely identifies a node in the {@code MeSH} tree structure.
 */
public final class MeshTreeNumber extends KeyedObject<String> {
    private final MeshTreeCategory category;
    private final List<Integer> levelCodes;

    private static final Map<String, MeshTreeNumber> instances =
        new HashMap<String, MeshTreeNumber>();

    private static final char DELIM_CHAR = '.';
    private static final Pattern DELIM_PATTERN = RegexUtil.DOT;
    private static final IntRange LEVEL_CODE_RANGE = new IntRange(1, 999);

    private MeshTreeNumber(String key) {
        super(key);

        this.category = extractCategory(key);
        this.levelCodes = extractLevelCodes(key);

        instances.put(key, this);
    }

    private static MeshTreeCategory extractCategory(String key) {
        char code = key.charAt(0);

        if (MeshTreeCategory.exists(code))
            return MeshTreeCategory.instance(key.charAt(0));
        else
            throw JamException.runtime("Invalid tree number: [%s].", key);
    }

    private static List<Integer> extractLevelCodes(String key) {
        //
        // Skip the first (category code) character...
        //
        String[] fields = RegexUtil.split(DELIM_PATTERN, key.substring(1));
        List<Integer> codes = new ArrayList<Integer>(fields.length);

        for (String field : fields)
            codes.add(Integer.parseInt(field));

        for (int code : codes)
            validateLevelCode(code, key);

        return Collections.unmodifiableList(codes);
    }

    private static void validateLevelCode(int code, String key) {
        if (!LEVEL_CODE_RANGE.contains(code))
            throw JamException.runtime("Invalid tree number: [%s].", key);
    }

    /**
     * {@code MeSH} tree number for the neoplasm antigens descriptor.
     */
    public static final MeshTreeNumber ANTIGENS_NEOPLASM = MeshTreeNumber.instance("D23.050.285");

    /**
     * {@code MeSH} tree numbers for anti-carcinogenic agents.
     */
    public static final MeshTreeNumberList ANTI_CARCINOGENIC_AGENTS =
        MeshTreeNumberList.of(MeshTreeNumber.instance("D27.505.696.706.018"),
                              MeshTreeNumber.instance("D27.505.954.248.125"),
                              MeshTreeNumber.instance("D27.720.799.018"));

    /**
     * {@code MeSH} tree number for anti-neoplastic agents.
     */
    public static final MeshTreeNumber ANTI_NEOPLASTIC_AGENTS = MeshTreeNumber.instance("D27.505.954.248");

    /**
     * {@code MeSH} tree number for the dietary supplements descriptor.
     */
    public static final MeshTreeNumber DIETARY_SUPPLEMENTS = MeshTreeNumber.instance("G07.203.300.456");

    /**
     * {@code MeSH} tree number for the drug-interactions descriptor,
     * which includes agonism, antagonism, and synergism.
     */
    public static final MeshTreeNumber DRUG_INTERACTIONS = MeshTreeNumber.instance("G07.690.773.968");

    /**
     * {@code MeSH} tree number for the metabolism descriptor.
     */
    public static final MeshTreeNumber METABOLISM_DESCRIPTOR = MeshTreeNumber.instance("G03");

    /**
     * {@code MeSH} tree number for the metabolism qualifier.
     */
    public static final MeshTreeNumber METABOLISM_QUALIFIER = MeshTreeNumber.instance("Y08.040");

    /**
     * {@code MeSH} tree numbers for mitochondria.
     */
    public static final MeshTreeNumberList MITOCHONDRIA =
        MeshTreeNumberList.of(MeshTreeNumber.instance("A11.284.835.626"),
                              MeshTreeNumber.instance("A11.284.430.214.190.875.564"));

    /**
     * Root-level {@code MeSH} tree number for neoplasms (cancers).
     */
    public static final MeshTreeNumber NEOPLASMS = MeshTreeNumber.instance("C04");

    /**
     * {@code MeSH} tree numbers for pharmacology.
     */
    public static final MeshTreeNumberList PHARMACOLOGY =
        MeshTreeNumberList.of(MeshTreeNumber.instance("H02.628"),
                              MeshTreeNumber.instance("H01.158.703"));

    /**
     * {@code MeSH} tree number for pharmacological actions.
     */
    public static final MeshTreeNumber PHARMACOLOGICAL_ACTIONS = MeshTreeNumber.instance("D27.505");

    /**
     * Root-level {@code MeSH} tree number for sources of research support.
     */
    public static final MeshTreeNumber RESEARCH_SUPPORT = MeshTreeNumber.instance("V04");

    /**
     * Returns the tree number with a given key string.
     *
     * @param key a key string that encodes a tree number.
     *
     * @return the tree number with the specified key string.
     *
     * @throws RuntimeException unless the specified string
     * encodes a valid tree number.
     */
    public static MeshTreeNumber instance(String key) {
        MeshTreeNumber instance;

        synchronized(instances) {
            instance = instances.get(key);

            if (instance == null)
                instance = new MeshTreeNumber(key);
        }

        return instance;
    }

    /**
     * Returns the enumerated category described by this tree number.
     *
     * @return the enumerated category described by this tree number.
     */
    public MeshTreeCategory getCategory() {
        return category;
    }

    /**
     * Returns the depth of the tree node associated with this number.
     *
     * <p>Root nodes have a depth of {@code 1}.
     *
     * @return the depth of the tree node associated with this number.
     */
    public int getDepth() {
        return levelCodes.size();
    }

    /**
     * Returns the level code at a given depth of the tree.
     *
     * @param level the unit-offset level of the desired depth (the
     * root nodes are at level {@code 1}).
     *
     * @return a read-only view of the level codes for this number.
     *
     * @throws RuntimeException unless the level index is in the valid
     * range {@code [1, getDepth()]}.
     */
    public int getLevelCode(int level) {
        return levelCodes.get(level - 1);
    }

    /**
     * Returns a read-only view of the level codes for this number.
     *
     * @return a read-only view of the level codes for this number.
     */
    public List<Integer> getLevelCodes() {
        return levelCodes;
    }

    /**
     * Returns the tree number of the parent node.
     *
     * @return the tree number of the parent node (or {@code null} if
     * this tree number refers to a root node).
     */
    public MeshTreeNumber getParent() {
        if (isRootNumber())
            return null;
        else
            return instance(getParentString());
    }

    private String getParentString() {
        int delimIndex = getKey().lastIndexOf(DELIM_CHAR);

        if (delimIndex < 0)
            throw JamException.runtime("No parent for root number [%s].", getKey());
        else
            return getKey().substring(0, delimIndex);
    }

    /**
     * Identifies tree numbers that refer to root nodes.
     *
     * @return {@code true} iff this tree number refers to a root
     * node.
     */
    public boolean isRootNumber() {
        return levelCodes.size() == 1;
    }

    /**
     * Determines if this tree number describes a related (on the same
     * branch) but broader concept than another tree number.
     *
     * @param reference the reference tree number.
     *
     * @return {@code true} iff this tree number describes a related
     * but broader concept than the reference tree number.
     */
    public boolean isBroader(MeshTreeNumber reference) {
        return reference.isNarrower(this);
    }

    /**
     * Determines if this tree number describes a related (on the same
     * branch) but narrower concept than another tree number.
     *
     * @param reference the reference tree number.
     *
     * @return {@code true} iff this tree number describes a related
     * but narrower concept than the reference tree number.
     */
    public boolean isNarrower(MeshTreeNumber reference) {
        //
        // A narrower concept must have (1) the same category as the
        // reference, (2) a depth greater than the reference, and (3)
        // level codes that match the reference codes up to the length
        // of the reference.
        //
        if (!this.category.equals(reference.category))
            return false;

        if (this.getDepth() <= reference.getDepth())
            return false;
        
        return reference.levelCodes.equals(this.levelCodes.subList(0, reference.getDepth()));
    }

    /**
     * Determines if this tree number describes a related (on the same
     * branch) concept with the same depth as another tree number (if
     * they have the same parent).
     *
     * @param reference the reference tree number.
     *
     * @return {@code true} iff this tree number describes a related
     * concept with the same depth as the reference tree number (if
     * they have the same parent).
     */
    public boolean isSibling(MeshTreeNumber reference) {
        //
        // One is not a sibling to oneself... 
        //
        if (this.equals(reference))
            return false;

        // Neither sibling may be a root number...
        if (this.isRootNumber())
            return false;

        if (reference.isRootNumber())
            return false;

        // Sibling concepts must share the same (1) category,
        // (2) depth, and (3) first "depth" - 1 level codes...
        if (!this.category.equals(reference.category))
            return false;

        if (this.getDepth() != reference.getDepth())
            return false;

        return this.parentLevelCodes().equals(reference.parentLevelCodes());
    }

    private List<Integer> parentLevelCodes() {
        return levelCodes.subList(0, getDepth() - 1);
    }

    /**
     * Determines whether this tree number lies on the subtree with a
     * reference root node: either this number is identical to the
     * reference or describes a concept narrower than the reference.
     *
     * @param reference the reference tree number.
     *
     * @return {@code true} iff this tree number lies on the subtree
     * having the reference number as its root (either this number is
     * identical to the reference or is narrower than the reference).
     */
    public boolean onSubTree(MeshTreeNumber reference) {
        return equals(reference) || isNarrower(reference);
    }
}
