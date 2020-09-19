
package pubmed.bulk;

import java.util.List;

import pubmed.flat.HeadingRecord;
import pubmed.flat.HeadingTable;
import pubmed.xml.PubmedArticleElement;

/**
 * Reads and writes joining files for articles and {@code MeSH}
 * descriptors.
 */
public final class HeadingFile extends MultiContentFile<HeadingRecord> {
    private HeadingFile(BulkFile bulkFile) {
        super(bulkFile);
    }

    /**
     * The suffix for flat file names.
     */
    public static final String SUFFIX = "heading";

    /**
     * Returns the flat file derived from a bulk XML file.
     *
     * @param bulkFile a bulk XML file.
     *
     * @return the flat file derived from the given bulk XML file.
     */
    public static HeadingFile instance(BulkFile bulkFile) {
        return new HeadingFile(bulkFile);
    }

    @Override public List<HeadingRecord> extractRecords(PubmedArticleElement element) {
        return HeadingRecord.from(element);
    }

    @Override public String getSuffix() {
        return SUFFIX;
    }

    @Override public HeadingTable load() {
        return (HeadingTable) super.load();
    }

    @Override public HeadingTable newStore() {
        return new HeadingTable();
    }
}
