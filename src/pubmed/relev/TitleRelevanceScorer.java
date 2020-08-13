
package pubmed.relev;

import pubmed.bulk.BulkFile;
import pubmed.flat.TitleLemmaRecord;
import pubmed.flat.TitleLemmaTable;

/**
 * Computes title relevance score records.
 */
public final class TitleRelevanceScorer extends LemmaRelevanceScorer<TitleLemmaRecord> {
    private TitleRelevanceScorer(TitleLemmaTable lemmaTable) {
        super(lemmaTable);
    }

    /**
     * Returns the relevance scorer for a given bulk XML file.
     *
     * @param bulkFile the bulk XML file to process.
     *
     * @return the relevance scorer for the given bulk XML file.
     */
    public static TitleRelevanceScorer instance(BulkFile bulkFile) {
        return instance(bulkFile.getTitleLemmaFile().load());
    }

    /**
     * Returns the relevance scorer for a table of lemma records.
     *
     * @param lemmaTable lemma records derived from a bulk XML file.
     *
     * @return the relevance scorer for the given lemma table.
     */
    public static TitleRelevanceScorer instance(TitleLemmaTable lemmaTable) {
        return new TitleRelevanceScorer(lemmaTable);
    }
}
