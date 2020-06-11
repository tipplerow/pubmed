
package pubmed.sql;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import jam.util.ListUtil;

import pubmed.article.PMID;
import pubmed.article.PubmedArticle;
import pubmed.nlp.LemmaList;

/**
 * Represents a row in the {@code keywords} table.
 */
public final class KeywordRecord extends ArticleTextJoinRecord {
    private KeywordRecord(PMID pmid, String keyword) {
        super(pmid, keyword);
    }

    /**
     * Creates a new keyword record from its fields.
     *
     * @param pmid the unique identifier of the indexed article.
     *
     * @param keyword a keyword contained in the indexed article.
     *
     * @return the {@code keywords} record with the specified
     * fields.
     */
    public static KeywordRecord create(PMID pmid, String keyword) {
        return new KeywordRecord(pmid, keyword);
    }

    /**
     * Creates {@code keywords} records for all keywords contained in
     * an article.
     *
     * @param article the article to index.
     *
     * @return a list of {@code keywords} records for the specified
     * article.
     */
    public static List<KeywordRecord> list(PubmedArticle article) {
        if (!article.hasKeywordList())
            return List.of();

        // An occasional article has duplicate keywords, and
        // lemmatizing might create duplicates, so accumulate
        // lemmatized keywords in a set...
        Set<String> keywords = new TreeSet<String>();

        for (LemmaList lemmaList : article.viewKeywordLemmas())
            keywords.add(lemmaList.join());

        return ListUtil.apply(keywords, keyword -> create(article.getPMID(), keyword));
    }

    /**
     * Returns the keyword contained in the article indexed by this
     * record.
     *
     * @return the keyword contained in the article indexed by this
     * record.
     */
    public String getKeyword() {
        return key2;
    }
}
