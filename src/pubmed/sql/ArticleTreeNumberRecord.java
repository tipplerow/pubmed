
package pubmed.sql;

import java.util.List;

import jam.util.ListUtil;

import pubmed.article.PMID;
import pubmed.article.PubmedArticle;
import pubmed.mesh.MeshTreeNumber;

/**
 * Represents a row in the {@code article_tree_numbers} table.
 */
public final class ArticleTreeNumberRecord extends ArticleTextJoinRecord {
    private ArticleTreeNumberRecord(PMID pmid, String number) {
        super(pmid, number);
    }

    /**
     * Creates a new tree number record from its fields.
     *
     * @param pmid the unique identifier of the indexed article.
     *
     * @param number the string representation of the tree number.
     *
     * @return the {@code article_tree_numbers} record with the specified
     * fields.
     */
    public static ArticleTreeNumberRecord create(PMID pmid, String number) {
        return new ArticleTreeNumberRecord(pmid, number);
    }

    /**
     * Creates a new tree number record from its fields.
     *
     * @param pmid the unique identifier of the indexed article.
     *
     * @param number a {@code MeSH} tree number corresponding to a
     * descriptor or qualifier contained in the article.
     *
     * @return the {@code article_tree_numbers} record with the specified
     * fields.
     */
    public static ArticleTreeNumberRecord create(PMID pmid, MeshTreeNumber number) {
        return new ArticleTreeNumberRecord(pmid, number.getKey());
    }

    /**
     * Creates records for all tree numbers contained in an article.
     *
     * @param article the article to index.
     *
     * @return a list of {@code article_tree_numbers} records for the
     * specified article.
     */
    public static List<ArticleTreeNumberRecord> list(PubmedArticle article) {
        return ListUtil.apply(article.getTreeNumbers(), number -> create(article.getPMID(), number));
    }

    /**
     * Returns the {@code MeSH} tree number indexed by this record.
     *
     * @return the {@code MeSH} tree number indexed by this record.
     */
    public MeshTreeNumber getTreeNumber() {
        return MeshTreeNumber.instance(key2);
    }
}
