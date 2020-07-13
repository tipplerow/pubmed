
package pubmed.flat;

import java.util.List;

import pubmed.article.PMID;

/**
 * Represents a data record containing the identifier of a
 * {@code PubMed} article and a single text attribute.
 */
public abstract class ArticleTextAttrRecord extends PubmedFlatRecord {
    /**
     * The text attribute for the article.
     */
    protected final String text;

    /**
     * Creates a new record with a fixed key and attribute.
     *
     * @param pmid the primary key for the record.
     *
     * @param text the text attribute for the record.
     */
    protected ArticleTextAttrRecord(PMID pmid, String text) {
        super(pmid);
        this.text = text;
        validateText();
    }

    private void validateText() {
        if (text == null)
            throw new NullPointerException("Null text attribute.");
    }

    @Override public List<String> formatFields() {
        return List.of(format(pmid), format(text));
    }
}
