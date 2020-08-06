
package pubmed.bulk;

import pubmed.flat.AbstractLemmaRecord;

/**
 * Computes and stores abstract relevance score records.
 */
public final class AbstractRelevanceFile extends ArticleLemmaRelevanceFile<AbstractLemmaRecord> {
    private AbstractRelevanceFile(BulkFile bulkFile) {
        super(bulkFile, bulkFile.getAbstractLemmaFile().load());
    }

    /**
     * The suffix for flat file names.
     */
    public static final String SUFFIX = "abstract_relevance";

    /**
     * Returns the flat file derived from a bulk XML file.
     *
     * @param bulkFile a bulk XML file.
     *
     * @return the flat file derived from the given bulk XML file.
     */
    public static AbstractRelevanceFile from(BulkFile bulkFile) {
        return new AbstractRelevanceFile(bulkFile);
    }

    @Override public String getSuffix() {
        return SUFFIX;
    }
}
