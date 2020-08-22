
package pubmed.junit;

import java.util.Set;

import pubmed.article.PMID;
import pubmed.bulk.BulkFile;
import pubmed.delcit.DeleteCitationFile;

import org.junit.*;
import static org.junit.Assert.*;

public class DeleteCitationFileTest {
    static {
        System.setProperty(DeleteCitationFile.DELCIT_FILENAME_PROPERTY, "data/test/delcit_test.txt");
    }

    @Test public void testProcess() {
        BulkFile bulkFile = BulkFile.create("data/test/pubmed_sample.xml");
        DeleteCitationFile delcitFile = DeleteCitationFile.instance();

        delcitFile.add(bulkFile);

        assertFalse(delcitFile.contains(PMID.instance(30830049)));
        assertTrue(delcitFile.contains(PMID.instance(30830050)));
        assertTrue(delcitFile.contains(PMID.instance(30830053)));
        assertTrue(delcitFile.contains(PMID.instance(30830055)));

        assertEquals(Set.of(PMID.instance(30830050),
                            PMID.instance(30830053),
                            PMID.instance(30830055)),
                     delcitFile.viewDeleted());

        assertTrue(delcitFile.delete());
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("pubmed.junit.DeleteCitationFileTest");
    }
}
