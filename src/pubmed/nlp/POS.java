
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
     * Determines whether a part of speech denotes a <em>content
     * word</em> for the purposes of lemmatizing unstructured text,
     * keywords, and phrases.
     *
     * @param tag the part-of-speech tag.
     *
     * @return {@code true} iff the specified part-of-speech tag
     * identifies a content word.
     */
    public static boolean isContent(String tag) {
        return isNoun(tag)
            || isVerb(tag)
            || isAdjective(tag)
            || isNumber(tag)
            || isHyphen(tag);
    }

    /**
     * Determines whether a part of speech is a hyphen character.
     *
     * @param tag the part-of-speech tag.
     *
     * @return {@code true} iff the specified part-of-speech tag
     * identifies a hyphen character.
     */
    public static boolean isHyphen(String tag) {
        return tag.equals("HYPH");
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
     * Determines whether a part of speech is a cardinal number.
     *
     * @param tag the part-of-speech tag.
     *
     * @return {@code true} iff the specified part-of-speech tag
     * identifies a cardinal number.
     */
    public static boolean isNumber(String tag) {
        return tag.equals("CD");
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
