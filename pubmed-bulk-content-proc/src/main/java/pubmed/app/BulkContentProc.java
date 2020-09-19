
package pubmed.app;

import java.util.List;

import jam.util.ListUtil;

import pubmed.bulk.BulkFile;
import pubmed.bulk.BulkFileProcessor;
import pubmed.bulk.DocumentContentFile;
import pubmed.delcit.DeleteCitationFile;
import pubmed.xml.PubmedXmlDocument;

/**
 * Generates content files for all {@code PubMed} bulk XML files in a
 * given directory.
 */
public final class BulkContentProc extends BulkFileProcessor {
    private BulkContentProc() {
        super();
    }

    /**
     * The single bulk file content processor.
     */
    public static final BulkContentProc INSTANCE = new BulkContentProc();

    @Override public void processFile(BulkFile bulkFile) {
        PubmedXmlDocument document = bulkFile.getDocument();

        List<DocumentContentFile> unprocessed =
            ListUtil.filter(bulkFile.getContentFiles(), file -> !file.exists());

        for (DocumentContentFile contentFile : unprocessed)
            contentFile.processDocument(document, false);

        DeleteCitationFile.instance().add(document);
    }

    private static void usage() {
        System.err.println("Usage: pubmed.app.BulkContentProc DIR1 [DIR2 ...]");
        System.exit(1);
    }

    /**
     * Generates content files for all {@code PubMed} bulk XML files
     * in a given directory.
     */
    public static void main(String[] args) {
        if (args.length < 1)
            usage();

        for (String arg : args)
            INSTANCE.processDirectory(arg);
    }
}
