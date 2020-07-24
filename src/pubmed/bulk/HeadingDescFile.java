
package pubmed.bulk;

import java.util.List;

import pubmed.flat.HeadingDescRecord;
import pubmed.flat.HeadingDescTable;
import pubmed.xml.PubmedArticleElement;

/**
 * Reads and writes joining files for articles and chemical
 * substances.
 */
public final class HeadingDescFile extends MultiContentFile<HeadingDescRecord> {
    private HeadingDescFile(BulkFile bulkFile) {
        super(bulkFile);
    }

    /**
     * The suffix for flat file names.
     */
    public static final String SUFFIX = "heading_desc";

    /**
     * Returns the flat file derived from a bulk XML file.
     *
     * @param bulkFile a bulk XML file.
     *
     * @return the flat file derived from the given bulk XML file.
     */
    public static HeadingDescFile from(BulkFile bulkFile) {
        return new HeadingDescFile(bulkFile);
    }

    @Override public List<HeadingDescRecord> extractRecords(PubmedArticleElement element) {
        return HeadingDescRecord.from(element);
    }

    @Override public String getSuffix() {
        return SUFFIX;
    }

    @Override public HeadingDescTable load() {
        return (HeadingDescTable) super.load();
    }

    @Override public HeadingDescTable newStore() {
        return new HeadingDescTable();
    }
}
