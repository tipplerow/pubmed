
package pubmed.flat;

import java.util.List;

import jam.flat.FlatRecord;
import jam.util.ListUtil;

import pubmed.article.PMID;
import pubmed.article.PublicationType;
import pubmed.xml.PubmedArticleElement;

/**
 * Represents a data record containing the identifier of an article
 * and its publication type.
 */
public final class PubTypeRecord extends PubmedJoinRecord<PublicationType> {
    private PubTypeRecord(PMID pmid, PublicationType type) {
        super(pmid, type);
    }

    /**
     * Creates a new record with a fixed identifier and type.
     *
     * @param pmid the unique article identifier.
     *
     * @param type the publication type of the article.
     *
     * @return a new record with the specified fields.
     */
    public static PubTypeRecord create(PMID pmid, PublicationType type) {
        return new PubTypeRecord(pmid, type);
    }

    /**
     * Creates new records from a parsed XML article element.
     *
     * @param element a parsed XML article element.
     *
     * @return a list of publication type records extracted from the
     * given article element.
     */
    public static List<PubTypeRecord> from(PubmedArticleElement element) {
        return ListUtil.apply(element.getPublicationTypes(), type -> create(element.getPMID(), type));
    }

    /**
     * Parses a delimited line that encodes a publication type record.
     *
     * @param line the delimited line to parse.
     *
     * @return a new record with the data encoded in the line.
     */
    public static PubTypeRecord parse(String line) {
        //
        // The third field is the descriptor name, which is redundant
        // and not needed to recreate the PublicationType instance...
        //
        String[] fields = FlatRecord.split(line, 3);
        return create(parsePMID(fields[0]), PublicationType.create(fields[1]));
    }

    /**
     * Returns the publication type in this record.
     *
     * @return the publication type in this record.
     */
    public PublicationType getType() {
        return fkey;
    }

    @Override public List<String> formatFields() {
        return List.of(format(pmid), format(fkey.getDescriptorKey()), format(fkey.getTypeName()));
    }
}
