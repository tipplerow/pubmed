
package pubmed.flat;

import java.util.List;

import jam.flat.FlatRecord;
import jam.util.ListUtil;

import pubmed.article.PMID;
import pubmed.mesh.MeshDescriptorKey;
import pubmed.mesh.MeshHeading;
import pubmed.mesh.MeshQualifierKey;
import pubmed.xml.PubmedArticleElement;

/**
 * Represents a joining record containing the identifier for an
 * article and the descriptor key of a {@code MeSH} heading.
 */
public final class HeadingDescRecord extends PubmedJoinRecord<MeshDescriptorKey> {
    private HeadingDescRecord(PMID pmid, MeshDescriptorKey descKey) {
        super(pmid, descKey);
    }

    /**
     * Creates a new record for a fixed article heading.
     *
     * @param pmid the article identifier.
     *
     * @param descKey the key of the {@code MeSH} descriptor
     * in the heading entry.
     *
     * @return a new record for the specified identifier and
     * descriptor.
     */
    public static HeadingDescRecord create(PMID pmid, MeshDescriptorKey descKey) {
        return new HeadingDescRecord(pmid, descKey);
    }

    /**
     * Extracts the heading records from a parsed XML article element.
     *
     * @param element a parsed XML article element.
     *
     * @return the heading records contained in the article element.
     */
    public static List<HeadingDescRecord> from(PubmedArticleElement element) {
        List<MeshHeading> headingList = element.getMeshHeadingList();

        if (headingList.isEmpty())
            return List.of();
        else
            return ListUtil.apply(headingList, heading -> create(element.getPMID(), heading.getDescriptorKey()));
    }

    /**
     * Parses a delimited line that encodes a heading record.
     *
     * @param line the delimited line to parse.
     *
     * @return a new record with the data encoded in the line.
     */
    public static HeadingDescRecord parse(String line) {
        String[] fields = FlatRecord.split(line, 2);
        return create(parsePMID(fields[0]), parseDescriptorKey(fields[1]));
    }

    /**
     * Returns the {@code MeSH} descriptor key in the heading.
     *
     * @return the {@code MeSH} descriptor key in the heading.
     */
    public MeshDescriptorKey getDescriptorKey() {
        return fkey;
    }

    @Override public List<String> formatFields() {
        return List.of(format(pmid), format(getDescriptorKey()));
    }
}
