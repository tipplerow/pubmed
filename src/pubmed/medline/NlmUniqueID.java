
package pubmed.medline;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import jam.lang.JamException;
import jam.lang.KeyedObject;

/**
 * Represents a National Library of Medicine (NLM) unique identifier.
 */
public final class NlmUniqueID extends KeyedObject<String> {
    private static final Map<String, NlmUniqueID> instances =
        new HashMap<String, NlmUniqueID>();

    private static final Predicate<String> VALIDATOR =
        Pattern.compile("^[0-9]+[0-9a-zA-Z]$").asMatchPredicate();

    private NlmUniqueID(String id) {
        super(id);
        validateNlmUniqueID(id);
    }

    private static void validateNlmUniqueID(String id) {
        if (!isValid(id))
            throw JamException.runtime("Malformed NLM unique ID: [%s].", id);
    }

    /**
     * Returns the {@code NlmUniqueID} for a given string value.
     *
     * @param id the identifier string value.
     *
     * @return the {@code NlmUniqueID} for the given string value.
     *
     * @throws RuntimeException unless the string is a well-formed
     * identifier.
     */
    public static NlmUniqueID instance(String id) {
        NlmUniqueID instance = instances.get(id);

        if (instance == null) {
            instance = new NlmUniqueID(id);
            instances.put(id, instance);
        }

        return instance;
    }

    /**
     * Identifies well-formed identifiers.
     *
     * @param id the identifier to test.
     *
     * @return {@code true} iff the input string is a well-formed
     * identifier.
     */
    public static boolean isValid(String id) {
        return VALIDATOR.test(id);
    }
}
