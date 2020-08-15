
package pubmed.flat;

import java.io.File;
import java.time.LocalDate;
import java.util.List;

import jam.flat.JoinTable;
import jam.lang.JamException;

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

    public LocalDate selectPubDate(PMID pmid) {
        List<PubDateRecord> records = selectPrimary(pmid);

        if (records.size() == 0)
            return null;

        if (records.size() == 1)
            return records.get(0).getDate();

        throw JamException.runtime("Multiple publication dates for article [%s].", pmid);
    }

    @Override public PubDateRecord parse(String line) {
        return PubDateRecord.parse(line);
    }
}
