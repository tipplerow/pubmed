
package pubmed.flat;

import java.util.List;

import jam.lang.ObjectUtil;

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
    }

    @Override public List<String> formatFields() {
        return List.of(format(pmid), format(text));
    }

    @Override public boolean equals(Object obj) {
        return ObjectUtil.equalsClass(this, obj);
    }
}
