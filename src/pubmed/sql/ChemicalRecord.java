
package pubmed.sql;

import java.util.List;

import jam.util.ListUtil;

import pubmed.article.PMID;
import pubmed.article.PubmedArticle;
import pubmed.mesh.MeshRecordKey;

/**
 * Represents a row in the {@code chemicals} table.
 */
public final class ChemicalRecord extends ArticleTextJoinRecord {
    private ChemicalRecord(PMID pmid, String meshKey) {
        super(pmid, meshKey);
    }

    /**
     * Creates a new chemical record from its fields.
     *
     * @param pmid the unique identifier of the indexed article.
     *
     * @param meshKey the record key of a {@code MeSH} chemical
     * substance contained in the article.
     *
     * @return the {@code chemicals} record with the specified fields.
     */
    public static ChemicalRecord create(PMID pmid, String meshKey) {
        return new ChemicalRecord(pmid, meshKey);
    }

    /**
     * Creates a new chemical record from its fields.
     *
     * @param pmid the unique identifier of the indexed article.
     *
     * @param meshKey the record key of a {@code MeSH} chemical
     * substsance contained in the article.
     *
     * @return the {@code chemicals} record with the specified fields.
     */
    public static ChemicalRecord create(PMID pmid, MeshRecordKey meshKey) {
        return new ChemicalRecord(pmid, meshKey.getKey());
    }

    /**
     * Creates {@code chemicals} records for all chemicals contained
     * in an article.
     *
     * @param article the article to index.
     *
     * @return a list of {@code chemicals} records for the specified
     * article.
     */
    public static List<ChemicalRecord> list(PubmedArticle article) {
        return ListUtil.apply(article.viewChemicalList(), key -> create(article.getPMID(), key));
    }

    /**
     * Returns the {@code MeSH} record key indexed by this record.
     *
     * @return the {@code MeSH} record key indexed by this record.
     */
    public MeshRecordKey getRecordKey() {
        return MeshRecordKey.instance(key2);
    }
}
