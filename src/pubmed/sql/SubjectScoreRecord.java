
package pubmed.sql;

import pubmed.article.ArticleType;
import pubmed.article.PMID;
import pubmed.article.PubmedArticle;

/**
 * Represents a row in a subject score table.
 */
public final class SubjectScoreRecord extends ArticleAttrRecord {
    private final int titleScore;
    private final int abstractScore;
    private final int meshTreeScore;
    private final int headingListScore;
    private final int keywordListScore;
    private final int chemicalListScore;

    private SubjectScoreRecord(PMID pmid,
                               int  titleScore,
                               int  abstractScore,
                               int  meshTreeScore,
                               int  headingListScore,
                               int  keywordListScore,
                               int  chemicalListScore) {
        super(pmid);
        
	this.titleScore = titleScore;
	this.abstractScore = abstractScore;
        this.meshTreeScore = meshTreeScore;
	this.headingListScore = headingListScore;
	this.keywordListScore = keywordListScore;
	this.chemicalListScore = chemicalListScore;
    }

    /**
     * Creates a new article record from its fields.
     *
     * @param pmid the unique {@code PMID} identifier for the article.
     *
     * @param titleScore the subject match score for the article title.
     *
     * @param abstractScore the subject match score for the article
     * abstract.
     *
     * @param meshTreeScore the subject match score for {@code MeSH}
     * tree numbers in the subject descriptor.
     *
     * @param headingListScore the subject match score for the heading
     * list.
     *
     * @param keywordListScore the subject match score for the keyword
     * list.
     *
     * @param chemicalListScore the subject match score for the
     * chemical list.
     *
     * @return the {@code articles} record with the specified fields.
     */
    public static SubjectScoreRecord create(PMID pmid,
                                            int  titleScore,
                                            int  abstractScore,
                                            int  meshTreeScore,
                                            int  headingListScore,
                                            int  keywordListScore,
                                            int  chemicalListScore) {
        return new SubjectScoreRecord(pmid,
                                      titleScore,
                                      abstractScore,
                                      meshTreeScore,
                                      headingListScore,
                                      keywordListScore,
                                      chemicalListScore);
    }

    /**
     * Determines whether to include this score record in a database
     * table.
     *
     * @return {@code true} iff one or more subject scores are positive.
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
     * Returns the subject match score for the article abstract.
     *
     * @return the subject match score for the article abstract.
     */
    public int getAbstractScore() {
	return abstractScore;
    }

    /**
     * Returns the subject match score for the chemical list.
     *
     * @return the subject match score for the chemical list.
     */
    public int getChemicalListScore() {
	return chemicalListScore;
    }

    /**
     * Returns the subject match score for the heading list.
     *
     * @return the subject match score for the heading list.
     */
    public int getHeadingListScore() {
	return headingListScore;
    }

    /**
     * Returns the subject match score for the keyword list.
     *
     * @return the subject match score for the keyword list.
     */
    public int getKeywordListScore() {
	return keywordListScore;
    }

    /**
     * Returns the subject match score for {@code MeSH} tree numbers
     * in the subject descriptor.
     *
     * @return the subject match score for {@code MeSH} tree numbers
     * in the subject descriptor.
     */
    public int getMeshTreeScore() {
        return meshTreeScore;
    }

    /**
     * Returns the subject match score for the article title.
     *
     * @return the subject match score for the article title.
     */
    public int getTitleScore() {
	return titleScore;
    }

    @Override public String formatBulk() {
        return joinBulk(formatBulk(pmid),
			formatBulk(titleScore),
			formatBulk(abstractScore),
                        formatBulk(meshTreeScore),
			formatBulk(headingListScore),
			formatBulk(keywordListScore),
			formatBulk(chemicalListScore));
    }
}
