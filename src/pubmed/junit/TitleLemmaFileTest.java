
package pubmed.junit;

import java.io.File;

import pubmed.article.PMID;
import pubmed.flat.TitleLemmaFile;
import pubmed.flat.TitleLemmaRecord;
import pubmed.flat.TitleLemmaTable;

import org.junit.*;
import static org.junit.Assert.*;

public class TitleLemmaFileTest {
    private static final File sampleXml = new File("data/test/pubmed_sample.xml");
    private static final TitleLemmaFile titleFile = TitleLemmaFile.from(sampleXml);

    @Test public void testFile() {
        titleFile.processFile(true);
        titleFile.processFile(true);

        TitleLemmaTable table = titleFile.load();

        assertEquals(6, table.count());
        assertTrue(table.select(PMID.instance(31687927)).getTitleLemmas().join().startsWith("loss adaptive capacity"));
        assertTrue(table.select(PMID.instance(31687927)).getTitleLemmas().join().endsWith("dynamics rhinovirus challenge"));

        assertTrue(titleFile.delete());
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("pubmed.junit.TitleLemmaFileTest");
    }
}
