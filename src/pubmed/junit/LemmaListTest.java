
package pubmed.junit;

import java.util.List;

import pubmed.nlp.LemmaList;

import org.junit.*;
import static org.junit.Assert.*;

public class LemmaListTest {
    @Test public void testEquals() {
        LemmaList list1 = LemmaList.create("abc", "def", "ghi");
        LemmaList list2 = LemmaList.create("abc", "def", "ghi");
        LemmaList list3 = LemmaList.create("abc", "def");
        LemmaList list4 = LemmaList.create("abc", "def", "ghi", "jkl");

        assertTrue(list1 != list2);
        
        assertTrue(list1.equals(list2));
        assertTrue(list2.equals(list1));

        assertFalse(list1.equals(list3));
        assertFalse(list1.equals(list4));
    }

    @Test public void testHashCode() {
        LemmaList list1 = LemmaList.create("abc", "def", "ghi");
        LemmaList list2 = LemmaList.create("abc", "def", "ghi");
        LemmaList list3 = LemmaList.create("abc", "def");
        LemmaList list4 = LemmaList.create("abc", "def", "ghi", "jkl");

        assertTrue(list1.hashCode() == list2.hashCode());
        assertTrue(list1.hashCode() != list3.hashCode());
        assertTrue(list1.hashCode() != list4.hashCode());
    }

    @Test public void testContentWords() {
        assertEquals(LemmaList.create("lens", "have", "interesting",  "property"),
                     LemmaList.contentWords("Lenses have interesting properties."));

        assertEquals(LemmaList.create("lens", "have", "interesting",  "property"),
                     LemmaList.contentWords("A lens has an interesting property."));

        assertEquals(LemmaList.create("p53", "kras", "apc", "be", "important", "oncogene"),
                     LemmaList.contentWords("P53, KRAS, and APC are important oncogenes."));

        assertEquals(LemmaList.create("mtor", "signaling", "orchestrate", "stress", "induce",
                                      "mutagenesis", "facilitate", "adaptive", "evolution", "cancer"),
                     LemmaList.contentWords("MTOR signaling orchestrates stress-induced mutagenesis,"
                                            + " facilitating adaptive evolution in cancer."));
    }

    @Test public void testContainsOne() {
        LemmaList list = LemmaList.contentWords("This article discusses new treatments for gastric cancers.");

        assertTrue(list.contains("article"));
        assertTrue(list.contains("discuss"));
        assertTrue(list.contains("new"));
        assertTrue(list.contains("treatment"));
        assertTrue(list.contains("gastric"));
        assertTrue(list.contains("cancer"));

        assertFalse(list.contains("this"));
        assertFalse(list.contains("for"));
    }

    @Test public void testContainsList() {
        LemmaList list = LemmaList.contentWords("This article discusses new treatments for gastric cancers.");

        assertTrue(list.contains(List.of("gastric", "cancer")));
        assertFalse(list.contains(List.of("cancer", "gastric")));
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("pubmed.junit.LemmaListTest");
    }
}
