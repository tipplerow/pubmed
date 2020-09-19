
package pubmed.flat;

import java.io.File;

import jam.flat.FlatTable;

import pubmed.article.PMID;

/**
 * Maintains a flat table of unique article identifiers.
 */
public final class PMIDTable extends FlatTable<PMID, PMIDRecord> {
    /**
     * Creates a new table by parsing a flat file.
     *
     * @param file the flat file containing the table data.
     *
     * @return a new table containing the data in the given
     * flat file.
     */
    public static PMIDTable load(File file) {
        PMIDTable table = new PMIDTable();
        table.parse(file);
        return table;
    }

    @Override public PMIDRecord parse(String line) {
        return PMIDRecord.parse(line);
    }
}
