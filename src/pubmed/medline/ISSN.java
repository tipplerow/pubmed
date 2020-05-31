
package pubmed.medline;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import jam.lang.JamException;
import jam.lang.KeyedObject;

/**
 * Represents an International Standard Serial Number.
 */
public final class ISSN extends KeyedObject<String> {
    private static final Map<String, ISSN> instances =
        new HashMap<String, ISSN>();

    private static final Predicate<String> VALIDATOR =
        Pattern.compile("^[0-9]{4}-[0-9]{3}[0-9xX]$").asMatchPredicate();

    private ISSN(String issn) {
        super(issn);
        validateISSN(issn);
    }

    private static void validateISSN(String issn) {
        if (!isValid(issn))
            throw JamException.runtime("Malformed ISSN: [%s].", issn);
    }

    /**
     * Returns the {@code ISSN} for a given string value.
     *
     * @param issn the ISSN string value.
     *
     * @return the {@code ISSN} for the given string value.
     *
     * @throws RuntimeException unless the string is a well-formed
     * ISSN.
     */
    public static ISSN instance(String issn) {
        ISSN instance = instances.get(issn);

        if (instance == null) {
            instance = new ISSN(issn);
            instances.put(issn, instance);
        }

        return instance;
    }

    /**
     * Identifies well-formed ISSN strings.
     *
     * @param issn the ISSN string to test.
     *
     * @return {@code true} iff the input string is a well-formed
     * ISSN.
     */
    public static boolean isValid(String issn) {
        return VALIDATOR.test(issn);
    }
}
