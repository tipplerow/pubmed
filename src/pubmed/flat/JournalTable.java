
package pubmed.flat;

import java.io.File;

import jam.flat.JoinTable;

import pubmed.article.ISSN;
import pubmed.article.PMID;

/**
 * Maintains a flat table of journal records.
 */
public final class JournalTable extends JoinTable<PMID, ISSN, JournalRecord> {
    /**
     * Creates a new table by parsing a flat file.
     *
     * @param file the flat file containing the table data.
     *
     * @return a new table containing the data in the given
     * flat file.
     */
    public static JournalTable load(File file) {
        JournalTable table = new JournalTable();
        table.parse(file);
        return table;
    }

    @Override public JournalRecord parse(String line) {
        return JournalRecord.parse(line);
    }
}
