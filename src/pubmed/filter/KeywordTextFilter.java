
package pubmed.filter;

import java.util.Collection;

import pubmed.article.PubmedArticle;
import pubmed.nlp.LemmaList;
import pubmed.subject.Subject;

/**
 * Matches target keywords or phrases to unstructured text contained
 * in {@code pubmed} articles.
 */
public abstract class KeywordTextFilter extends ArticleFilter {
    private final Collection<LemmaList> targetLemmaLists;

    /**
     * Creates a new keyword text filter for a specific subject.
     *
     * @param subject the subject to match.
     */
    protected KeywordTextFilter(Subject subject) {
        this(subject.getKeywordLemmas());
    }

    /**
     * Creates a new keyword text filter with fixed target lemmas.
     *
     * @param targetLemmaLists the lemmas of the keywords or phrases
     * to match.
     */
    protected KeywordTextFilter(Collection<LemmaList> targetLemmaLists) {
        this.targetLemmaLists = targetLemmaLists;
    }

    /**
     * Extracts the lemmatized text from the filtered article.
     *
     * @param article the article being filtered.
     *
     * @return the lemmatized text to search.
     */
    public abstract LemmaList getText(PubmedArticle article);

    @Override public int score(PubmedArticle article) {
        int count = 0;
        LemmaList articleLemmaList = getText(article);

	for (LemmaList targetLemmaList : targetLemmaLists)
            count += articleLemmaList.countSequence(targetLemmaList);

        return count;
    }

    @Override public boolean testArticle(PubmedArticle article) {
        LemmaList articleLemmaList = getText(article);

	for (LemmaList targetLemmaList : targetLemmaLists)
            if (articleLemmaList.containsSequence(targetLemmaList))
                return true;

        return false;
    }
}
