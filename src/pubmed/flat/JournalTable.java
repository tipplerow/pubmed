
package pubmed.flat;

import java.io.File;
import java.util.List;

import jam.flat.JoinTable;
import jam.lang.JamException;

import pubmed.article.ISSN;
import pubmed.article.PMID;
import pubmed.article.PubmedJournal;

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

    public PubmedJournal selectJournal(PMID pmid) {
        List<JournalRecord> records = selectPrimary(pmid);

        if (records.size() == 0)
            return null;

        if (records.size() == 1)
            return records.get(0).getJournal();

        throw JamException.runtime("Multiple journals for article [%s].", pmid);
    }

    @Override public JournalRecord parse(String line) {
        return JournalRecord.parse(line);
    }
}
