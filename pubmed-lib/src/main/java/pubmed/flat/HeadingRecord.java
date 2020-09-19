
package pubmed.flat;

import java.util.ArrayList;
import java.util.List;

import jam.flat.FlatRecord;
import jam.lang.ObjectUtil;

import pubmed.article.PMID;
import pubmed.mesh.MeshDescriptor;
import pubmed.mesh.MeshDescriptorKey;
import pubmed.mesh.MeshHeading;
import pubmed.mesh.MeshQualifier;
import pubmed.mesh.MeshQualifierKey;
import pubmed.xml.PubmedArticleElement;

/**
 * Represents a joining record containing the identifier for an
 * article and the descriptor key of a {@code MeSH} heading.
 */
public final class HeadingRecord extends PubmedJoinRecord<MeshDescriptorKey> {
    private final MeshQualifierKey qualKey;

    private HeadingRecord(PMID pmid, MeshDescriptorKey descKey, MeshQualifierKey qualKey) {
        super(pmid, descKey);
        this.qualKey = qualKey;
    }

    /**
     * Creates a new record for a fixed article heading.
     *
     * @param pmid the article identifier.
     *
     * @param descKey the key of the {@code MeSH} descriptor in the
     * heading entry.
     *
     * @param qualKey the key of the {@code MeSH} qualifier in the
     * heading entry ({@code null} for headings that do not contain
     * a qualifier).
     *
     * @return a new record for the specified heading.
     */
    public static HeadingRecord create(PMID pmid, MeshDescriptorKey descKey, MeshQualifierKey qualKey) {
        return new HeadingRecord(pmid, descKey, qualKey);
    }

    /**
     * Extracts the heading records from a parsed XML article element.
     *
     * @param element a parsed XML article element.
     *
     * @return the heading records contained in the article element.
     */
    public static List<HeadingRecord> from(PubmedArticleElement element) {
        List<MeshHeading> headingList = element.getMeshHeadingList();

        if (headingList.isEmpty())
            return List.of();

        PMID pmid = element.getPMID();
        List<HeadingRecord> recordList = new ArrayList<HeadingRecord>();

        for (MeshHeading heading : headingList) {
            MeshDescriptorKey descKey = heading.getDescriptorKey();

            if (heading.hasQualifiers()) {
                for (MeshQualifierKey qualKey : heading.viewQualifierKeys())
                    recordList.add(create(pmid, descKey, qualKey));
            }
            else {
                recordList.add(create(pmid, descKey, null));
            }
        }

        return recordList;
    }

    /**
     * Parses a delimited line that encodes a heading record.
     *
     * @param line the delimited line to parse.
     *
     * @return a new record with the data encoded in the line.
     */
    public static HeadingRecord parse(String line) {
        String[] fields = FlatRecord.split(line, 3);
        return create(parsePMID(fields[0]), parseDescriptorKey(fields[1]), parseQualifierKey(fields[2]));
    }

    /**
     * Returns the {@code MeSH} descriptor in the heading.
     *
     * @return the {@code MeSH} descriptor in the heading.
     */
    public MeshDescriptor getDescriptor() {
        return MeshDescriptor.instance(getDescriptorKey());
    }

    /**
     * Returns the {@code MeSH} descriptor key in the heading.
     *
     * @return the {@code MeSH} descriptor key in the heading.
     */
    public MeshDescriptorKey getDescriptorKey() {
        return fkey;
    }

    /**
     * Returns the {@code MeSH} qualifier in the heading.
     *
     * @return the {@code MeSH} qualifier in the heading ({@code null}
     * if the heading does not contain a qualifier).
     */
    public MeshQualifier getQualifier() {
        if (qualKey != null)
            return MeshQualifier.instance(qualKey);
        else
            return null;
    }

    /**
     * Returns the {@code MeSH} qualifier key in the heading.
     *
     * @return the {@code MeSH} qualifier key in the heading (or
     * {@code null} if the heading does not contain a qualifier).
     */
    public MeshQualifierKey getQualifierKey() {
        return qualKey;
    }

    @Override public boolean equalsData(Object record) {
        HeadingRecord that = (HeadingRecord) record;
        return ObjectUtil.equals(this.qualKey, that.qualKey);
    }

    @Override public List<String> formatFields() {
        return List.of(format(pmid), format(getDescriptorKey()), formatQualifierKey(qualKey));
    }
}
