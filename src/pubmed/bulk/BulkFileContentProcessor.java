
package pubmed.bulk;

import java.io.File;
import java.util.List;

import jam.app.JamLogger;
import jam.util.ListUtil;

import pubmed.xml.PubmedXmlDocument;

/**
 * Generates content files for all {@code PubMed} bulk XML files in a
 * given directory.
 */
public final class BulkFileContentProcessor extends BulkFileProcessor {
    private BulkFileContentProcessor() {
        super();
    }

    /**
     * The single bulk file content processor.
     */
    public static final BulkFileContentProcessor INSTANCE = new BulkFileContentProcessor();

    @Override public void processFile(BulkFile bulkFile) {
        //
        // Do not parse the XML document if all content files already
        // exist...
        //
        List<DocumentContentFile> unprocessed =
            ListUtil.filter(bulkFile.getContentFiles(), file -> !file.exists());

        if (unprocessed.isEmpty()) {
            JamLogger.info("All content files have been processed.");
            return;
        }

        PubmedXmlDocument document = bulkFile.getDocument();

        for (DocumentContentFile contentFile : unprocessed)
            contentFile.processDocument(document, false);
    }

    private static void usage() {
        System.err.println("Usage: pubmed.bulk.BulkFileContentProcessor DIR1 [DIR2 ...]");
        System.exit(1);
    }

    public static void main(String[] args) {
        if (args.length < 1)
            usage();

        for (String arg : args)
            INSTANCE.processDirectory(arg);
    }
}
