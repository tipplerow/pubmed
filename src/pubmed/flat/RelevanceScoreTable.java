
package pubmed.flat;

import java.io.File;

import jam.flat.JoinTable;

import pubmed.article.PMID;
import pubmed.subject.Subject;

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

    /**
     * Retrieves the relevance score for an article and subject.
     *
     * @param pmid the identifier of the target article.
     *
     * @param subj the key of the target subject.
     *
     * @return the relevance score for the specified article and
     * subject (zero if there is no matching relevance record).
     */
    public int getScore(PMID pmid, String subj) {
        RelevanceScoreRecord record = select(pmid, subj);

        if (record != null)
            return record.getScore();
        else
            return 0;
    }

    /**
     * Retrieves the relevance score for an article and subject.
     *
     * @param pmid the identifier of the target article.
     *
     * @param subj the target subject.
     *
     * @return the relevance score for the specified article and
     * subject (zero if there is no matching relevance record).
     */
    public int getScore(PMID pmid, Subject subj) {
        return getScore(pmid, subj.getKey());
    }

    @Override public RelevanceScoreRecord parse(String line) {
        return RelevanceScoreRecord.parse(line);
    }
}
