
package pubmed.junit;

import java.util.List;

import pubmed.subject.MeshSubject;
import pubmed.subject.Subject;
import pubmed.nlp.LemmaList;

import org.junit.*;
import static org.junit.Assert.*;

public class SubjectTest {
    private static final Subject hematoma = MeshSubject.create("D006406");
    private static final Subject nivolumab = MeshSubject.create("D000077594");

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

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("pubmed.junit.SubjectTest");
    }
}
