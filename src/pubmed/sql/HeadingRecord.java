
package pubmed.sql;

import java.util.ArrayList;
import java.util.List;

import pubmed.article.PMID;
import pubmed.article.PubmedArticle;
import pubmed.mesh.MeshDescriptorKey;
import pubmed.mesh.MeshHeading;
import pubmed.mesh.MeshQualifierKey;

/**
 * Represents a row in the {@code headings} table.
 */
public final class HeadingRecord implements PubmedBulkRecord {
    private final PMID pmid;
    private final MeshDescriptorKey descKey;
    private final MeshQualifierKey  qualKey;

    private HeadingRecord(PMID pmid, MeshDescriptorKey descKey, MeshQualifierKey qualKey) {
        this.pmid = pmid;
        this.descKey = descKey;
        this.qualKey = qualKey;
    }

    private static MeshQualifierKey qualifierKey(String qualKey) {
        return qualKey.equals(NO_QUALIFIER) ? null : MeshQualifierKey.instance(qualKey);
    }

    private static String qualifierKeyString(MeshQualifierKey qualKey) {
        return qualKey != null ? qualKey.getKey() : NO_QUALIFIER;
    }
           
    /**
     * The special string used to indicate that a heading record has
     * no qualifier (to avoid {@code NULL} values in the database).
     */
    public static final String NO_QUALIFIER = "-";

    /**
     * Creates a new heading record from its fields.
     *
     * @param pmid the unique identifier of the indexed article.
     *
     * @param descKey the descriptor key in the {@code MeSH} heading.
     *
     * @param qualKey the qualifier key in the {@code MeSH} heading
     * (may be {@code null} for headings that lack qualifiers).
     *
     * @return the {@code headings} record with the specified fields.
     */
    public static HeadingRecord create(PMID pmid, String descKey, String qualKey) {
        return create(pmid, MeshDescriptorKey.instance(descKey), qualifierKey(qualKey));
    }


    /**
     * Creates a new heading record from its fields.
     *
     * @param pmid the unique identifier of the indexed article.
     *
     * @param descKey the descriptor key in the {@code MeSH} heading.
     *
     * @param qualKey the qualifier key in the {@code MeSH} heading
     * (may be {@code null} for headings that lack qualifiers).
     *
     * @return the {@code headings} record with the specified fields.
     */
    public static HeadingRecord create(PMID pmid, MeshDescriptorKey descKey, MeshQualifierKey qualKey) {
        return new HeadingRecord(pmid, descKey, qualKey);
    }

    /**
     * Creates {@code headings} records for all headings contained in
     * an article.
     *
     * @param article the article to index.
     *
     * @return a list of {@code headings} records for the specified
     * article.
     */
    public static List<HeadingRecord> list(PubmedArticle article) {
        if (!article.hasHeadingList())
            return List.of();

        List<HeadingRecord> records =
            new ArrayList<HeadingRecord>();

        for (MeshHeading heading : article.viewHeadingList()) {
            MeshDescriptorKey descKey = heading.getDescriptorKey();
            List<MeshQualifierKey> qualKeys = heading.viewQualifierKeys();

            if (qualKeys.isEmpty()) {
                records.add(create(article.getPMID(), descKey, null));
            }
            else {
                for (MeshQualifierKey qualKey : qualKeys)
                    records.add(create(article.getPMID(), descKey, qualKey));
            }
        }

        return records;
    }

    /**
     * Returns the {@code MeSH} descriptor key indexed by this record.
     *
     * @return the {@code MeSH} descriptor key indexed by this record.
     */
    public PMID getPMID() {
        return pmid;
    }

    /**
     * Returns the {@code MeSH} descriptor key indexed by this record.
     *
     * @return the {@code MeSH} descriptor key indexed by this record.
     */
    public MeshDescriptorKey getDescriptorKey() {
        return descKey;
    }

    /**
     * Returns the {@code MeSH} qualifier key indexed by this record.
     *
     * @return the {@code MeSH} qualifier key indexed by this record.
     */
    public MeshQualifierKey getQualifierKey() {
        return qualKey;
    }

    @Override public String formatBulk() {
        return joinBulk(formatBulk(pmid), formatBulk(descKey), qualifierKeyString(qualKey));
    }
}
