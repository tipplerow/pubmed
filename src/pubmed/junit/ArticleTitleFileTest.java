
package pubmed.junit;

import pubmed.article.PMID;
import pubmed.bulk.ArticleTitleFile;
import pubmed.bulk.BulkFile;
import pubmed.flat.ArticleTitleRecord;
import pubmed.flat.ArticleTitleTable;

import org.junit.*;
import static org.junit.Assert.*;

public class ArticleTitleFileTest {
    private static final BulkFile bulkFile = BulkFile.create("data/test/pubmed_sample.xml");
    private static final ArticleTitleFile titleFile = ArticleTitleFile.instance(bulkFile);

    @Test public void testFile() {
        titleFile.processFile(true);
        titleFile.processFile(true);

        ArticleTitleTable table = titleFile.load();

        assertEquals(6, table.count());
        assertEquals("Formate assay in body fluids: application in methanol poisoning.\\",
                     table.select(PMID.instance(1)).getTitle());

        assertTrue(titleFile.delete());
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("pubmed.junit.ArticleTitleFileTest");
    }
}
