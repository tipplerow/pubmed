
package pubmed.nlp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import jam.util.ListUtil;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;

/**
 * Extracts lemmas from raw text.
 */
public final class LemmaAnnotator {
    private static StanfordCoreNLP pipeline;

    static {
        pipeline = createPipeline();
    }

    private static StanfordCoreNLP createPipeline() {
        Properties props = new Properties();
        props.put("annotators", "tokenize, ssplit, pos, lemma");

        return new StanfordCoreNLP(props);
    }

    /**
     * Annotates bare text.
     *
     * @param text the text to annotate.
     *
     * @return the annotated document.
     */
    public static synchronized CoreDocument annotate(String text) {
        CoreDocument document = new CoreDocument(text);
        pipeline.annotate(document);
        return document;
    }

    /**
     * Extracts the lemmatized nouns and adjectives from raw text.
     *
     * @param text the text to annotate.
     *
     * @return the lemmatized nouns and adjectives in the specified
     * text.
     */
    public static List<String> keywords(String text) {
        return keywords(annotate(text));
    }

    /**
     * Extracts the lemmatized nouns and adjectives from an annotated
     * document.
     *
     * @param document an annotated document.
     *
     * @return the lemmatized nouns and adjectives in the specified
     * document.
     */
    public static List<String> keywords(CoreDocument document) {
        List<String> keywords = new ArrayList<String>();

        for (CoreLabel token : document.tokens())
            if (Token.isKeyword(token))
                keywords.add(Token.lemma(token));

        return keywords;
    }

    /**
     * Retains only lemmatized nouns, verbs and adjectives from raw
     * text and returns them in the proper order in a new string.
     *
     * @param text the raw text to lemmatize.
     *
     * @return a new string containing only the nouns, verbs, and
     * adjectives from the raw text (with order maintained).
     */
    public static String lemmatize(String text) {
        return lemmatize(annotate(text));
    }

    /**
     * Retains only lemmatized nouns, verbs and adjectives from
     * raw text strings and returns them in the proper order in
     * new strings.
     *
     * @param strings the raw text to lemmatize.
     *
     * @return a list of lemmatized text for each input string.
     */
    public static List<String> lemmatize(Collection<String> strings) {
        return ListUtil.apply(strings, s -> lemmatize(s));
    }

    /**
     * Retains only lemmatized nouns, verbs and adjectives from an
     * annotated document and returns them in the proper order in a
     * new string.
     *
     * @param document the annotated document to lemmatize.
     *
     * @return a new string containing only the nouns, verbs, and
     * adjectives from the annotated document (with order maintained).
     */
    public static String lemmatize(CoreDocument document) {
        List<String> words = new ArrayList<String>();

        for (CoreLabel token : document.tokens())
            if (Token.isContentWord(token))
                words.add(Token.lemma(token));

        return String.join(" ", words);
    }

    /**
     * Extracts the lemmatized nouns from raw text.
     *
     * @param text the text to annotate.
     *
     * @return the lemmatized nouns in the specified text.
     */
    public static List<String> nouns(String text) {
        return nouns(annotate(text));
    }

    /**
     * Extracts the lemmatized nouns from an annotated document.
     *
     * @param document an annotated document.
     *
     * @return the lemmatized nouns in the specified document.
     */
    public static List<String> nouns(CoreDocument document) {
        List<String> nouns = new ArrayList<String>();

        for (CoreLabel token : document.tokens())
            if (Token.isNoun(token))
                nouns.add(Token.lemma(token));

        return nouns;
    }

    /**
     * Writes to standard output the lemmas and parts of speech
     * contained in raw text.
     *
     * @param text the raw text to annotate.
     */
    public static void display(String text) {
        CoreDocument document = annotate(text);

        for (CoreLabel label : document.tokens())
            System.out.println(String.format("[%s] (%s)", label.lemma(), label.tag()));
    }
}
