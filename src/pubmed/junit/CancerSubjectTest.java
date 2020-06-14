
package pubmed.junit;

import java.util.List;

import pubmed.subject.CancerSubject;
import pubmed.nlp.LemmaList;

import org.junit.*;
import static org.junit.Assert.*;

public class CancerSubjectTest {
    @Test public void testKeywordLemmas() {
        System.out.println(CancerSubject.INSTANCE.getKeywordLemmas());
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("pubmed.junit.CancerSubjectTest");
    }
}
