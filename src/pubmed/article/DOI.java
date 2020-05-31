
package pubmed.article;

import java.util.HashMap;
import java.util.Map;

import jam.lang.KeyedObject;

/**
 * Represents the unique Digital Object Identifier.
 */
public final class DOI extends KeyedObject<String> {
    private static final Map<String, DOI> instances =
        new HashMap<String, DOI>();

    private DOI(String doi) {
        super(doi);
    }

    /**
     * The prefix required to form a URL from a DOI.
     */
    public static final String URL_PREFIX = "https://doi.org/";

    /**
     * Returns the {@code DOI} object for a given identifier string.
     *
     * @param doi the {@code DOI} string.
     *
     * @return the {@code DOI} object for the given identifier string.
     */
    public static DOI instance(String doi) {
        DOI instance = instances.get(doi);

        if (instance == null) {
            instance = new DOI(doi);
            instances.put(doi, instance);
        }

        return instance;
    }

    /**
     * Returns the URL for this identifier.
     *
     * @return the URL for this identifier.
     */
    public String asURL() {
        return URL_PREFIX + getKey();
    }
}
