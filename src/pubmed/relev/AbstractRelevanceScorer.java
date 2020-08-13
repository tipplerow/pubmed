
package pubmed.relev;

import pubmed.bulk.BulkFile;
import pubmed.flat.AbstractLemmaRecord;
import pubmed.flat.AbstractLemmaTable;

/**
 * Computes abstract relevance score records.
 */
public final class AbstractRelevanceScorer extends LemmaRelevanceScorer<AbstractLemmaRecord> {
    private AbstractRelevanceScorer(AbstractLemmaTable lemmaTable) {
        super(lemmaTable);
    }

    /**
     * Returns the relevance scorer for a given bulk XML file.
     *
     * @param bulkFile the bulk XML file to process.
     *
     * @return the relevance scorer for the given bulk XML file.
     */
    public static AbstractRelevanceScorer instance(BulkFile bulkFile) {
        return instance(bulkFile.getAbstractLemmaFile().load());
    }

    /**
     * Returns the relevance scorer for a table of lemma records.
     *
     * @param lemmaTable lemma records derived from a bulk XML file.
     *
     * @return the relevance scorer for the given lemma table.
     */
    public static AbstractRelevanceScorer instance(AbstractLemmaTable lemmaTable) {
        return new AbstractRelevanceScorer(lemmaTable);
    }
}
