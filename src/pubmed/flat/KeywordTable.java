
package pubmed.flat;

import java.io.File;

import jam.flat.JoinTable;

import pubmed.article.PMID;

/**
 * Maintains a flat table of article title records.
 */
public final class KeywordTable extends JoinTable<PMID, String, KeywordRecord> {
    /**
     * Creates a new table by parsing a flat file.
     *
     * @param file the flat file containing the table data.
     *
     * @return a new table containing the data in the given
     * flat file.
     */
    public static KeywordTable load(File file) {
        KeywordTable table = new KeywordTable();
        table.parse(file);
        return table;
    }

    @Override public KeywordRecord parse(String line) {
        return KeywordRecord.parse(line);
    }
}
