
package pubmed.sql;

import pubmed.article.PMID;
import pubmed.article.PubmedArticle;

/**
 * Represents a row in the {@code titles} table.
 */
public final class TitleRecord extends ArticleTextAttrRecord {
    private TitleRecord(PMID pmid, String title) {
        super(pmid, title);
    }

    /**
     * Creates the {@code titles} record for an article.
     *
     * @param article the article to store.
     *
     * @return the {@code titles} record for the specified article.
     */
    public static TitleRecord create(PubmedArticle article) {
        return create(article.getPMID(), article.getTitle());
    }

    /**
     * Creates a new title record from its fields.
     *
     * @param pmid the unique {@code PMID} identifier for the article.
     *
     * @param title the title of the article.
     *
     * @return the {@code titles} record with the specified fields.
     */
    public static TitleRecord create(PMID pmid, String title) {
        return new TitleRecord(pmid, title);
    }

    /**
     * Returns the title of the article.
     *
     * @return the title of the article.
     */
    public String getTitle() {
        return text;
    }
}
