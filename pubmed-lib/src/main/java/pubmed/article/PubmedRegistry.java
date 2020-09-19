
package pubmed.article;

import java.util.Collection;

import jam.lang.JamException;
import jam.lang.ObjectRegistry;

/**
 * Represents one article from the {@code PubMed} database.
 */
public final class PubmedRegistry extends ObjectRegistry<PMID, PubmedArticle> {
    private static PubmedRegistry GLOBAL = new PubmedRegistry();

    private PubmedRegistry() {
        super(PMID.class, PubmedArticle.class, article -> article.getPMID());
    }

    /**
     * Returns a new, empty registry.
     *
     * @return a new, empty registry.
     */
    public static PubmedRegistry create() {
        return new PubmedRegistry();
    }

    /**
     * Returns a new registry and populates it with articles.
     *
     * @param articles the articles to add to the registry.
     *
     * @return a new registry containing the specified articles.
     *
     * @throws RuntimeException unless all articles have unique PMIDs.
     */
    public static PubmedRegistry create(Collection<PubmedArticle> articles) {
        PubmedRegistry registry = create();
        registry.addAll(articles);
        return registry;
    }

    /**
     * Returns the single global registry.
     *
     * @return the single global registry.
     */
    public static PubmedRegistry global() {
        return GLOBAL;
    }

    /**
     * Identifies {@code PubMed} articles in the global registry.
     *
     * @param pmid the {@code PubMed} identifier for the article
     * of interest.
     *
     * @return {@code true} iff an article with the identifier has
     * been created and stored in the global registry.
     */
    public static boolean exists(PMID pmid) {
        return GLOBAL.contains(pmid);
    }

    /**
     * Retrieves an article from the global registry.
     *
     * @param pmid the {@code PubMed} identifier for the article of
     * interest.
     *
     * @return the article with the specified identifier.
     *
     * @throws RuntimeException if there is no matching article.
     */
    public static PubmedArticle instance(PMID pmid) {
        PubmedArticle article = GLOBAL.get(pmid);

        if (article != null)
            return article;
        else
            throw JamException.runtime("Missing article: [%s].", pmid);
    }
}
