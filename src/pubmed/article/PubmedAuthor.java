
package pubmed.article;

import java.lang.StringBuilder;
import java.util.List;

import jam.lang.JamException;

/**
 * Encapsulates author information as represented in the
 * {@code PubMed} database.
 */
public final class PubmedAuthor implements Comparable<PubmedAuthor> {
    private final String lastName;
    private final String foreName;
    private final String initials;
    private final String suffix;
    private final ORCID orcid;
    private final int hashCode;

    private PubmedAuthor(String lastName,
                         String foreName,
                         String initials,
                         String suffix,
                         ORCID orcid) {
        this.lastName = lastName;
        this.foreName = normalize(foreName);
        this.initials = normalize(initials);
        this.suffix = normalize(suffix);
        this.orcid = orcid;

        this.hashCode = java.util.Arrays.asList(lastName, foreName, initials, suffix).hashCode();
    }

    private static String normalize(String s) {
        if (s == null)
            return "";
        else
            return s;
    }

    /**
     * Returns a {@code PubMed} author with fixed attributes.
     *
     * @param lastName the last name of the author.
     *
     * @param foreName the first name of the author (may be {@code null}).
     *
     * @param initials the initials in the author name (may be {@code null}).
     *
     * @param suffix the suffix of the author name (may be {@code null}).
     *
     * @param orcid the {@code ORCID} identifier for the author.
     *
     * @return a {@code PubmedAuthor} instance with the given attributes.
     */
    public static PubmedAuthor create(String lastName,
                                      String foreName,
                                      String initials,
                                      String suffix,
                                      ORCID orcid) {
        return new PubmedAuthor(lastName, foreName, initials, suffix, orcid);
    }

    /**
     * Returns the standard string representation for this author.
     *
     * @return the standard string representation for this author.
     */
    public String format() {
        StringBuilder builder = new StringBuilder();

        builder.append(lastName);

        if (foreName.length() > 0 || initials.length() > 0)
            builder.append(",");

        if (foreName.length() > 0) {
            builder.append(" ");
            builder.append(foreName);
        }

        if (initials.length() > 0) {
            builder.append(" ");
            builder.append(initials);
        }

        if (suffix.length() > 0) {
            builder.append(", ");
            builder.append(suffix);
        }

        return builder.toString();
    }

    /**
     * Returns the last name of this author.
     *
     * @return the last name of this author.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Returns the first name of this author.
     *
     * @return the first name of this author.
     */
    public String getForeName() {
        return foreName;
    }

    /**
     * Returns the initials in the author name.
     *
     * @return the initials in the author name.
     */
    public String getInitials() {
        return initials;
    }

    /**
     * Returns the suffix of the author name.
     *
     * @return the suffix of the author name.
     */
    public String getSuffix() {
        return suffix;
    }

    /**
     * Returns the {@code ORCID} identifier for this author.
     *
     * @return the {@code ORCID} identifier for this author
     * ({@code null} if there is no known identifier).
     */
    public ORCID getORCID() {
        return orcid;
    }

    @Override public int compareTo(PubmedAuthor that) {
        int cmp;

        cmp = this.lastName.compareTo(that.lastName);

        if (cmp != 0)
            return cmp;

        cmp = this.foreName.compareTo(that.foreName);

        if (cmp != 0)
            return cmp;

        cmp = this.initials.compareTo(that.initials);

        if (cmp != 0)
            return cmp;

        return this.suffix.compareTo(that.suffix);
    }

    @Override public boolean equals(Object obj) {
        return (obj instanceof PubmedAuthor) && equalsAuthor((PubmedAuthor) obj);
    }

    private boolean equalsAuthor(PubmedAuthor that) {
        return this.lastName.equals(that.lastName)
            && this.foreName.equals(that.foreName)
            && this.initials.equals(that.initials)
            && this.suffix.equals(that.suffix);
    }

    @Override public int hashCode() {
        return hashCode;
    }

    @Override public String toString() {
        return String.format("PubmedAuthor(%s)", format());
    }
}
