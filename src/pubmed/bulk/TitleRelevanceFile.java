
package pubmed.bulk;

import pubmed.flat.TitleLemmaRecord;

/**
 * Computes and stores title relevance score records.
 */
public final class TitleRelevanceFile extends ArticleLemmaRelevanceFile<TitleLemmaRecord> {
    private TitleRelevanceFile(BulkFile bulkFile) {
        super(bulkFile, bulkFile.getTitleLemmaFile().load());
    }

    /**
     * The suffix for flat file names.
     */
    public static final String SUFFIX = "title_relevance";

    /**
     * Returns the flat file derived from a bulk XML file.
     *
     * @param bulkFile a bulk XML file.
     *
     * @return the flat file derived from the given bulk XML file.
     */
    public static TitleRelevanceFile from(BulkFile bulkFile) {
        return new TitleRelevanceFile(bulkFile);
    }

    @Override public String getSuffix() {
        return SUFFIX;
    }
}
