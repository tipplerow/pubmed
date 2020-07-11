
package pubmed.flat;

import java.io.File;

import jam.flat.FlatTable;

import pubmed.article.PMID;

/**
 * Maintains a flat table of article abstract lemma records.
 */
public final class AbstractLemmaTable extends FlatTable<PMID, AbstractLemmaRecord> {
    /**
     * Creates a new table by parsing a flat file.
     *
     * @param file the flat file containing the table data.
     *
     * @return a new table containing the data in the given
     * flat file.
     */
    public static AbstractLemmaTable load(File file) {
        AbstractLemmaTable table = new AbstractLemmaTable();
        table.parse(file);
        return table;
    }

    @Override public AbstractLemmaRecord parse(String line) {
        return AbstractLemmaRecord.parse(line);
    }
}
