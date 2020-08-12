
package pubmed.bulk;

import java.io.File;

import jam.app.JamLogger;

/**
 * Provides a base class to identify and process all {@code PubMed}
 * bulk XML files in a given directory.
 */
public abstract class BulkFileProcessor {
    private BulkFile[] bulkFileArray;

    /**
     * Creates a new bulk file processor.
     */
    protected BulkFileProcessor() {
    }

    /**
     * Processes a single bulk XML file.
     *
     * @param bulkFile the bulk XML file to process.
     */
    public abstract void processFile(BulkFile bulkFile);

    /**
     * Processes the bulk XML files in a given directory.
     *
     * @param directory the bulk file directory to process.
     *
     * @throws RuntimeException unless the specified file is a
     * directory and all files are processed successfully.
     */
    public void processDirectory(String directory) {
        processDirectory(new File(directory));
    }

    /**
     * Processes the bulk XML files in a given directory.
     *
     * @param directory the bulk file directory to process.
     *
     * @throws RuntimeException unless the specified file is a
     * directory and all files are processed successfully.
     */
    public synchronized void processDirectory(File directory) {
        bulkFileArray = BulkFile.list(directory);

        for (int fileIndex = 0; fileIndex < bulkFileArray.length; ++fileIndex)
            processFile(fileIndex);

        JamLogger.info("DONE!");
    }

    private void processFile(int fileIndex) {
        BulkFile bulkFile = bulkFileArray[fileIndex];

	JamLogger.info("************************************************************************");
        JamLogger.info("Processing file [%d] of [%d]...", fileIndex + 1, bulkFileArray.length);
        JamLogger.info(bulkFile);
	JamLogger.info("************************************************************************");

        try {
            processFile(bulkFile);
        }
        finally {
            //
            // The bulk file stores the parsed XML document privately,
            // so processing a directory with a thousand files would
            // create a large amount of memory overhead.  We delete
            // the bulk file reference to allow the garbage collector
            // to reclaim the allocated memory...
            //
            bulkFile = null;
            bulkFileArray[fileIndex] = null;
        }
    }
}
