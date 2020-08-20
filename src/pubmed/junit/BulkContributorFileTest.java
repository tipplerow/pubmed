
package pubmed.junit;

import pubmed.bulk.BulkContributorFile;
import pubmed.bulk.BulkFile;

import org.junit.*;
import static org.junit.Assert.*;

public class BulkContributorFileTest {
    private static final BulkFile bulk0001 = BulkFile.create("data/test/pubmed20n0001.xml.gz");
    private static final BulkFile bulk0002 = BulkFile.create("data/test/pubmed20n0002.xml.gz");
    private static final BulkFile bulk1203 = BulkFile.create("data/test/pubmed20n1203.xml.gz");

    @Test public void testBasic() {
        BulkContributorFile contribFile1 =
            BulkContributorFile.instance("data/test/bulk_contrib.txt");

        BulkContributorFile contribFile2 =
            BulkContributorFile.instance("data/test/bulk_contrib.txt");

        contribFile1.delete();
        assertFalse(contribFile1.exists());
        assertFalse(contribFile2.exists());

        assertFalse(contribFile1.isContributor(bulk0001));
        assertFalse(contribFile1.isContributor(bulk0002));
        assertFalse(contribFile1.isContributor(bulk1203));

        assertFalse(contribFile2.isContributor(bulk0001));
        assertFalse(contribFile2.isContributor(bulk0002));
        assertFalse(contribFile2.isContributor(bulk1203));

        contribFile1.addContributor(bulk0001);

        assertTrue(contribFile1.isContributor(bulk0001));
        assertFalse(contribFile1.isContributor(bulk0002));
        assertFalse(contribFile1.isContributor(bulk1203));

        assertTrue(contribFile2.isContributor(bulk0001));
        assertFalse(contribFile2.isContributor(bulk0002));
        assertFalse(contribFile2.isContributor(bulk1203));

        contribFile2.addContributor(bulk0002);
        contribFile2.addContributor(bulk1203);

        assertTrue(contribFile1.isContributor(bulk0001));
        assertTrue(contribFile1.isContributor(bulk0002));
        assertTrue(contribFile1.isContributor(bulk1203));

        assertTrue(contribFile2.isContributor(bulk0001));
        assertTrue(contribFile2.isContributor(bulk0002));
        assertTrue(contribFile2.isContributor(bulk1203));

        assertTrue(contribFile1.delete());
        assertFalse(contribFile2.delete());
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("pubmed.junit.BulkContributorFileTest");
    }
}
