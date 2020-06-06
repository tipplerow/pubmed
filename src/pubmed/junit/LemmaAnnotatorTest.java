
package pubmed.junit;

import java.util.List;

import pubmed.nlp.LemmaAnnotator;

import org.junit.*;
import static org.junit.Assert.*;

public class LemmaAnnotatorTest {
    @Test public void testKeywords() {
        assertKeywords("Lenses have interesting properties.", "lens", "interesting", "property");
        assertKeywords("P53, KRAS, and APC are important oncogenes.", "p53", "kras", "apc", "important", "oncogene");
        assertKeywords("MTOR signaling orchestrates stress-induced mutagenesis, facilitating adaptive evolution in cancer",
                       "mtor", "signaling", "stress", "mutagenesis", "adaptive", "evolution", "cancer");

        assertKeywords("Lenses have interesting properties.  The most important is their ability to focus light.",
                       "lens", "interesting", "property", "important", "ability", "light");
    }

    private void assertKeywords(String text, String... keywords) {
        assertEquals(List.of(keywords), LemmaAnnotator.keywords(text));
    }

    @Test public void testNouns() {
        assertNouns("Lenses have many interesting properties.", "lens", "property");
        assertNouns("P53, KRAS, and APC are important oncogenes.", "p53", "kras", "apc", "oncogene");
        assertNouns("MTOR signaling orchestrates stress-induced mutagenesis, facilitating adaptive evolution in cancer",
                    "mtor", "signaling", "stress", "mutagenesis", "evolution", "cancer");

        assertNouns("Lenses have interesting properties.  The most important is their ability to focus light.",
                    "lens", "property", "ability", "light");
    }

    private void assertNouns(String text, String... nouns) {
        assertEquals(List.of(nouns), LemmaAnnotator.nouns(text));
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("pubmed.junit.LemmaAnnotatorTest");
    }
}
