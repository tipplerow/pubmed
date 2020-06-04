
package pubmed.junit;

import java.util.List;
import java.util.Properties;

import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreSentence;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;

import org.junit.*;
import static org.junit.Assert.*;

public class CoreNLPTest {
    @Test public void testPipeline() {
        Properties props = new Properties();
        props.put("annotators", "tokenize, ssplit, pos, lemma");

        String text = String.join(" ",
                                  "add your text here!",
                                  "It can contain multiple sentences.",
                                  "Cancer-related Atorvastatin an statins.",
                                  "Atorvastatin foo bar, the quite quite.",
                                  "Kidney Cancer Carcinoma, and Melanoma",
                                  "Formate assay in body fluids: application in methanol poisoning.",
                                  "Evaluating properties; fooling banshees (not so much).");

        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
        CoreDocument document = new CoreDocument(text);

        pipeline.annotate(document);

        for (CoreSentence sentence : document.sentences()) {
            List<String> lemmas = sentence.lemmas();
            List<String> posTags = sentence.posTags();

            for (int index = 0; index < lemmas.size(); ++index) {
                System.out.println("[" + lemmas.get(index) + "] (" + posTags.get(index) + ")");
            }
        }
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("pubmed.junit.CoreNLPTest");
    }
}
