
package pubmed.sql;

import pubmed.article.PMID;

/**
 * Represents a row composed of a {@code PMID} identifier and a text
 * column.
 */
public abstract class ArticleTextAttrRecord extends ArticleAttrRecord {
    /**
     * The text entry for this record.
     */
    protected final String text;

    /**
     * Creates a new record with immutable components.
     *
     * @param pmid the {@code PMID} key for the record.
     *
     * @param text the text entry for the record.
     */
    protected ArticleTextAttrRecord(PMID pmid, String text) {
	super(pmid);
	this.text = text;
    }

    /**
     * Returns the text entry for this record.
     *
     * @return the text entry for this record.
     */
    public String getText() {
        return text;
    }

    @Override public String formatBulk() {
        return joinBulk(formatBulk(pmid), formatBulk(text));
    }
}
