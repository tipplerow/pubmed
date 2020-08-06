
package pubmed.flat;

import java.util.List;

import jam.flat.FlatRecord;

import pubmed.article.PMID;
import pubmed.subject.Subject;

/**
 * Represents a joining record containing the identifier of an
 * article, the key of a subject, and a score that quantifies
 * the relevance of the article to the subject.
 */
public final class RelevanceScoreRecord extends PubmedJoinRecord<String> {
    private final int score;

    private RelevanceScoreRecord(PMID pmid, String subj, int score) {
        super(pmid, subj);
        this.score = score;
    }

    /**
     * Creates a new record with fixed attributes.
     *
     * @param pmid the article identifier.
     *
     * @param subj the key of the subject in question.
     *
     * @param score a score quantifying the relevance of the article
     * to the subject.
     *
     * @return a new record for the specified attributes.
     */
    public static RelevanceScoreRecord create(PMID pmid, String subj, int score) {
        return new RelevanceScoreRecord(pmid, subj, score);
    }

    /**
     * Creates a new record with fixed attributes.
     *
     * @param pmid the article identifier.
     *
     * @param subj the subject in question.
     *
     * @param score a score quantifying the relevance of the article
     * to the subject.
     *
     * @return a new record for the specified attributes.
     */
    public static RelevanceScoreRecord create(PMID pmid, Subject subj, int score) {
        return create(pmid, subj.getKey(), score);
    }

    /**
     * Creates a new record with a fixed article and subject and a
     * zero score.
     *
     * @param pmid the article identifier.
     *
     * @param subj the subject in question.
     *
     * @return a new record with a zero score for the specified
     * article and subject.
     */
    public static RelevanceScoreRecord zero(PMID pmid, Subject subj) {
        return create(pmid, subj, 0);
    }

    /**
     * Parses a delimited line that encodes a relevance score record.
     *
     * @param line the delimited line to parse.
     *
     * @return a new record with the data encoded in the line.
     */
    public static RelevanceScoreRecord parse(String line) {
        String[] fields = FlatRecord.split(line, 3);
        return create(parsePMID(fields[0]), fields[1], Integer.parseInt(fields[2]));
    }

    /**
     * Returns the key of the subject in this record.
     *
     * @return the key of the subject in this record.
     */
    public String getSubjectKey() {
        return fkey;
    }

    /**
     * Returns the relevance score for this record.
     *
     * @return the relevance score for this record.
     */
    public int getScore() {
        return score;
    }      

    @Override public boolean equalsData(Object record) {
        RelevanceScoreRecord that = (RelevanceScoreRecord) record;
        return this.score == that.score;
    }

    @Override public List<String> formatFields() {
        return List.of(format(pmid), format(fkey), format(score));
    }
}
