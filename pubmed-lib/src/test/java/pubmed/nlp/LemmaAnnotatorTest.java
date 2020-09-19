
package pubmed.nlp;

import org.junit.*;
import static org.junit.Assert.*;

public class LemmaAnnotatorTest {
    @Test public void testContentWords() {
        assertEquals(LemmaList.create("12", "-", "month", "overall", "survival", "be", "69", "%"),
                     LemmaAnnotator.contentWords("The 12-month overall survival was 69%"));

        assertEquals(LemmaList.create("lens", "have", "interesting", "property"),
                     LemmaAnnotator.contentWords("Lenses have interesting properties."));

        assertEquals(LemmaList.create("lens", "have", "interesting", "property"),
                     LemmaAnnotator.contentWords("A lens has an interesting property."));

        assertEquals(LemmaList.create("p53", "kras", "apc", "be", "important", "oncogene"),
                     LemmaAnnotator.contentWords("P53, KRAS, and APC are important oncogenes."));

        assertEquals(LemmaList.create("mtor", "signaling", "orchestrate", "stress", "-", "induce",
                                      "mutagenesis", "facilitate", "adaptive", "evolution", "cancer"),
                     LemmaAnnotator.contentWords("MTOR signaling orchestrates stress-induced mutagenesis,"
                                                 + " facilitating adaptive evolution in cancer."));
    }

    @Test public void testNouns() {
        assertEquals(LemmaList.create("month", "survival", "%"),
                     LemmaAnnotator.nouns("The 12-month overall survival was 69%"));

        assertEquals(LemmaList.create("lens", "property"),
                     LemmaAnnotator.nouns("Lenses have interesting properties."));

        assertEquals(LemmaList.create("lens", "property"),
                     LemmaAnnotator.nouns("A lens has an interesting property."));

        assertEquals(LemmaList.create("p53", "kras", "apc", "oncogene"),
                     LemmaAnnotator.nouns("P53, KRAS, and APC are important oncogenes."));

        assertEquals(LemmaList.create("mtor", "signaling", "stress", "mutagenesis", "evolution", "cancer"),
                     LemmaAnnotator.nouns("MTOR signaling orchestrates stress-induced mutagenesis,"
                                                 + " facilitating adaptive evolution in cancer."));
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("pubmed.nlp.LemmaAnnotatorTest");
    }
}
