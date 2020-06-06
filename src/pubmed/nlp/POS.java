
package pubmed.nlp;

/**
 * Categorizes parts of speech.
 */
public final class POS {
    /**
     * Determines whether a part of speech is an adjective.
     *
     * @param tag the part-of-speech tag.
     *
     * @return {@code true} iff the specified part-of-speech tag
     * identifies an adjective.
     */
    public static boolean isAdjective(String tag) {
        return tag.startsWith("JJ");
    }

    /**
     * Determines whether a part of speech is a noun.
     *
     * @param tag the part-of-speech tag.
     *
     * @return {@code true} iff the specified part-of-speech tag
     * identifies a noun.
     */
    public static boolean isNoun(String tag) {
        return tag.startsWith("NN");
    }

    /**
     * Determines whether a part of speech is a verb.
     *
     * @param tag the part-of-speech tag.
     *
     * @return {@code true} iff the specified part-of-speech tag
     * identifies a verb.
     */
    public static boolean isVerb(String tag) {
        return tag.startsWith("VB");
    }
}
