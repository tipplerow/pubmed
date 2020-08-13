
package pubmed.junit;

import java.io.File;

import pubmed.article.PMID;
import pubmed.bulk.BulkFile;
import pubmed.bulk.TitleLemmaFile;
import pubmed.flat.TitleLemmaRecord;
import pubmed.flat.TitleLemmaTable;

import org.junit.*;
import static org.junit.Assert.*;

public class TitleLemmaFileTest {
    private static final BulkFile sampleXml = BulkFile.create("data/test/pubmed_sample.xml");
    private static final TitleLemmaFile titleFile = TitleLemmaFile.instance(sampleXml);

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
