
package pubmed.flat;

import java.io.File;
import java.io.PrintWriter;

import jam.app.JamLogger;
import jam.io.FileUtil;
import jam.io.IOUtil;
import jam.flat.FlatRecord;

import pubmed.xml.PubmedArticleElement;
import pubmed.xml.PubmedXmlDocument;

/**
 * Provides a base class to generate flat files from parsed XML
 * documents.
 */
public abstract class PubmedFlatFile<V extends FlatRecord> {
    /**
     * The path to the bulk XML file.
     */
    protected final File xmlFile;

    /**
     * The path to this flat file.
     */
    protected final File flatFile;

    /**
     * Creates a new flat file for records derived from a given bulk
     * XML file.
     *
     * @param xmlFile the bulk XML file containing articles to be
     * processed.
     */
    protected PubmedFlatFile(File xmlFile) {
        this.xmlFile = xmlFile;
        this.flatFile = resolveFlatFile();
    }

    private File resolveFlatFile() {
        return new File(FileUtil.getCanonicalPrefix(xmlFile) + "_" + getBasenameSuffix() + ".psv.gz");
    }

    /**
     * Extracts a data record from an XML article element.
     *
     * @param element the XML article element to process.
     *
     * @return the data record extracted from the specified article
     * element (may be {@code null} if the article does not contain
     * the relevant data items).
     */
    public abstract V extractRecord(PubmedArticleElement element);

    /**
     * Returns the suffix to append to the bulk XML file prefix when
     * composing the name of this flat file.
     *
     * @return the suffix to append to the bulk XML file prefix when
     * composing the name of this flat file.
     */
    public abstract String getBasenameSuffix();

    /**
     * Deletes the physical flat file.
     *
     * @return {@code true} iff the file existed and was successfully
     * deleted.
     */
    public boolean delete() {
        return flatFile.delete();
    }

    /**
     * Identifies existing flat files.
     *
     * @return {@code true} iff the physical flat file has already
     * been generated.
     */
    public boolean exists() {
        return flatFile.canRead();
    }

    /**
     * Returns the path to the physical flat file.
     *
     * @return the path to the physical flat file.
     */
    public File getFlatFile() {
        return flatFile;
    }

    /**
     * Returns the bulk XML file providing data for this flat file.
     *
     * @return the bulk XML file providing data for this flat file.
     */
    public File getXmlFile() {
        return xmlFile;
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
            processFile();
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

    private void processFile() {
        PubmedXmlDocument document = PubmedXmlDocument.parse(xmlFile);
        processDocument(document);
    }

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

    private void processDocument(PubmedXmlDocument document) {
        try (PrintWriter writer = IOUtil.openWriter(flatFile)) {
            JamLogger.info("Writing file [%s]...", flatFile);

            for (PubmedArticleElement element : document.getPubmedArticleElements()) {
                V record = extractRecord(element);

                if (record != null)
                    writer.println(record.format());
            }
        }
    }
}
