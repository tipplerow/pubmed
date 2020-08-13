
package pubmed.flat;

import java.util.List;

import jam.flat.FlatRecord;

import pubmed.article.PMID;
import pubmed.flat.PubmedJoinRecord;
import pubmed.relev.AbstractRelevanceScorer;
import pubmed.relev.ChemicalRelevanceScorer;
import pubmed.relev.HeadingRelevanceScorer;
import pubmed.relev.KeywordRelevanceScorer;
import pubmed.relev.MeshTreeRelevanceScorer;
import pubmed.relev.TitleRelevanceScorer;
import pubmed.subject.Subject;

/**
 * Represents a joining record containing the identifier for an
 * article, the key of a subject covered in that article, and
 * scoring metrics that quantify the degree of relevance of the
 * article to the subject.
 *
 * <p><b>Title score:</b> The number of times a subject keyword or
 * phrase occurs in the article title.
 *
 * <p><b>Abstract score:</b> The number of times a subject keyword
 * or phrase occurs in the article abstract.
 *
 * <p><b>MeSH tree score:</b> If the subject maps to a {@code MeSH}
 * descriptor and the article has a {@code MeSH} heading list, the
 * tree score is {@code +1} if one or more of the heading descriptors
 * has a MeSH tree number equal to or more specific than a tree number
 * assigned to the subject descriptor and the tree score is {@code -1}
 * if there are no matching heading descriptors.  The tree score is
 * {@code 0} if the subject does not map to a {@code MeSH} descriptor
 * or if the article does not have a heading list.
 *
 * <p><b>Heading list score:</b> If the subject maps to a {@code MeSH}
 * descriptor and the article has a {@code MeSH} heading list, the
 * score is {@code +1} if the list contains the subject descriptor,
 * {@code -1} if it does not.  The score is {@code 0} if the subject
 * does not map to a {@code MeSH} descriptor or if the article does
 * not have a heading list.
 *
 * <p><b>Keyword list score:</b> If the article has a keyword list, the
 * score is {@code +1} if the keyword list contains a subject keyword
 * or phrase, {@code -1} if it does not.  The score is {@code 0} if
 * article does not have a keyword list.
 *
 * <p><b>Chemical list score:</b> If the subject maps to a {@code MeSH}
 * chemical record and the article has a chemical list, the score is
 * {@code +1} if the list contains the subject record, {@code -1} if
 * it does not. The score is {@code 0} if the subject does not map to
 * a {@code MeSH} chemical record or if the article does not have a
 * chemical list.
 */
public final class RelevanceScoreRecord extends PubmedJoinRecord<String> {
    private final int titleScore;
    private final int abstractScore;
    private final int meshTreeScore;
    private final int headingListScore;
    private final int keywordListScore;
    private final int chemicalListScore;

    private RelevanceScoreRecord(PMID pmid,
                                 String subjectKey,
                                 int titleScore,
                                 int abstractScore,
                                 int meshTreeScore,
                                 int headingListScore,
                                 int keywordListScore,
                                 int chemicalListScore) {
        super(pmid, subjectKey);

        this.titleScore = titleScore;
        this.abstractScore = abstractScore;
        this.meshTreeScore = meshTreeScore;
        this.headingListScore = headingListScore;
        this.keywordListScore = keywordListScore;
        this.chemicalListScore = chemicalListScore;
    }

    /**
     * Computes relevance scores for an article and subject and
     * creates a new record with the individual scores.
     *
     * @param pmid the article identifier.
     *
     * @param subject the subject to be scored.
     *
     * @param titleScorer the title scoring engine.
     *
     * @param abstractScorer the abstract scoring engine.
     *
     * @param meshTreeScorer the MeSH tree scoring engine.
     *
     * @param headingListScorer the heading list scoring engine.
     *
     * @param keywordListScorer the keyword list scoring engine.
     *
     * @param chemicalListScorer the chemical list scoring engine.
     *
     * @return a new record with the computed scores for the article
     * and subject.
     */
    public static RelevanceScoreRecord compute(PMID pmid,
                                               Subject subject,
                                               TitleRelevanceScorer titleScorer,
                                               AbstractRelevanceScorer abstractScorer,
                                               MeshTreeRelevanceScorer meshTreeScorer,
                                               HeadingRelevanceScorer headingListScorer,
                                               KeywordRelevanceScorer keywordListScorer,
                                               ChemicalRelevanceScorer chemicalListScorer) {

        int titleScore        = titleScorer.computeScore(pmid, subject);
        int abstractScore     = abstractScorer.computeScore(pmid, subject);
        int meshTreeScore     = meshTreeScorer.computeScore(pmid, subject);
        int headingListScore  = headingListScorer.computeScore(pmid, subject);
        int keywordListScore  = keywordListScorer.computeScore(pmid, subject);
        int chemicalListScore = chemicalListScorer.computeScore(pmid, subject);
    
        return create(pmid,
                      subject.getKey(),
                      titleScore,
                      abstractScore,
                      meshTreeScore,
                      headingListScore,
                      keywordListScore,
                      chemicalListScore);
    }

    /**
     * Creates a new record with fixed attributes.
     *
     * @param pmid the article identifier.
     *
     * @param subjectKey a key of the subject covered in the article.
     *
     * @param titleScore the title score described in the class
     * header.
     *
     * @param abstractScore the abstract score described in the class
     * header.
     *
     * @param meshTreeScore the MeSH tree score described in the class
     * header.
     *
     * @param headingListScore the heading list score described in the
     * class header.
     *
     * @param keywordListScore the keyword list score described in the
     * class header.
     *
     * @param chemicalListScore the chemical list score described in
     * the class header.
     *
     * @return a new record with the specified attributes.
     */
    public static RelevanceScoreRecord create(PMID pmid,
                                              String subjectKey,
                                              int titleScore,
                                              int abstractScore,
                                              int meshTreeScore,
                                              int headingListScore,
                                              int keywordListScore,
                                              int chemicalListScore) {
        return new RelevanceScoreRecord(pmid,
                                        subjectKey,
                                        titleScore,
                                        abstractScore,
                                        meshTreeScore,
                                        headingListScore,
                                        keywordListScore,
                                        chemicalListScore);
    }

    /**
     * Parses a delimited line that encodes a relevance record.
     *
     * @param line the delimited line to parse.
     *
     * @return a new record with the data encoded in the line.
     */
    public static RelevanceScoreRecord parse(String line) {
        String[] fields = FlatRecord.split(line, 8);

        return create(parsePMID(fields[0]),
                      fields[1],
                      FlatRecord.parseInt(fields[2]),
                      FlatRecord.parseInt(fields[3]),
                      FlatRecord.parseInt(fields[4]),
                      FlatRecord.parseInt(fields[5]),
                      FlatRecord.parseInt(fields[6]),
                      FlatRecord.parseInt(fields[7]));
    }

    /**
     * Creates a new record with all zero scores.
     *
     * @param pmid the article identifier.
     *
     * @param subj a key of the subject covered in the article.
     *
     * @return a new record with all zero scores for the specified article and subject.
     */
    public static RelevanceScoreRecord zero(PMID pmid, String subj) {
        return create(pmid, subj, 0, 0, 0, 0, 0, 0);
    }

    /**
     * Defines a filter that selects relevance records with one or
     * more positive scores.
     *
     * @return {@code true} iff this record contains one or more
     * positive relevance scores
     */
    public boolean filter() {
        return titleScore > 0
            || abstractScore > 0
            || meshTreeScore > 0
            || headingListScore > 0
            || keywordListScore > 0
            || chemicalListScore > 0;
    }

    /**
     * Returns the key of the subject covered in the article.
     *
     * @return the key of the subject covered in the article.
     */
    public String getSubjectKey() {
        return fkey;
    }

    /**
     * Returns the title score described in the class header.
     *
     * @return the title score described in the class header.
     */
    public int getTitleScore() {
        return titleScore;
    }

    /**
     * Returns the abstract score described in the class header.
     *
     * @return the abstract score described in the class header.
     */
    public int getAbstractScore() {
        return abstractScore;
    }

    /**
     * Returns the MeSH tree score described in the class header.
     *
     * @return the MeSH tree score described in the class header.
     */
    public int getMeshTreeScore() {
        return meshTreeScore;
    }

    /**
     * Returns the heading list score described in the class header.
     *
     * @return the heading list score described in the class header.
     */
    public int getHeadingListScore() {
        return headingListScore;
    }

    /**
     * Returns the keyword list score described in the class header.
     *
     * @return the keyword list score described in the class header.
     */
    public int getKeywordListScore() {
        return keywordListScore;
    }

    /**
     * Returns the chemical list score described in the class header.
     *
     * @return the chemical list score described in the class header.
     */
    public int getChemicalListScore() {
        return chemicalListScore;
    }

    @Override public boolean equalsData(Object record) {
        RelevanceScoreRecord that = (RelevanceScoreRecord) record;

        return this.titleScore == that.titleScore
            && this.abstractScore == that.abstractScore
            && this.meshTreeScore == that.meshTreeScore
            && this.headingListScore == that.headingListScore
            && this.keywordListScore == that.keywordListScore
            && this.chemicalListScore == that.chemicalListScore;
    }

    @Override public List<String> formatFields() {
        return List.of(format(pmid),
                       format(getSubjectKey()),
                       format(titleScore),
                       format(abstractScore),
                       format(meshTreeScore),
                       format(headingListScore),
                       format(keywordListScore),
                       format(chemicalListScore));
    }
}
