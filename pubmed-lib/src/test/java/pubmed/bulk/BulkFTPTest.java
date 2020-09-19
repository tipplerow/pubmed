
package pubmed.bulk;

import java.util.Set;

import org.junit.*;
import static org.junit.Assert.*;

public class BulkFTPTest {
    @Test public void testListLocalUpdateFiles() {
        Set<String> fileNames = BulkFTP.listLocalUpdateFiles();

        assertFalse(fileNames.isEmpty());

        for (String fileName : fileNames)
            assertTrue(fileName.startsWith("pubmed20n"));
    }

    @Test public void testListRemoteUpdateFiles() {
        Set<String> fileNames = BulkFTP.listRemoteUpdateFiles();

        assertFalse(fileNames.isEmpty());

        for (String fileName : fileNames)
            assertTrue(fileName.startsWith("pubmed20n"));
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("pubmed.bulk.BulkFTPTest");
    }
}
