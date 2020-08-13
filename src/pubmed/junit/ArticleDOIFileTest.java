
package pubmed.junit;

import pubmed.article.DOI;
import pubmed.article.PMID;
import pubmed.bulk.ArticleDOIFile;
import pubmed.bulk.BulkFile;
import pubmed.flat.ArticleDOIRecord;
import pubmed.flat.ArticleDOITable;

import org.junit.*;
import static org.junit.Assert.*;

public class ArticleDOIFileTest {
    private static final BulkFile bulkFile = BulkFile.create("data/test/pubmed_sample.xml");
    private static final ArticleDOIFile DOIFile = ArticleDOIFile.instance(bulkFile);

    @Test public void testFile() {
        DOIFile.processFile(true);
        DOIFile.processFile(true);

        ArticleDOITable table = DOIFile.load();

        assertEquals(6, table.count());
        assertEquals(DOI.instance("10.7554/eLife.47969"), table.select(PMID.instance(31687927)).getDOI());

        assertTrue(DOIFile.delete());
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("pubmed.junit.ArticleDOIFileTest");
    }
}
