
package pubmed.filter;

import java.util.Collection;

import pubmed.article.PubmedArticle;
import pubmed.nlp.LemmaList;
import pubmed.subject.Subject;

/**
 * Matches target keywords or phrases to unstructured text contained
 * in {@code pubmed} articles.
 */
public final class KeywordAbstractFilter extends KeywordTextFilter {
    private KeywordAbstractFilter(Subject subject) {
        super(subject);
    }

    private KeywordAbstractFilter(Collection<LemmaList> targetLemmaLists) {
        super(targetLemmaLists);
    }

    /**
     * Creates a new keyword abstract filter for a specific subject.
     *
     * @param subject the subject to match.
     *
     * @return a new keyword abstract filter for the specified subject.
     */
    public static KeywordAbstractFilter create(Subject subject) {
        return new KeywordAbstractFilter(subject);
    }

    /**
     * Creates a new keyword abstract filter with fixed target lemmas.
     *
     * @param targetLemmaLists the lemmas of the keywords or phrases
     * to match.
     *
     * @return a new keyword abstract filter for the specified target
     * lemmas.
     */
    public static KeywordAbstractFilter create(Collection<LemmaList> targetLemmaLists) {
        return new KeywordAbstractFilter(targetLemmaLists);
    }

    @Override public LemmaList getText(PubmedArticle article) {
        return article.getAbstractLemmas();
    }
}
