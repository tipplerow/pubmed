
package pubmed.sql;

import java.util.Collection;
import java.util.List;

import pubmed.article.PubmedArticle;

/**
 * Maintains the {@code abstract_lemmas} table: a many-to-many mapping
 * between articles and the lemmatized words in its abstract.
 */
public final class AbstractLemmaTable extends ArticleLemmaTable {
    private static AbstractLemmaTable instance = null;

    private AbstractLemmaTable() {
        super();
    }

    /**
     * The name of the {@code abstract_lemmas} table.
     */
    public static final String TABLE_NAME = "abstract_lemmas";

    /**
     * Returns the single instance.
     *
     * @return the single instance.
     */
    public static synchronized AbstractLemmaTable instance() {
        if (instance == null)
            instance = new AbstractLemmaTable();

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
        if (article.hasAbstract())
            return ArticleLemmaRecord.nouns(article.getPMID(), article.getAbstract());
        else
            return List.of();
    }

    @Override public String getTableName() {
        return TABLE_NAME;
    }
}
