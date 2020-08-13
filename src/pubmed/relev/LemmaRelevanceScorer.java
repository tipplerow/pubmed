
package pubmed.relev;

import jam.flat.FlatTable;

import pubmed.article.PMID;
import pubmed.flat.ArticleLemmaRecord;
import pubmed.nlp.LemmaList;
import pubmed.subject.Subject;

/**
 * Base class to compute relevance scores for lemmatized titles
 * or abstracts.
 *
 * <p>The relevance score is the number of times a (lemmatized)
 * subject keyword or phrase occurs in the (lemmatized) article
 * component (title or abstract).
 */
public abstract class LemmaRelevanceScorer<V extends ArticleLemmaRecord> implements RelevanceScorer {
    private final FlatTable<PMID, V> lemmaTable;

    /**
     * Creates a new relevance scorer for table of lemma records.
     *
     * @param lemmaTable lemma records derived from a bulk XML file.
     */
    protected LemmaRelevanceScorer(FlatTable<PMID, V> lemmaTable) {
        this.lemmaTable = lemmaTable;
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
        ArticleLemmaRecord lemmaRecord = lemmaTable.select(pmid);

        if (lemmaRecord != null)
            return computeScore(subject, lemmaRecord.getLemmaList());
        else
            return 0;
    }
}
