
package pubmed.flat;

import java.io.File;

import jam.flat.JoinTable;

import pubmed.article.PMID;
import pubmed.article.PublicationType;

/**
 * Maintains a flat table of publication type records.
 */
public final class PubTypeTable extends JoinTable<PMID, PublicationType, PubTypeRecord> {
    /**
     * Creates a new table by parsing a flat file.
     *
     * @param file the flat file containing the table data.
     *
     * @return a new table containing the data in the given
     * flat file.
     */
    public static PubTypeTable load(File file) {
        PubTypeTable table = new PubTypeTable();
        table.parse(file);
        return table;
    }

    @Override public PubTypeRecord parse(String line) {
        return PubTypeRecord.parse(line);
    }
}
