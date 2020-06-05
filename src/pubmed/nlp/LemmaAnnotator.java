
package pubmed.nlp;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreSentence;
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
     * Extracts the lemmatized nouns from bare text.
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

        for (CoreSentence sentence : document.sentences()) {
            List<String> lemmas = sentence.lemmas();
            List<String> posTags = sentence.posTags();

            assert lemmas.size() == posTags.size();

            for (int index = 0; index < lemmas.size(); ++index)
                if (POS.isNoun(posTags.get(index)))
                    nouns.add(lemmas.get(index).toLowerCase());
        }

        return nouns;
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("pubmed.junit.CoreNLPTest");
    }
}
