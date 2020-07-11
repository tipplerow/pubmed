
package pubmed.flat;

import java.io.File;

import jam.flat.FlatTable;

import pubmed.article.PMID;

/**
 * Maintains a flat table of article title lemma records.
 */
public final class TitleLemmaTable extends FlatTable<PMID, TitleLemmaRecord> {
    /**
     * Creates a new table by parsing a flat file.
     *
     * @param file the flat file containing the table data.
     *
     * @return a new table containing the data in the given
     * flat file.
     */
    public static TitleLemmaTable load(File file) {
        TitleLemmaTable table = new TitleLemmaTable();
        table.parse(file);
        return table;
    }

    @Override public TitleLemmaRecord parse(String line) {
        return TitleLemmaRecord.parse(line);
    }
}
