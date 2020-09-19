
package pubmed.subject;

import java.util.List;

import pubmed.nlp.LemmaList;

import org.junit.*;
import static org.junit.Assert.*;

public class SubjectTest {
    private static final Subject hematoma = MeshSubject.instance("D006406");
    private static final Subject nivolumab = MeshSubject.instance("D000077594");

    @Test public void testExists() {
        assertTrue(Subject.exists("D006406"));
        assertTrue(Subject.exists("D000077594"));
        assertFalse(Subject.exists("foo"));
    }

    @Test public void testInstance() {
        assertNull(Subject.instance("foo"));
        assertEquals(hematoma, Subject.instance("D006406"));
        assertEquals(nivolumab, Subject.instance("D000077594"));
    }

    @Test public void testKeywords() {
        assertEquals(List.of("Hematoma", "Hematomas"), hematoma.getKeywords());
        assertEquals(List.of("Nivolumab", "Opdivo",
                             "ONO-4538", "ONO 4538", "ONO4538",
                             "MDX-1106", "MDX 1106", "MDX1106",
                             "BMS-936558", "BMS 936558", "BMS936558"),
                     nivolumab.getKeywords());
    }

    @Test public void testKeywordLemmas() {
        assertEquals(List.of(LemmaList.create("hematoma")), hematoma.getKeywordLemmas());

        assertEquals(LemmaList.create("nivolumab"), nivolumab.getKeywordLemmas().get(0));
        assertEquals(LemmaList.create("opdivo"), nivolumab.getKeywordLemmas().get(1));
    }

    @Test public void testPrimaryName() {
        assertEquals("Hematoma", hematoma.getPrimaryName());
        assertEquals("Nivolumab", nivolumab.getPrimaryName());
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("pubmed.subject.SubjectTest");
    }
}
