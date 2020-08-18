
package pubmed.relev;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import jam.io.LineReader;
import jam.util.PairKeyTreeTable;

import pubmed.article.PMID;
import pubmed.subject.Subject;

public final class RelevanceSummaryTable extends PairKeyTreeTable<PMID, String, RelevanceSummaryRecord> {
    private RelevanceSummaryTable() {
        super();
    }

    /**
     * Creates an empty relevance summary table.
     *
     * @return an empty relevance summary table.
     */
    @SuppressWarnings("unchecked")
    public static RelevanceSummaryTable create() {
        return new RelevanceSummaryTable();
    }

    /**
     * Loads the relevance summary table from a file.
     *
     * @param file the file to load.
     *
     * @return the relevance summary table stored in the given file.
     *
     * @throws RuntimeException if any I/O errors occur.
     */
    public static RelevanceSummaryTable load(File file) {
        RelevanceSummaryTable table = new RelevanceSummaryTable();

        try (LineReader reader = LineReader.open(file)) {
            // Skip the header line...
            reader.next();

            for (String line : reader)
                table.add(RelevanceSummaryRecord.parse(line));
        }

        return table;
    }

    /**
     * Adds a relevance summary record to this table.
     *
     * @param record the record to add.
     */
    public void add(RelevanceSummaryRecord record) {
        put(record.getPMID(), record.getSubjectKey(), record);
    }

    /**
     * Retrieves a relevance summary record by its article identifier
     * and subject.
     *
     * @param pmid the article identifier for the record.
     *
     * @param subject the subject for the record.
     *
     * @return the relevance summary record for the specified article
     * identifier and subject (or {@code null} if there is no matching
     * record).
     */
    public RelevanceSummaryRecord get(PMID pmid, Subject subject) {
        return get(pmid, subject.getKey());
    }

    /**
     * Extracts the identifiers for the relevant articles in this
     * table.
     *
     * @return a new set containing the identifiers for all records
     * marked as <em>likely relevant</em>.
     */
    public Set<PMID> getRelevantPMID() {
        Set<PMID> pmids = new HashSet<PMID>();

        for (RelevanceSummaryRecord record : values())
            if (record.isLikelyMatch())
                pmids.add(record.getPMID());

        return pmids;
    }
}
