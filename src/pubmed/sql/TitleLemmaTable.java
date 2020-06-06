
package pubmed.sql;

import java.util.Collection;
import java.util.List;

import pubmed.article.PubmedArticle;

/**
 * Maintains the {@code title_lemmas} table: a many-to-many mapping
 * between articles and the lemmatized words in its title.
 */
public final class TitleLemmaTable extends ArticleLemmaTable {
    private static TitleLemmaTable instance = null;

    private TitleLemmaTable() {
        super();
    }

    /**
     * The name of the {@code title_lemmas} table.
     */
    public static final String TABLE_NAME = "title_lemmas";

    /**
     * Returns the single instance.
     *
     * @return the single instance.
     */
    public static synchronized TitleLemmaTable instance() {
        if (instance == null)
            instance = new TitleLemmaTable();

        instance.require();
        return instance;
    }

    /**
     * Drops the physical database table.
     */
    public static synchronized void dropTable() {
        DbEnv.activeDb().dropTable(TABLE_NAME);
    }

    @Override public Collection<ArticleLemmaRecord> getBulkRecords(PubmedArticle article) {
        if (article.hasTitle())
            return ArticleLemmaRecord.nouns(article.getPMID(), article.getTitle());
        else
            return List.of();
    }

    @Override public String getTableName() {
        return TABLE_NAME;
    }
}
