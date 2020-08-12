
package pubmed.flat;

import java.util.List;

import jam.flat.FlatRecord;

import pubmed.article.DOI;
import pubmed.article.PMID;
import pubmed.xml.PubmedArticleElement;

/**
 * Represents a data record containing the {@code PubMed} identifier
 * and Digital Object Identifier (DOI) for an article.
 */
public final class ArticleDOIRecord extends PubmedFlatRecord {
    private final DOI doi;

    private ArticleDOIRecord(PMID pmid, DOI doi) {
        super(pmid);
        this.doi = doi;
    }

    /**
     * Creates a new record with a fixed key and attribute.
     *
     * @param pmid the primary key for the record.
     *
     * @param doi the Digital Object Identifier for the record.
     *
     * @return a new record with the specified attributes.
     */
    public static ArticleDOIRecord create(PMID pmid, DOI doi) {
        return new ArticleDOIRecord(pmid, doi);
    }

    /**
     * Creates a new record from a parsed XML article element.
     *
     * @param element a parsed XML article element.
     *
     * @return a new record with data extracted from the given article
     * element ({@code null} if the article has no DOI field).
     */
    public static ArticleDOIRecord from(PubmedArticleElement element) {
        DOI doi = element.getDOI();

        if (doi != null)
            return create(element.getPMID(), doi);
        else
            return null;
    }

    /**
     * Parses a delimited line that encodes a DOI record.
     *
     * @param line the delimited line to parse.
     *
     * @return a new record with the data encoded in the line.
     */
    public static ArticleDOIRecord parse(String line) {
        String[] fields = FlatRecord.split(line, 2);
        return create(parsePMID(fields[0]), parseDOI(fields[1]));
    }

    /**
     * Returns the Digital Object Identifier for the article.
     *
     * @return the Digital Object Identifier for the article.
     */
    public DOI getDOI() {
        return doi;
    }

    @Override public boolean equalsData(Object record) {
        ArticleDOIRecord that = (ArticleDOIRecord) record;
        return this.doi.equals(that.doi);
    }

    @Override public List<String> formatFields() {
        return List.of(format(pmid), format(doi));
    }
}
