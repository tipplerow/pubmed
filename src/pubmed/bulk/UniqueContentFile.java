
package pubmed.bulk;

import java.util.List;

import jam.util.StreamUtil;

import pubmed.flat.PubmedFlatRecord;
import pubmed.xml.PubmedArticleElement;
import pubmed.xml.PubmedXmlDocument;

/**
 * Provides a base class to for flat files containing unique article
 * attribute data (one row per article).
 */
public abstract class UniqueContentFile<V extends PubmedFlatRecord> extends DocumentContentFile<V> {
    /**
     * Creates a new flat file for records derived from a given bulk
     * XML file.
     *
     * @param bulkFile the bulk XML file containing articles to be
     * processed.
     */
    protected UniqueContentFile(BulkFile bulkFile) {
        super(bulkFile);
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
