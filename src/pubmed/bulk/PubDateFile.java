
package pubmed.bulk;

import java.util.List;

import pubmed.flat.PubDateRecord;
import pubmed.flat.PubDateTable;
import pubmed.xml.PubmedArticleElement;

/**
 * Reads and writes joining files for articles and publication dates.
 */
public final class PubDateFile extends MultiContentFile<PubDateRecord> {
    private PubDateFile(BulkFile bulkFile) {
        super(bulkFile);
    }

    /**
     * The suffix for flat file names.
     */
    public static final String SUFFIX = "pub_date";

    /**
     * Returns the flat file derived from a bulk XML file.
     *
     * @param bulkFile a bulk XML file.
     *
     * @return the flat file derived from the given bulk XML file.
     */
    public static PubDateFile instance(BulkFile bulkFile) {
        return new PubDateFile(bulkFile);
    }

    @Override public List<PubDateRecord> extractRecords(PubmedArticleElement element) {
        PubDateRecord record = PubDateRecord.from(element);

        if (record != null)
            return List.of(record);
        else
            return List.of();
    }

    @Override public String getSuffix() {
        return SUFFIX;
    }

    @Override public PubDateTable load() {
        return (PubDateTable) super.load();
    }

    @Override public PubDateTable newStore() {
        return new PubDateTable();
    }
}
