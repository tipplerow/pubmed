
package pubmed.junit;

import pubmed.article.PMID;
import pubmed.bulk.BulkFile;
import pubmed.bulk.DeleteCitationFile;
import pubmed.flat.PMIDRecord;
import pubmed.flat.PMIDTable;

import org.junit.*;
import static org.junit.Assert.*;

public class DeleteCitationFileTest {
    @Test public void testBaseline() {
        BulkFile bulkFile = BulkFile.create("data/test/pubmed20n0001.xml.gz");
        DeleteCitationFile deleteCitationFile = DeleteCitationFile.instance(bulkFile);

        deleteCitationFile.processFile(true);
        deleteCitationFile.processFile(true);

        PMIDTable table = deleteCitationFile.load();

        assertEquals(0, table.count());

        assertTrue(deleteCitationFile.delete());
    }

    @Test public void testUpdateFiles() {
        BulkFile bulkFile = BulkFile.create("data/test/pubmed_sample.xml");
        DeleteCitationFile deleteCitationFile = DeleteCitationFile.instance(bulkFile);

        deleteCitationFile.processFile(true);
        deleteCitationFile.processFile(true);

        PMIDTable table = deleteCitationFile.load();

        assertEquals(3, table.count());

        assertFalse(table.contains(PMID.instance(30830049)));
        assertTrue(table.contains(PMID.instance(30830050)));
        assertTrue(table.contains(PMID.instance(30830053)));
        assertTrue(table.contains(PMID.instance(30830055)));

        assertTrue(deleteCitationFile.delete());
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("pubmed.junit.DeleteCitationFileTest");
    }
}
