
package pubmed.flat;

import java.io.File;

import jam.flat.JoinTable;

import pubmed.article.PMID;

/**
 * Maintains a flat table of relevance score records.
 */
public final class RelevanceScoreTable extends JoinTable<PMID, String, RelevanceScoreRecord> {
    /**
     * Creates a new table by parsing a flat file.
     *
     * @param file the flat file containing the table data.
     *
     * @return a new table containing the data in the given
     * flat file.
     */
    public static RelevanceScoreTable load(File file) {
        RelevanceScoreTable table = new RelevanceScoreTable();
        table.parse(file);
        return table;
    }

    @Override public RelevanceScoreRecord parse(String line) {
        return RelevanceScoreRecord.parse(line);
    }
}
