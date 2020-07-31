
package pubmed.bulk;

import java.util.Map;

import pubmed.article.PMID;
import pubmed.flat.ArticleLemmaRecord;
import pubmed.nlp.LemmaList;
import pubmed.subject.Subject;

/**
 * Base class to compute and store relevance score records for
 * lemmatized titles or abstracts.
 *
 * <p>The relevance score is the number of times a (lemmatized)
 * subject keyword or phrase occurs in the (lemmatized) article
 * component (title or abstract).
 */
public abstract class ArticleLemmaRelevanceFile<V extends ArticleLemmaRecord> extends RelevanceScoreFile {
    private final Map<PMID, V> lemmaMap;

    /**
     * Creates a new relevance file for a given bulk file and lemma
     * components.
     *
     * @param bulkFile the bulk XML file containing articles to be
     * scored.
     *
     * @param lemmaMap the article component (title or abstract)
     * lemmas extracted from the bulk file.
     */
    protected ArticleLemmaRelevanceFile(BulkFile bulkFile, Map<PMID, V> lemmaMap) {
        super(bulkFile);
        this.lemmaMap = lemmaMap; 
    }

    /**
     * Computes the relevance score for an article and subject.
     *
     * @param subject the subject of interest.
     *
     * @param articleLemmas lemmas extracted from an article.
     *
     * @return the relevance score for the article and subject.
     */
    public static int computeScore(Subject subject, LemmaList articleLemmas) {
        int score = 0;

        for (LemmaList subjectLemmas : subject.getKeywordLemmas())
            score += articleLemmas.countSequence(subjectLemmas);

        return score;
    }

    @Override public int computeScore(PMID pmid, Subject subject) {
        ArticleLemmaRecord lemmaRecord = lemmaMap.get(pmid);

        if (lemmaRecord != null)
            return computeScore(subject, lemmaRecord.getLemmaList());
        else
            return 0;
    }
}
