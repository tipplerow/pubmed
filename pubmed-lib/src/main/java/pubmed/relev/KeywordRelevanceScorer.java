
package pubmed.relev;

import pubmed.article.PMID;
import pubmed.bulk.BulkFile;
import pubmed.flat.KeywordTable;
import pubmed.nlp.LemmaList;
import pubmed.subject.Subject;

/**
 * Computes keyword relevance score records.
 *
 * <p>If the article has a keyword list, the score is {@code +1} if
 * the list contains a subject keyword or phrase, {@code -1} if it
 * does not.  The score is {@code 0} if the article does not have a
 * keyword list.
 */
public final class KeywordRelevanceScorer implements RelevanceScorer {
    private final KeywordTable table;

    private KeywordRelevanceScorer(KeywordTable table) {
        this.table = table;
    }

    /**
     * Returns the relevance scorer for a given bulk XML file.
     *
     * @param bulkFile the bulk XML file to process.
     *
     * @return the relevance scorer for the given bulk XML file.
     */
    public static KeywordRelevanceScorer instance(BulkFile bulkFile) {
        return instance(bulkFile.getKeywordFile().load());
    }

    /**
     * Returns the relevance scorer for a table of keyword records.
     *
     * @param table keyword records derived from a bulk XML file.
     *
     * @return the relevance scorer for the given lemma table.
     */
    public static KeywordRelevanceScorer instance(KeywordTable table) {
        return new KeywordRelevanceScorer(table);
    }

    @Override public int computeScore(PMID pmid, Subject subject) {
        if (!table.containsPrimary(pmid))
            return 0;

        for (LemmaList lemmaList : subject.getKeywordLemmas()) {
            String keyword = lemmaList.join();

            if (table.contains(pmid, keyword))
                return +1;
        }

        return -1;
    }
}
