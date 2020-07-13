
package pubmed.flat;

import jam.flat.FlatRecord;

import pubmed.article.PMID;
import pubmed.xml.PubmedArticleElement;

/**
 * Represents a data record containing the identifier and abstract of a
 * {@code PubMed} article.
 */
public final class ArticleAbstractRecord extends ArticleTextAttrRecord {
    private ArticleAbstractRecord(PMID pmid, String abstract_) {
        super(pmid, abstract_);
    }

    /**
     * Creates a new record with a fixed identifier and abstract.
     *
     * @param pmid the unique article identifier.
     *
     * @param abstract_ the article abstract.
     *
     * @return a new record for the specified identifier and abstract.
     */
    public static ArticleAbstractRecord create(PMID pmid, String abstract_) {
        return new ArticleAbstractRecord(pmid, abstract_);
    }

    /**
     * Creates a new record from a parsed XML article element.
     *
     * @param element a parsed XML article element.
     *
     * @return a new record with data extracted from the given article
     * element ({@code null} if the article has no abstract).
     */
    public static ArticleAbstractRecord from(PubmedArticleElement element) {
        String abstract_ = element.getAbstract();

        if (abstract_ != null)
            return create(element.getPMID(), abstract_);
        else
            return null;
    }

    /**
     * Parses a delimited line that encodes a abstract record.
     *
     * @param line the delimited line to parse.
     *
     * @return a new record with the data encoded in the line.
     */
    public static ArticleAbstractRecord parse(String line) {
        String[] fields = FlatRecord.split(line, 2);
        return create(parsePMID(fields[0]), fields[1]);
    }

    /**
     * Returns the abstract of the article.
     *
     * @return the abstract of the article.
     */
    public String getAbstract() {
        return text;
    }
}
