
package pubmed.flat;

import java.time.LocalDate;
import java.util.List;

import pubmed.article.PMID;

/**
 * Represents a data record containing the identifier of an article
 * and a date associated with the article (e.g., a publication date).
 */
public abstract class ArticleDateRecord extends PubmedJoinRecord<LocalDate> {
    /**
     * Creates a new record with a fixed identifier and date.
     *
     * @param pmid the unique article identifier.
     *
     * @param date a date associated with the article.
     */
    protected ArticleDateRecord(PMID pmid, LocalDate date) {
        super(pmid, date);
    }

    /**
     * Returns the date associated with the article.
     *
     * @return the date associated with the article.
     */
    public LocalDate getDate() {
        return fkey;
    }

    @Override public List<String> formatFields() {
        return List.of(format(pmid), format(getDate()));
    }
}
