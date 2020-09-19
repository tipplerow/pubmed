
package pubmed.mesh;

import java.util.HashMap;
import java.util.Map;

import jam.lang.JamException;

/**
 * Enumerates the categories contained in the {@code MeSH} tree
 * structure.
 */
public enum MeshTreeCategory {
    Anatomy('A'),
    Organisms('B'),
    Diseases('C'),
    Chemicals_and_Drugs('D'),
    Analytical_Diagnostic_and_Therapeutic_Techniques_and_Equipment('E'),
    Psychiatry_and_Psychology('F'),
    Biological_Sciences('G'),
    Physical_Sciences('H'),
    Anthropology_Education_Sociology_and_Social_Phenomena('I'),
    Technology_and_Food_and_Beverages('J'),
    Humanities('K'),
    Information_Science('L'),
    Persons('M'),
    Health_Care('N'),
    Publication_Characteristics('V'),
    Chemistry('Y'),
    Geographic_Locations('Z');

    private final Character code;

    private static final Map<Character, MeshTreeCategory> codeIndex =
        new HashMap<Character, MeshTreeCategory>();

    static {
        mapCodes();
    }

    private static void mapCodes() {
        for (MeshTreeCategory category : values())
            codeIndex.put(category.code, category);
    }

    private MeshTreeCategory(Character code) {
        this.code = code;
    }

    /**
     * Identifies defined {@code MeSH} tree categories.
     *
     * @param code a {@code MeSH} category character code.
     *
     * @return {@code true} iff there is a {@code MeSH} tree category
     * with the specified character code.
     */
    public static boolean exists(char code) {
        return codeIndex.containsKey(code);
    }

    /**
     * Returns the {@code MeSH} tree category with a given character
     * code.
     *
     * @param code a {@code MeSH} category character code.
     *
     * @return the {@code MeSH} tree category with the specified
     * character code.
     *
     * @throws RuntimeException unless there is a matching category.
     */
    public static MeshTreeCategory instance(char code) {
        MeshTreeCategory category = codeIndex.get(code);

        if (category != null)
            return category;
        else
            throw JamException.runtime("No matching category: [%c].", code);
    }

    /**
     * Returns the character code for this category.
     *
     * @return the character code for this category.
     */
    public char getCode() {
        return code;
    }
}
