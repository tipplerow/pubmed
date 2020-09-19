
package pubmed.flat;

import java.time.LocalDate;

import jam.flat.FlatRecord;

import pubmed.article.PMID;
import pubmed.xml.PubmedArticleElement;

/**
 * Represents a data record containing the identifier of an article
 * and its publication date.
 */
public final class PubDateRecord extends ArticleDateRecord {
    private PubDateRecord(PMID pmid, LocalDate date) {
        super(pmid, date);
    }

    /**
     * Creates a new record with a fixed identifier and date.
     *
     * @param pmid the unique article identifier.
     *
     * @param date the publication date of the article.
     *
     * @return a new record with the specified fields.
     */
    public static PubDateRecord create(PMID pmid, LocalDate date) {
        return new PubDateRecord(pmid, date);
    }

    /**
     * Creates a new record from a parsed XML article element.
     *
     * @param element a parsed XML article element.
     *
     * @return a new record with data extracted from the given article
     * element ({@code null} if the article does not have a discernible
     * publication date).
     */
    public static PubDateRecord from(PubmedArticleElement element) {
        LocalDate pubDate = element.resolvePublicationDate();

        if (pubDate != null)
            return create(element.getPMID(), pubDate);
        else
            return null;
    }

    /**
     * Parses a delimited line that encodes a publication date record.
     *
     * @param line the delimited line to parse.
     *
     * @return a new record with the data encoded in the line.
     */
    public static PubDateRecord parse(String line) {
        String[] fields = FlatRecord.split(line, 2);
        return create(parsePMID(fields[0]), FlatRecord.parseDate(fields[1]));
    }
}
