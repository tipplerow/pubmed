
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
        BulkContributorFile contribFile =
            BulkContributorFile.instance("data/test/bulk_contrib.txt");

        contribFile.delete();
        assertFalse(contribFile.exists());

        assertFalse(contribFile.isContributor(bulk0001));
        assertFalse(contribFile.isContributor(bulk0002));
        assertFalse(contribFile.isContributor(bulk1203));

        contribFile.addContributor(bulk0001);

        assertTrue(contribFile.isContributor(bulk0001));
        assertFalse(contribFile.isContributor(bulk0002));
        assertFalse(contribFile.isContributor(bulk1203));

        contribFile.addContributor(bulk0002);
        contribFile.addContributor(bulk1203);

        assertTrue(contribFile.isContributor(bulk0001));
        assertTrue(contribFile.isContributor(bulk0002));
        assertTrue(contribFile.isContributor(bulk1203));

        assertTrue(contribFile.delete());
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("pubmed.junit.BulkContributorFileTest");
    }
}
