
package pubmed.medline;

import java.util.HashMap;
import java.util.Map;

import jam.lang.KeyedObject;

/**
 * Represents a {@code MEDLINE} title abbrevation.
 */
public final class MedlineTA extends KeyedObject<String> {
    private static final Map<String, MedlineTA> instances =
        new HashMap<String, MedlineTA>();

    private MedlineTA(String abbr) {
        super(abbr);
    }

    /**
     * Returns the {@code MedlineTA} for a given string value.
     *
     * @param abbr the title abbreviation.
     *
     * @return the {@code MedlineTA} for the given string value.
     */
    public static MedlineTA instance(String abbr) {
        MedlineTA instance = instances.get(abbr);

        if (instance == null) {
            instance = new MedlineTA(abbr);
            instances.put(abbr, instance);
        }

        return instance;
    }
}
