
package pubmed.sql;

import java.util.ArrayList;
import java.util.List;

import jam.util.ListUtil;

import pubmed.article.PMID;
import pubmed.article.PubmedArticle;
import pubmed.nlp.LemmaAnnotator;

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

        // An occasional article has duplicate keywords...
        List<String> keywords = ListUtil.unique(article.viewKeywordList());

        List<KeywordRecord> records =
            new ArrayList<KeywordRecord>(keywords.size());

        for (String keyword : keywords)
            records.add(create(article.getPMID(), LemmaAnnotator.lemmatize(keyword)));

        return records;
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
