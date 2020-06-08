
package pubmed.filter;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import pubmed.article.PubmedArticle;
import pubmed.nlp.LemmaList;
import pubmed.subject.Subject;

/**
 * Identifies articles with keyword lists that match a set of target
 * keywords or phrases.
 */
public final class KeywordListFilter extends ArticleFilter {
    private final Set<String> targetWords = new HashSet<String>();
    private final Set<LemmaList> targetLemmas = new HashSet<LemmaList>();

    private KeywordListFilter(Collection<String> targetWords,
                              Collection<LemmaList> targetLemmas) {
        this.targetWords.addAll(targetWords);
        this.targetLemmas.addAll(targetLemmas);
    }

    /**
     * Creates a new keyword list filter that matches a single
     * subject.
     *
     * @param subject the subject to match.
     *
     * @return a new keyword list filter for the specified subject.
     */
    public static KeywordListFilter create(Subject subject) {
        return new KeywordListFilter(subject.getKeywords(), subject.getKeywordLemmas());
    }

    /**
     * Creates a new keyword list filter with fixed targets.
     *
     * @param targets the keywords or phrases to match.
     *
     * @return a new keyword list filter with the specified targets.
     */
    public static KeywordListFilter create(String... targets) {
        return create(Set.of(targets));
    }

    /**
     * Creates a new keyword list filter with fixed targets.
     *
     * @param targets the keywords or phrases to match.
     *
     * @return a new keyword list filter with the specified targets.
     */
    public static KeywordListFilter create(Collection<String> targets) {
        return new KeywordListFilter(targets, LemmaList.contentWords(targets));
    }

    @Override public int score(PubmedArticle article) {
        if (article.hasKeywordList())
            return super.score(article);
        else
            return 0;
    }

    @Override public boolean testArticle(PubmedArticle article) {
	for (String word : article.viewKeywordList())
	    if (targetWords.contains(word))
		return true;

	for (LemmaList lemma : article.viewKeywordLemmas())
	    if (targetLemmas.contains(lemma))
		return true;

        return false;
    }
}
