
package pubmed.bulk;

import pubmed.flat.ArticleAbstractRecord;
import pubmed.flat.ArticleAbstractTable;
import pubmed.xml.PubmedArticleElement;

/**
 * Reads and writes flat files containing article abstracts.
 */
public final class ArticleAbstractFile extends UniqueContentFile<ArticleAbstractRecord> {
    private ArticleAbstractFile(BulkFile bulkFile) {
        super(bulkFile);
    }

    /**
     * The suffix for flat file names.
     */
    public static final String SUFFIX = "abstract";

    /**
     * Returns the flat file derived from a bulk XML file.
     *
     * @param bulkFile a bulk XML file.
     *
     * @return the flat file derived from the given bulk XML file.
     */
    public static ArticleAbstractFile instance(BulkFile bulkFile) {
        return new ArticleAbstractFile(bulkFile);
    }

    @Override public ArticleAbstractRecord extractRecord(PubmedArticleElement element) {
        return ArticleAbstractRecord.from(element);
    }

    @Override public String getSuffix() {
        return SUFFIX;
    }

    @Override public ArticleAbstractTable load() {
        return (ArticleAbstractTable) super.load();
    }

    @Override public ArticleAbstractTable newStore() {
        return new ArticleAbstractTable();
    }
}
