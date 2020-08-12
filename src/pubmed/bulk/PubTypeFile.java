
package pubmed.bulk;

import java.util.List;

import pubmed.flat.PubTypeRecord;
import pubmed.flat.PubTypeTable;
import pubmed.xml.PubmedArticleElement;

/**
 * Reads and writes joining files for articles and publication types.
 */
public final class PubTypeFile extends MultiContentFile<PubTypeRecord> {
    private PubTypeFile(BulkFile bulkFile) {
        super(bulkFile);
    }

    /**
     * The suffix for flat file names.
     */
    public static final String SUFFIX = "pub_type";

    /**
     * Returns the flat file derived from a bulk XML file.
     *
     * @param bulkFile a bulk XML file.
     *
     * @return the flat file derived from the given bulk XML file.
     */
    public static PubTypeFile from(BulkFile bulkFile) {
        return new PubTypeFile(bulkFile);
    }

    @Override public List<PubTypeRecord> extractRecords(PubmedArticleElement element) {
        return PubTypeRecord.from(element);
    }

    @Override public String getSuffix() {
        return SUFFIX;
    }

    @Override public PubTypeTable load() {
        return (PubTypeTable) super.load();
    }

    @Override public PubTypeTable newStore() {
        return new PubTypeTable();
    }
}
