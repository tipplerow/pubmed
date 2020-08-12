
package pubmed.bulk;

import java.io.File;
import java.util.Collection;
import java.util.List;

import jam.app.JamLogger;
import jam.util.ListUtil;

import pubmed.relev.RelevanceSummaryFile;
import pubmed.subject.Subject;
import pubmed.xml.PubmedXmlDocument;

/**
 * Provides a base class to identify and process all {@code PubMed}
 * bulk XML files in a given directory.
 */
public abstract class BulkFileProcessor {
    private BulkFile[] bulkFileArray;

    private int fileIndex;
    private BulkFile bulkFile;

    /**
     * Creates a new bulk file processor.
     */
    protected BulkFileProcessor() {
    }

    /**
     * Returns the subjects used as the targets for article relevance
     * scoring.
     *
     * @return the subjects used as the targets for article relevance
     * scoring.
     */
    public abstract Collection<Subject> getSubjects();

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

        for (fileIndex = 0; fileIndex < bulkFileArray.length; ++fileIndex)
            processFile();

        JamLogger.info("DONE!");
    }

    private void processFile() {
        bulkFile = bulkFileArray[fileIndex];

	JamLogger.info("************************************************************************");
        JamLogger.info("Processing file [%d] of [%d]...", fileIndex + 1, bulkFileArray.length);
        JamLogger.info(bulkFile);
	JamLogger.info("************************************************************************");

        try {
            processContent();
            processRelevance();
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

    private void processContent() {
        //
        // Do not parse the XML document if all content files already
        // exist...
        //
        List<DocumentContentFile> unprocessed =
            getUnprocessedContentFiles();

        if (unprocessed.isEmpty()) {
            JamLogger.info("All content files have been processed.");
            return;
        }

        PubmedXmlDocument document = bulkFile.getDocument();

        for (DocumentContentFile contentFile : unprocessed)
            contentFile.processDocument(document, false);
    }

    private List<DocumentContentFile> getUnprocessedContentFiles() {
        return ListUtil.filter(bulkFile.getContentFiles(), file -> !file.exists());
    }

    private void processRelevance() {
        //
        // Relevance score files must be processed even if they
        // already exist, because new subjects might be present...
        //
        Collection<Subject> subjects = getSubjects();

        for (RelevanceScoreFile relevanceFile : bulkFile.getRelevanceScoreFiles())
            relevanceFile.process(subjects);

        RelevanceSummaryFile.process(bulkFile, subjects);
    }
}
