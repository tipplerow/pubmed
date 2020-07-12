
package pubmed.flat;

import java.io.File;
import java.util.List;

import jam.flat.FlatRecord;
import jam.util.StreamUtil;

import pubmed.xml.PubmedArticleElement;
import pubmed.xml.PubmedXmlDocument;

/**
 * Provides a base class to for flat files containing unique article
 * attribute data (one row per article).
 */
public abstract class PubmedFlatFile<V extends FlatRecord> extends FlatFileBase {
    /**
     * Creates a new flat file for records derived from a given bulk
     * XML file.
     *
     * @param xmlFile the bulk XML file containing articles to be
     * processed.
     */
    protected PubmedFlatFile(File xmlFile) {
        super(xmlFile);
    }

    /**
     * Extracts the single, unique data record from an XML article
     * element.
     *
     * @param element the XML article element to process.
     *
     * @return the data record extracted from the specified article
     * element (may be {@code null} if the article does not contain
     * the relevant data items).
     */
    public abstract V extractRecord(PubmedArticleElement element);

    @Override public List<V> extractRecords(PubmedXmlDocument document) {
        return StreamUtil.applyParallel(document.getPubmedArticleElements(), element -> extractRecord(element));
    }
}
