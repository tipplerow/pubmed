
package pubmed.relev;

import java.io.File;

import jam.flat.JoinTable;

import pubmed.article.PMID;

/**
 * Maintains a flat table of relevance summary records.
 */
public final class RelevanceSummaryTable extends JoinTable<PMID, String, RelevanceSummaryRecord> {
    /**
     * Creates a new table by parsing a flat file.
     *
     * @param file the flat file containing the table data.
     *
     * @return a new table containing the data in the given
     * flat file.
     */
    public static RelevanceSummaryTable load(File file) {
        RelevanceSummaryTable table = new RelevanceSummaryTable();
        table.parse(file);
        return table;
    }

    @Override public RelevanceSummaryRecord parse(String line) {
        return RelevanceSummaryRecord.parse(line);
    }
}
