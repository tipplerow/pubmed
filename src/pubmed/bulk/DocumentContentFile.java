
package pubmed.bulk;

import java.io.PrintWriter;
import java.util.List;

import jam.app.JamLogger;
import jam.flat.FlatRecord;

import pubmed.xml.PubmedXmlDocument;

/**
 * Provides a base class for flat files that contain data records
 * derived directly from a parsed XML document.
 */
public abstract class DocumentContentFile<V extends FlatRecord> extends PubmedFlatFile<V> {
    /**
     * Creates a new content file for records derived from a given
     * bulk XML file.
     *
     * @param bulkFile the bulk XML file containing articles to be
     * processed.
     */
    protected DocumentContentFile(BulkFile bulkFile) {
        super(bulkFile);
    }

    /**
     * Extracts the data records from a parsed XML document.
     *
     * @param document the document derived from the bulk XML file.
     *
     * @return the data records extracted from the specified document
     * (elements may be {@code null} or missing for articles that do
     * not contain the relevant data items).
     */
    public abstract List<V> extractRecords(PubmedXmlDocument document);

    /**
     * Processes all article elements in the document derived from the
     * bulk XML file and writes them to the physical flat file.
     *
     * @param document the document derived from the bulk XML file.
     *
     * @param overwrite whether to overwrite an existing flat file (or
     * skip processing if the flat file already exists).
     */
    public void processDocument(PubmedXmlDocument document, boolean overwrite) {
        if (mustProcess(overwrite))
            processDocument(document);
    }

    private boolean mustProcess(boolean overwrite) {
        if (overwrite || !exists()) {
            return true;
        }
        else {
            JamLogger.info("File [%s] exists; not overwriting.", flatFile);
            return false;
        }
    }

    private void processDocument(PubmedXmlDocument document) {
        try (PrintWriter writer = openWriter(false)) {
            for (V record : extractRecords(document))
                if (record != null)
                    writer.println(record.format());
        }
    }

    /**
     * Parses the bulk XML file, processes all article elements in the
     * document, and writes them to the physical flat file.
     *
     * @param overwrite whether to overwrite an existing flat file (or
     * skip processing if the flat file already exists).
     */
    public void processFile(boolean overwrite) {
        if (mustProcess(overwrite))
            processDocument(bulkFile.parse());
    }
}
