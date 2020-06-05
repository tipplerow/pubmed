
package pubmed.junit;

import java.util.List;

import pubmed.nlp.LemmaAnnotator;

import org.junit.*;
import static org.junit.Assert.*;

public class LemmaAnnotatorTest {
    @Test public void testNouns() {
        assertNouns("Lenses have interesting properties.", "lens", "property");
        assertNouns("P53, KRAS, and APC are important oncogenes.", "p53", "kras", "apc", "oncogene");
        assertNouns("MTOR signaling orchestrates stress-induced mutagenesis, facilitating adaptive evolution in cancer",
                    "mtor", "signaling", "stress", "mutagenesis", "evolution", "cancer");
    }

    private void assertNouns(String text, String... nouns) {
        assertEquals(List.of(nouns), LemmaAnnotator.nouns(text));
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("pubmed.junit.LemmaAnnotatorTest");
    }
}
