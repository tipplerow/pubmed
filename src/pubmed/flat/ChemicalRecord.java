
package pubmed.flat;

import java.util.List;

import jam.flat.FlatRecord;
import jam.util.ListUtil;

import pubmed.article.PMID;
import pubmed.mesh.MeshRecordKey;
import pubmed.xml.PubmedArticleElement;

/**
 * Represents a joining record containing the identifier for an
 * article and a chemical substance contained in that article.
 */
public final class ChemicalRecord extends PubmedJoinRecord<MeshRecordKey> {
    private ChemicalRecord(PMID pmid, MeshRecordKey mesh) {
        super(pmid, mesh);
    }

    /**
     * Creates a new record with a fixed identifier and substance.
     *
     * @param pmid the article identifier.
     *
     * @param mesh the key of the substance contained in the article.
     *
     * @return a new record for the specified identifier and substance.
     */
    public static ChemicalRecord create(PMID pmid, MeshRecordKey mesh) {
        return new ChemicalRecord(pmid, mesh);
    }

    /**
     * Extracts the chemical records from a parsed XML article element.
     *
     * @param element a parsed XML article element.
     *
     * @return the chemical records contained in the article element.
     */
    public static List<ChemicalRecord> from(PubmedArticleElement element) {
        List<MeshRecordKey> keyList = element.getChemicalList();

        if (keyList.isEmpty())
            return List.of();
        else
            return ListUtil.apply(keyList, key -> create(element.getPMID(), key));
    }

    /**
     * Parses a delimited line that encodes a chemical record.
     *
     * @param line the delimited line to parse.
     *
     * @return a new record with the data encoded in the line.
     */
    public static ChemicalRecord parse(String line) {
        String[] fields = FlatRecord.split(line, 2);
        return create(PMID.instance(fields[0]), MeshRecordKey.instance(fields[1]));
    }

    /**
     * Returns the {@code MeSH} record key of the substance.
     *
     * @return the {@code MeSH} record key of the substance.
     */
    public MeshRecordKey getMeshKey() {
        return fkey;
    }

    @Override public List<String> formatFields() {
        return List.of(format(pmid), format(getMeshKey()));
    }
}
