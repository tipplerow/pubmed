
package pubmed.flat;

import java.io.File;

import jam.flat.JoinTable;

import pubmed.article.PMID;
import pubmed.mesh.MeshRecordKey;

/**
 * Maintains a flat table of article title records.
 */
public final class ChemicalTable extends JoinTable<PMID, MeshRecordKey, ChemicalRecord> {
    /**
     * Creates a new table by parsing a flat file.
     *
     * @param file the flat file containing the table data.
     *
     * @return a new table containing the data in the given
     * flat file.
     */
    public static ChemicalTable load(File file) {
        ChemicalTable table = new ChemicalTable();
        table.parse(file);
        return table;
    }

    @Override public ChemicalRecord parse(String line) {
        return ChemicalRecord.parse(line);
    }
}
