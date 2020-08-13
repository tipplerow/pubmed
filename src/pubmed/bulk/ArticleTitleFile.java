
package pubmed.bulk;

import pubmed.flat.ArticleTitleRecord;
import pubmed.flat.ArticleTitleTable;
import pubmed.xml.PubmedArticleElement;

/**
 * Reads and writes flat files containing article titles.
 */
public final class ArticleTitleFile extends UniqueContentFile<ArticleTitleRecord> {
    private ArticleTitleFile(BulkFile bulkFile) {
        super(bulkFile);
    }

    /**
     * The suffix for flat file names.
     */
    public static final String SUFFIX = "title";

    /**
     * Returns the flat file derived from a bulk XML file.
     *
     * @param bulkFile a bulk XML file.
     *
     * @return the flat file derived from the given bulk XML file.
     */
    public static ArticleTitleFile instance(BulkFile bulkFile) {
        return new ArticleTitleFile(bulkFile);
    }

    @Override public ArticleTitleRecord extractRecord(PubmedArticleElement element) {
        return ArticleTitleRecord.from(element);
    }

    @Override public String getSuffix() {
        return SUFFIX;
    }

    @Override public ArticleTitleTable load() {
        return (ArticleTitleTable) super.load();
    }

    @Override public ArticleTitleTable newStore() {
        return new ArticleTitleTable();
    }
}
