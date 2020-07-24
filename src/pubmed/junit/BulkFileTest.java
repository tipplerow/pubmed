
package pubmed.junit;

import java.io.File;
import java.util.List;

import jam.app.JamEnv;

import pubmed.bulk.BulkFile;
import pubmed.bulk.DocumentContentFile;

import org.junit.*;
import static org.junit.Assert.*;

public class BulkFileTest {
    private static final File testDir = new File("data/test");

    private static final BulkFile bulk0001 = BulkFile.create("data/test/pubmed20n0001.xml.gz");
    private static final BulkFile bulk0002 = BulkFile.create("data/test/pubmed20n0002.xml.gz");
    private static final BulkFile bulk1203 = BulkFile.create("data/test/pubmed20n1203.xml.gz");

    @Test public void testBaseName() {
        assertEquals("pubmed20n0001.xml.gz", bulk0001.getBaseName());
    }

    @Test public void testBaseNamePrefix() {
        assertEquals("pubmed20n0001", bulk0001.getBaseNamePrefix());
    }

    @Test public void testCanonicalPrefix() {
        assertEquals(JamEnv.getRequired("PUBMED_HOME") + "/data/test/pubmed20n0001", bulk0001.getCanonicalPrefix());
    }

    @Test public void testList() {
        assertEquals(List.of(bulk0001, bulk0002, bulk1203), BulkFile.list(testDir));
    }

    @Test public void testProcess() {
        BulkFile bulkFile = BulkFile.create("data/test/pubmed_sample.xml");

        bulkFile.process();
        bulkFile.process();

        for (DocumentContentFile contentFile : bulkFile.getContentFiles())
            assertTrue(contentFile.delete());
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("pubmed.junit.BulkFileTest");
    }
}
