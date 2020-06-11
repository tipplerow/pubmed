
package pubmed.nlp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import java.util.function.Predicate;

import jam.util.ListUtil;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;

/**
 * Extracts lemmas from unstructured text.
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
     * Retains only lemmatized content words from unstructured text
     * and returns them in the proper order in a new list.
     *
     * @param text the unstructured text to lemmatize.
     *
     * @return a new list containing only the content words from the
     * unstructured text (with order maintained).
     */
    public static LemmaList contentWords(String text) {
        return contentWords(annotate(text));
    }

    /**
     * Retains only lemmatized content words from unstructured text
     * and returns them in the proper order in a list of new lists.
     *
     * @param strings the unstructured text to lemmatize.
     *
     * @return a list of lemmatized content lists.
     */
    public static List<LemmaList> contentWords(Collection<String> strings) {
        return ListUtil.apply(strings, s -> contentWords(s));
    }

    /**
     * Retains only lemmatized content words from an annotated
     * document and returns them in the proper order in a new
     * list.
     *
     * @param document the annotated document to lemmatize.
     *
     * @return a new list containing only the content words from
     * the annotated document (with order maintained).
     */
    public static LemmaList contentWords(CoreDocument document) {
        return filter(document, Token::isContentWord);
    }

    /**
     * Writes to standard output the lemmas and parts of speech
     * contained in unstructured text.
     *
     * @param text the unstructured text to annotate.
     */
    public static void display(String text) {
        CoreDocument document = annotate(text);

        for (CoreLabel label : document.tokens())
            System.out.println(String.format("[%s] (%s)", label.lemma(), label.tag()));
    }

    /**
     * Retains only desired tokens from an annotated document and
     * returns them in the proper order in a new list.
     *
     * @param document the annotated document to lemmatize.
     *
     * @param predicate a predicate to select the desired tokens.
     *
     * @return a new list containing only tokens that are selected by
     * the specified predicate (with order maintained).
     */
    public static LemmaList filter(CoreDocument document, Predicate<CoreLabel> predicate) {
        List<String> words = new ArrayList<String>();

        for (CoreLabel token : document.tokens())
            if (predicate.test(token))
                words.add(Token.lemma(token));

        return LemmaList.create(words);
    }

    /**
     * Retains only lemmatized nouns from unstructured text and
     * returns them in the proper order in a new list.
     *
     * @param text the unstructured text to lemmatize.
     *
     * @return a new list containing only the lemmatized nouns from
     * the unstructured text (with order maintained).
     */
    public static LemmaList nouns(String text) {
        return nouns(annotate(text));
    }

    /**
     * Retains only lemmatized nouns from unstructured text and
     * returns them in the proper order in a list of new lists.
     *
     * @param strings the unstructured text to lemmatize.
     *
     * @return a list of lemmatized content lists.
     */
    public static List<LemmaList> nouns(Collection<String> strings) {
        return ListUtil.apply(strings, s -> nouns(s));
    }

    /**
     * Retains only lemmatized nouns from an annotated document and
     * returns them in the proper order in a new list.
     *
     * @param document the annotated document to lemmatize.
     *
     * @return a new list containing only the lemmatized nouns from
     * the annotated document (with order maintained).
     */
    public static LemmaList nouns(CoreDocument document) {
        return filter(document, Token::isNoun);
    }
}
