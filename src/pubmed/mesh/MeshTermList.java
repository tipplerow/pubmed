
package pubmed.mesh;

import java.util.AbstractList;
import java.util.Collections;
import java.util.List;

/**
 * Encapsulates the terms associated with a {@code MeSH} record and
 * identifies the preferred and alternate terms.
 */
public final class MeshTermList extends AbstractList<String> {
    private final List<String> terms;

    private MeshTermList(List<String> terms) {
        if (terms.isEmpty())
            throw new IllegalArgumentException("Term list is empty.");

        this.terms = Collections.unmodifiableList(terms);
    }

    /**
     * The default delimiter to be used when joining the terms into a
     * single string.
     */
    public static final String DELIM = ";";

    /**
     * Creates a new term list.
     *
     * @param terms the terms in the list, with the preferred term as
     * the first element.
     *
     * @return the new term list.
     *
     * @throws RuntimeException if the term list is empty.
     */
    public static MeshTermList create(List<String> terms) {
        return new MeshTermList(terms);
    }

    /**
     * Returns the preferred term.
     *
     * @return the preferred term.
     */
    public String getPreferred() {
        return terms.get(0);
    }

    /**
     * Returns the alternate terms (synonyms).
     *
     * @return the alternate terms (synonyms).
     */
    public List<String> getAlternate() {
        return terms.subList(1, terms.size());
    }

    /**
     * Constructs a single string composed of the terms in this list
     * separated by the default delimiter.
     *
     * @return a single string composed of the terms in this list
     * separated by the default delimiter.
     */
    public String join() {
        return join(DELIM);
    }

    /**
     * Constructs a single string composed of the terms in this list
     * separated by a delimiter.
     *
     * @param delim the delimiter to separate the terms.
     *
     * @return a single string composed of the terms in this list
     * separated by the given delimiter.
     */
    public String join(String delim) {
        return String.join(delim, terms);
    }

    @Override public String get(int index) {
        return terms.get(index);
    }

    @Override public int size() {
        return terms.size();
    }
}
