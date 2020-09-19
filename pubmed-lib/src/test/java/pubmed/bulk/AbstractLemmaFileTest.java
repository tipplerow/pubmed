
package pubmed.bulk;

import pubmed.article.PMID;
import pubmed.flat.AbstractLemmaTable;

import org.junit.*;
import static org.junit.Assert.*;

public class AbstractLemmaFileTest {
    private static final BulkFile sampleXml = BulkFile.create("data/test/pubmed_sample.xml");
    private static final AbstractLemmaFile abstractFile = AbstractLemmaFile.instance(sampleXml);

    @Test public void testFile() {
        abstractFile.processFile(true);
        abstractFile.processFile(true);

        AbstractLemmaTable table = abstractFile.load();

        assertEquals(4, table.count());
        assertTrue(table.select(PMID.instance(24451147)).getAbstractLemmas().join().startsWith("epidemiological | study"));
        assertTrue(table.select(PMID.instance(24451147)).getAbstractLemmas().join().endsWith("effect extracellular atp"));
        assertTrue(table.select(PMID.instance(31383287)).getAbstractLemmas().join().startsWith("major glioblastoma hallmark"));
        assertTrue(table.select(PMID.instance(31383287)).getAbstractLemmas().join().endsWith("primary gb cell culture"));

        assertTrue(abstractFile.delete());
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("pubmed.bulk.AbstractLemmaFileTest");
    }
}
