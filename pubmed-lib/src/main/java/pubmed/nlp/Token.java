
package pubmed.nlp;

import edu.stanford.nlp.ling.CoreLabel;

/**
 * Utility functions operating on text tokens.
 */
public final class Token {
    /**
     * Determines whether a text token represents an adjective.
     *
     * @param token a text token.
     *
     * @return {@code true} iff the text token represents an
     * adjective.
     */
    public static boolean isAdjective(CoreLabel token) {
        return POS.isAdjective(token.tag());
    }

    /**
     * Determines whether a text tokent represents a <em>content
     * word</em> for the purposes of lemmatizing unstructured text,
     * keywords, and phrases.
     *
     * @param token a text token.
     *
     * @return {@code true} iff the text token represents a content
     * word.
     */
    public static boolean isContentWord(CoreLabel token) {
        return POS.isContent(token.tag());
    }

    /**
     * Determines whether a text token represents a hyphen.
     *
     * @param token a text token.
     *
     * @return {@code true} iff the text token represents a hyphen.
     */
    public static boolean isHyphen(CoreLabel token) {
        return POS.isHyphen(token.tag());
    }

    /**
     * Determines whether a text token represents a noun.
     *
     * @param token a text token.
     *
     * @return {@code true} iff the text token represents a noun.
     */
    public static boolean isNoun(CoreLabel token) {
        return POS.isNoun(token.tag());
    }

    /**
     * Determines whether a text token represents a cardinal number.
     *
     * @param token a text token.
     *
     * @return {@code true} iff the text token represents a cardinal
     * number.
     */
    public static boolean isNumber(CoreLabel token) {
        return POS.isNumber(token.tag());
    }

    /**
     * Determines whether a text token represents a verb.
     *
     * @param token a text token.
     *
     * @return {@code true} iff the text token represents a verb.
     */
    public static boolean isVerb(CoreLabel token) {
        return POS.isVerb(token.tag());
    }

    /**
     * Extracts the lemma from a token in <em>lower</em> case.
     *
     * @param token a text token.
     *
     * @return the lemma contained in the token, converted to lower
     * case.
     */
    public static String lemma(CoreLabel token) {
        return token.lemma().toLowerCase();
    }
}
