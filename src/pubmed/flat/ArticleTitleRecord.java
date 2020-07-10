
package pubmed.flat;

import jam.flat.FlatRecord;

import pubmed.article.PMID;
import pubmed.xml.PubmedArticleElement;

/**
 * Represents a data record containing the identifier and title of a
 * {@code PubMed} article.
 */
public final class ArticleTitleRecord extends ArticleTextAttrRecord {
    private ArticleTitleRecord(PMID pmid, String title) {
        super(pmid, title);
    }

    /**
     * Creates a new record with a fixed identifier and title.
     *
     * @param pmid the unique article identifier.
     *
     * @param title the article title.
     *
     * @return a new record for the specified identifier and title.
     */
    public static ArticleTitleRecord create(PMID pmid, String title) {
        return new ArticleTitleRecord(pmid, title);
    }

    /**
     * Creates a new record from a parsed XML article element.
     *
     * @param element a parsed XML article element.
     *
     * @return a new record with data extracted from the given
     * article element.
     */
    public static ArticleTitleRecord from(PubmedArticleElement element) {
        return create(element.getPMID(), element.getArticleTitle());
    }

    /**
     * Parses a delimited line that encodes a title record.
     *
     * @param line the delimited line to parse.
     *
     * @return a new record with the data encoded in the line.
     */
    public static ArticleTitleRecord parse(String line) {
        String[] fields = FlatRecord.split(line, 2);
        return create(PMID.instance(fields[0]), fields[1]);
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
