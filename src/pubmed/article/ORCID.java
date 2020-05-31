
package pubmed.article;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import jam.lang.JamException;
import jam.lang.KeyedObject;

/**
 * Represents the unique {@code ORCID} identifier.
 */
public final class ORCID extends KeyedObject<String> {
    private static final Map<String, ORCID> instances =
        new HashMap<String, ORCID>();

    private static final int ID_LENGTH = 19;

    private static final Predicate<String> VALIDATOR =
        Pattern.compile("^[0-9]{4}-[0-9]{4}-[0-9]{4}-[0-9]{3}[0-9xX]$").asMatchPredicate();

    private ORCID(String orcid) {
        super(orcid);
        validate(orcid);
    }

    private static void validate(String orcid) {
        if (!isValid(orcid))
            throw JamException.runtime("Malformed ORCID: [%s].", orcid);
    }

    /**
     * Returns the {@code ORCID} identifier for a given string
     * value.
     *
     * @param orcid the {@code ORCID} string.
     *
     * @return the {@code ORCID} identifier for the given string
     * value.
     */
    public static ORCID instance(String orcid) {
        orcid = normalize(orcid);
        ORCID instance = instances.get(orcid);

        if (instance == null) {
            instance = new ORCID(orcid);
            instances.put(orcid, instance);
        }

        return instance;
    }

    /**
     * Determines whether an ORCID string has a structure required of
     * valid identifiers.
     *
     * @param orcid the identifier string to test.
     *
     * @return {@code true} iff the string is a valid {@code ORCID}
     * identifier.
     */
    public static boolean isValid(String orcid) {
        return VALIDATOR.test(orcid);
    }

    /**
     * Extracts the valid ORCID identifier from a string that might
     * include a leading URL or a trailing slash and might omit the
     * dash characters.
     *
     * @param orcid an {@code ORCID} string, possibly with a URL
     * prefix or trailing slash or lacking the dash characters.
     *
     * @return the normalized {@code ORCID} identifier.
     */
    public static String normalize(String orcid) {
        orcid = orcid.trim();
        orcid = removeTrailingSlash(orcid);
        orcid = removeURL(orcid);
        orcid = insertDashCharacters(orcid);

        return orcid;
    }

    private static String removeURL(String orcid) {
        if (orcid.length() > ID_LENGTH && orcid.charAt(orcid.length() - ID_LENGTH - 1) == '/')
            return orcid.substring(orcid.length() - ID_LENGTH);
        else
            return orcid;
    }

    private static String removeTrailingSlash(String orcid) {
        if (orcid.endsWith("/"))
            return orcid.substring(0, orcid.length() - 1);
        else
            return orcid;
    }

    private static String insertDashCharacters(String orcid) {
        //
        // Some PubMed ORCID strings contain only the 16 numeric characters...
        //
        if (orcid.length() != 16)
            return orcid;

        StringBuilder builder = new StringBuilder();

        builder.append(orcid.substring(0, 4));
        builder.append("-");
        builder.append(orcid.substring(4, 8));
        builder.append("-");
        builder.append(orcid.substring(8, 12));
        builder.append("-");
        builder.append(orcid.substring(12, 16));

        return builder.toString();
    }
}
