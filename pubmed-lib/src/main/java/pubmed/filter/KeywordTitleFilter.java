
package pubmed.filter;

import java.util.Collection;

import pubmed.article.PubmedArticle;
import pubmed.nlp.LemmaList;
import pubmed.subject.Subject;

/**
 * Matches target keywords or phrases to unstructured text contained
 * in {@code pubmed} articles.
 */
public final class KeywordTitleFilter extends KeywordTextFilter {
    private KeywordTitleFilter(Subject subject) {
        super(subject);
    }

    private KeywordTitleFilter(Collection<LemmaList> targetLemmaLists) {
        super(targetLemmaLists);
    }

    /**
     * Creates a new keyword title filter for a specific subject.
     *
     * @param subject the subject to match.
     *
     * @return a new keyword title filter for the specified subject.
     */
    public static KeywordTitleFilter create(Subject subject) {
        return new KeywordTitleFilter(subject);
    }

    /**
     * Creates a new keyword title filter with fixed target lemmas.
     *
     * @param targetLemmaLists the lemmas of the keywords or phrases
     * to match.
     *
     * @return a new keyword title filter for the specified target
     * lemmas.
     */
    public static KeywordTitleFilter create(Collection<LemmaList> targetLemmaLists) {
        return new KeywordTitleFilter(targetLemmaLists);
    }

    @Override public LemmaList getText(PubmedArticle article) {
        return article.getTitleLemmas();
    }
}
