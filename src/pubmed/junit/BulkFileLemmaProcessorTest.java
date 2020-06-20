
package pubmed.junit;

import java.io.File;
import java.util.List;
import java.util.Map;

import pubmed.article.PMID;
import pubmed.bulk.BulkFileLemmaProcessor;
import pubmed.nlp.LemmaList;

import org.junit.*;
import static org.junit.Assert.*;

public class BulkFileLemmaProcessorTest {
    private static final File LEMMA_FILE = new File("data/test/title_lemmas.psv");

    @Test public void testLoad() {
        Map<PMID, LemmaList> lemmas = BulkFileLemmaProcessor.load(LEMMA_FILE);

        assertEquals(3, lemmas.size());
        assertEquals(List.of("immune", "complex", "rheumatic", "disease"), lemmas.get(PMID.instance(61537)));
        assertEquals(List.of("loss", "hla", "antigen", "associate", "hormonal", "state"), lemmas.get(PMID.instance(61538)));
        assertEquals(List.of("fracture", "acrylic", "bone", "cement"), lemmas.get(PMID.instance(61541)));
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("pubmed.junit.BulkFileLemmaProcessorTest");
    }
}
