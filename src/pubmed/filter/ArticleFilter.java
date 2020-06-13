
package pubmed.filter;

import java.util.function.Predicate;

import jam.app.JamLogger;

import pubmed.article.PubmedArticle;

/**
 * Identifies {@code PubMed} articles meeting specific selection criteria.
 */
public abstract class ArticleFilter implements Predicate<PubmedArticle> {
    private static boolean verbose = false;

    /**
     * Returns a no-op filter that passes all articles.
     *
     * @return a no-op filter that passes all articles.
     */
    public static ArticleFilter all() {
        return new ArticleFilter() {
            @Override public boolean testArticle(PubmedArticle article) {
                return true;
            };
        };
    }

    /**
     * Returns a filter that fails all articles.
     *
     * @return a filter that fails all articles.
     */
    public static ArticleFilter none() {
        return new ArticleFilter() {
            @Override public boolean testArticle(PubmedArticle article) {
                return false;
            };
        };
    }

    /**
     * Returns a filter that assigns a zero score to all articles.
     *
     * @return a filter that fails all articles and assigns a zero
     * score.
     */
    public static ArticleFilter zero() {
        return new ArticleFilter() {
            @Override public int score(PubmedArticle article) {
                return 0;
            }

            @Override public boolean testArticle(PubmedArticle article) {
                return false;
            };
        };
    }

    /**
     * Applies the filter test to an article.
     *
     * @param article the article to test.
     *
     * @return the result of the filter test.
     */
    public abstract boolean testArticle(PubmedArticle article);

    /**
     * Generates an integral score for an article.
     *
     * @param article the article to score.
     *
     * @return {@code -1} if the article fails the filter test;
     * {@code +1} if the article passes.
     */
    public int score(PubmedArticle article) {
        if (test(article))
            return +1;
        else
            return -1;
    }

    /**
     * Returns the verbosity flag.
     *
     * @return the verbosity flag.
     */
    public static boolean verbose() {
        return verbose;
    }

    /**
     * Assigns the verbosity flag.
     *
     * @param flag the new verbosity flag.
     */
    public static void verbose(boolean flag) {
        verbose = flag;
    }

    @Override public boolean test(PubmedArticle article) {
        try {
            return testArticle(article);
        }
        catch (Exception ex) {
            JamLogger.warn("Article test exception: [%s].", ex.getMessage());
            return false;
        }
    }
}
