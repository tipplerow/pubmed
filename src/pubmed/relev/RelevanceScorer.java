
package pubmed.relev;

import pubmed.article.PMID;
import pubmed.subject.Subject;

/**
 * Defines an interface for the calculation of relevance scores for
 * articles and subjects.
 */
public interface RelevanceScorer {
    /**
     * Computes the relevance score for an article and subject.
     *
     * @param pmid the identifier of the article to score.
     *
     * @param subject the subject of interest.
     *
     * @return the relevance score for the specified article and
     * subject.
     */
    public abstract int computeScore(PMID pmid, Subject subject);
}
