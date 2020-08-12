
package pubmed.bulk;

import pubmed.flat.ArticleDOIRecord;
import pubmed.flat.ArticleDOITable;
import pubmed.xml.PubmedArticleElement;

/**
 * Reads and writes flat files containing article DOIs.
 */
public final class ArticleDOIFile extends UniqueContentFile<ArticleDOIRecord> {
    private ArticleDOIFile(BulkFile bulkFile) {
        super(bulkFile);
    }

    /**
     * The suffix for flat file names.
     */
    public static final String SUFFIX = "doi";

    /**
     * Returns the flat file derived from a bulk XML file.
     *
     * @param bulkFile a bulk XML file.
     *
     * @return the flat file derived from the given bulk XML file.
     */
    public static ArticleDOIFile from(BulkFile bulkFile) {
        return new ArticleDOIFile(bulkFile);
    }

    @Override public ArticleDOIRecord extractRecord(PubmedArticleElement element) {
        return ArticleDOIRecord.from(element);
    }

    @Override public String getSuffix() {
        return SUFFIX;
    }

    @Override public ArticleDOITable load() {
        return (ArticleDOITable) super.load();
    }

    @Override public ArticleDOITable newStore() {
        return new ArticleDOITable();
    }
}
