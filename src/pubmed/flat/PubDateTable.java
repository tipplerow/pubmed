
package pubmed.flat;

import java.io.File;
import java.time.LocalDate;

import jam.flat.JoinTable;

import pubmed.article.PMID;

/**
 * Maintains a flat table of article date records.
 */
public final class PubDateTable extends JoinTable<PMID, LocalDate, PubDateRecord> {
    /**
     * Creates a new table by parsing a flat file.
     *
     * @param file the flat file containing the table data.
     *
     * @return a new table containing the data in the given
     * flat file.
     */
    public static PubDateTable load(File file) {
        PubDateTable table = new PubDateTable();
        table.parse(file);
        return table;
    }

    @Override public PubDateRecord parse(String line) {
        return PubDateRecord.parse(line);
    }
}
