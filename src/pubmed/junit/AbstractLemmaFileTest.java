
package pubmed.junit;

import java.io.File;

import pubmed.article.PMID;
import pubmed.flat.AbstractLemmaFile;
import pubmed.flat.AbstractLemmaRecord;
import pubmed.flat.AbstractLemmaTable;

import org.junit.*;
import static org.junit.Assert.*;

public class AbstractLemmaFileTest {
    private static final File sampleXml = new File("data/test/pubmed_sample.xml");
    private static final AbstractLemmaFile abstractFile = AbstractLemmaFile.from(sampleXml);

    @Test public void testFile() {
        abstractFile.processFile(true);
        abstractFile.processFile(true);

        AbstractLemmaTable table = abstractFile.load();

        assertEquals(4, table.count());
        assertTrue(table.select(PMID.instance(31383287)).getAbstractLemmas().join().startsWith("major glioblastoma hallmark"));
        assertTrue(table.select(PMID.instance(31383287)).getAbstractLemmas().join().endsWith("primary gb cell culture"));

        assertTrue(abstractFile.delete());
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("pubmed.junit.AbstractLemmaFileTest");
    }
}
