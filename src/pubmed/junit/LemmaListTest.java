
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

    @Test public void testContainsOne() {
        LemmaList list = LemmaList.create("article", "discuss", "new", "treatment", "gastric", "cancer");

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
        LemmaList list = LemmaList.create("article", "discuss", "new", "treatment", "gastric", "cancer");

        assertTrue(list.containsSequence(List.of("new", "treatment")));
        assertFalse(list.containsSequence(List.of("treatment", "new")));

        assertTrue(list.containsSequence(List.of("gastric", "cancer")));
        assertFalse(list.containsSequence(List.of("cancer", "gastric")));
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("pubmed.junit.LemmaListTest");
    }
}
