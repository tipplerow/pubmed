
package pubmed.bulk;

import java.util.Collection;

import pubmed.article.PMID;
import pubmed.flat.KeywordRecord;
import pubmed.flat.KeywordTable;
import pubmed.nlp.LemmaList;
import pubmed.subject.Subject;

/**
 * Computes and stores keyword relevance score records.
 *
 * <p>If the article has a keyword list, the score is {@code +1} if
 * the list contains a subject keyword or phrase, {@code -1} if it
 * does not.  The score is {@code 0} if the article does not have a
 * keyword list.
 */
public final class KeywordRelevanceFile extends RelevanceScoreFile {
    private final KeywordTable table;

    private KeywordRelevanceFile(BulkFile bulkFile) {
        super(bulkFile);
        this.table = bulkFile.getKeywordFile().load();
    }

    /**
     * The suffix for flat file names.
     */
    public static final String SUFFIX = "keyword_relevance";

    /**
     * Returns the flat file derived from a bulk XML file.
     *
     * @param bulkFile a bulk XML file.
     *
     * @return the flat file derived from the given bulk XML file.
     */
    public static KeywordRelevanceFile from(BulkFile bulkFile) {
        return new KeywordRelevanceFile(bulkFile);
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

    @Override public String getSuffix() {
        return SUFFIX;
    }
}
