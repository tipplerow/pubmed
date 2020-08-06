
package pubmed.bulk;

import java.util.Collection;

import com.google.common.collect.Multimap;

import pubmed.article.PMID;
import pubmed.flat.KeywordRecord;
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
    private final Multimap<PMID, KeywordRecord> recordMap;

    private KeywordRelevanceFile(BulkFile bulkFile) {
        super(bulkFile);
        this.recordMap = bulkFile.getKeywordFile().getRecordMap();
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
        Collection<KeywordRecord> records = recordMap.get(pmid);
        
        if (records.isEmpty())
            return 0;

        for (KeywordRecord record : records)
            for (LemmaList lemmaList : subject.getKeywordLemmas())
                if (record.getKeyword().equals(lemmaList.join()))
                    return 1;

        return -1;
    }

    @Override public String getSuffix() {
        return SUFFIX;
    }
}
