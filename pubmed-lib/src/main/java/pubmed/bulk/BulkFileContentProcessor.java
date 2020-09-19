
package pubmed.bulk;

import java.io.File;
import java.util.List;

import jam.app.JamLogger;
import jam.util.ListUtil;

import pubmed.delcit.DeleteCitationFile;
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
        PubmedXmlDocument document = bulkFile.getDocument();

        List<DocumentContentFile> unprocessed =
            ListUtil.filter(bulkFile.getContentFiles(), file -> !file.exists());

        for (DocumentContentFile contentFile : unprocessed)
            contentFile.processDocument(document, false);

        DeleteCitationFile.instance().add(document);
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
