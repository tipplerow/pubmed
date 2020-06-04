
package pubmed.sql;

import pubmed.article.PMID;
import pubmed.article.PubmedArticle;

/**
 * Represents a row in the {@code pubmed.abstracts} table.
 */
public final class AbstractRecord extends ArticleTextAttrRecord {
    private AbstractRecord(PMID pmid, String abstract_) {
        super(pmid, abstract_);
    }

    /**
     * Creates the {@code pubmed.abstracts} record for an article.
     *
     * @param article the article to store.
     *
     * @return the {@code pubmed.abstracts} record for the specified
     * article.
     */
    public static AbstractRecord create(PubmedArticle article) {
        return create(article.getPMID(), article.getAbstract());
    }

    /**
     * Creates a new abstract record from its fields.
     *
     * @param pmid the unique {@code PMID} identifier for the article.
     *
     * @param abstract_ the abstract of the article.
     *
     * @return the {@code pubmed.abstracts} record with the specified
     * fields.
     */
    public static AbstractRecord create(PMID pmid, String abstract_) {
        return new AbstractRecord(pmid, abstract_);
    }

    /**
     * Returns the abstract of the article.
     *
     * @return the abstract of the article.
     */
    public String getAbstract() {
        return text;
    }
}
