
package pubmed.bulk;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.ImmutableListMultimap.Builder;
import com.google.common.collect.ListMultimap;

import jam.flat.RecordStore;

import pubmed.article.PMID;
import pubmed.flat.PubmedFlatRecord;
import pubmed.xml.PubmedArticleElement;
import pubmed.xml.PubmedXmlDocument;

/**
 * Provides a base class to for flat files containing multiple joining
 * records (many-to-many mappings) for each article.
 */
public abstract class MultiContentFile<V extends PubmedFlatRecord> extends DocumentContentFile<V> {
    private ListMultimap<PMID, V> recordMap = null;

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

    /**
     * Returns an immutable multimap of the records in this file
     * indexed by {@code PMID}.  The file will be generated on demand
     * if necessary.
     *
     * @return an immutable map of the records in this file indexed
     * by {@code PMID}.
     */
    public synchronized ListMultimap<PMID, V> getRecordMap() {
        if (recordMap == null)
            mapRecords();

        return recordMap;
    }

    private void mapRecords() {
        if (!exists())
            processFile(false);

        RecordStore<V> recordStore = load();
        ImmutableListMultimap.Builder<PMID, V> builder = ImmutableListMultimap.builder();

        for (V record : recordStore)
            builder.put(record.getPMID(), record);

        recordMap = builder.build();
    }

    @Override public List<V> extractRecords(PubmedXmlDocument document) {
        List<V> documentRecords = new ArrayList<V>();

        for (PubmedArticleElement element : document.getPubmedArticleElements())
            documentRecords.addAll(extractRecords(element));

        return documentRecords;
    }
}