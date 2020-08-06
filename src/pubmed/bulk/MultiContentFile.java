
package pubmed.bulk;

import java.util.ArrayList;
import java.util.List;

import pubmed.flat.PubmedFlatRecord;
import pubmed.xml.PubmedArticleElement;
import pubmed.xml.PubmedXmlDocument;

/**
 * Provides a base class to for flat files containing multiple joining
 * records (many-to-many mappings) for each article.
 */
public abstract class MultiContentFile<V extends PubmedFlatRecord> extends DocumentContentFile<V> {
    /**
     * Creates a new flat file for records derived from a given bulk
     * XML file.
     *
     * @param bulkFile the bulk XML file containing articles to be
     * processed.
     */
    protected MultiContentFile(BulkFile bulkFile) {
        super(bulkFile);
    }

    /**
     * Extracts the joining records from an XML article element.
     *
     * @param element the XML article element to process.
     *
     * @return the joining records extracted from the article (will be
     * empty if the article does not contain any relevant data items).
     */
    public abstract List<V> extractRecords(PubmedArticleElement element);

    @Override public List<V> extractRecords(PubmedXmlDocument document) {
        List<V> documentRecords = new ArrayList<V>();

        for (PubmedArticleElement element : document.getPubmedArticleElements())
            documentRecords.addAll(extractRecords(element));

        return documentRecords;
    }
}
