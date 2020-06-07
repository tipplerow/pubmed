
package pubmed.filter;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import pubmed.article.PubmedArticle;
import pubmed.nlp.LemmaAnnotator;

/**
 * Identifies articles with keyword lists that match a set of target
 * keywords or phrases.
 */
public final class KeywordListFilter extends ArticleFilter {
    private final Set<String> targets = new HashSet<String>();

    private KeywordListFilter(Collection<String> targets) {
        this.targets.addAll(LemmaAnnotator.lemmatize(targets));
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
        return new KeywordListFilter(targets);
    }

    @Override public int score(PubmedArticle article) {
        if (article.hasKeywordList())
            return super.score(article);
        else
            return 0;
    }

    @Override public boolean testArticle(PubmedArticle article) {
	for (String lemma : article.viewKeywordLemmas())
	    if (targets.contains(lemma))
		return true;

        return false;
    }
}
