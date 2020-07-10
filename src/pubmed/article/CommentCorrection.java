
package pubmed.article;

/**
 * Represents a comment on or correction to a previously published
 * article.
 */
public final class CommentCorrection {
    private final PMID commCorrPMID;
    private final PMID originalPMID;
    private final CommentCorrectionType commCorrType;

    private CommentCorrection(CommentCorrectionType commCorrType, PMID commCorrPMID, PMID originalPMID) {
        this.commCorrType = commCorrType;
        this.commCorrPMID = commCorrPMID;
        this.originalPMID = originalPMID;
    }

    /**
     * Creates a new comment-correction record.
     *
     * @param commCorrType the enumerated type for the comment or
     * correction.
     *
     * @param commCorrPMID the {@code PMID} for the comment or
     * correction entry.
     *
     * @param originalPMID the {@code PMID} for the original article
     * (to which the comment or correction refers).
     *
     * @return the new comment-correction record.
     */
    public static CommentCorrection create(CommentCorrectionType commCorrType,
                                           PMID commCorrPMID,
                                           PMID originalPMID) {
        return new CommentCorrection(commCorrType, commCorrPMID, originalPMID);
    }

    /**
     * Creates a new comment-correction record.
     *
     * @param originalPMID the {@code PMID} for the original article
     * (to which the comment or correction refers).
     *
     * @param commCorrType the enumerated type for the comment or
     * correction.
     *
     * @return the new comment-correction record.
     */
    public static CommentCorrection create(PMID originalPMID, CommentCorrectionType commCorrType) {
        return new CommentCorrection(commCorrType, null, originalPMID);
    }

    /**
     * Returns the enumerated type for this comment or correction.
     *
     * @return the enumerated type for this comment or correction.
     */
    public CommentCorrectionType getCommCorrType() {
        return commCorrType;
    }

    /**
     * Returns the {@code PMID} for this comment or correction.
     *
     * @return the {@code PMID} for this comment or correction.
     */
    public PMID getCommCorrPMID() {
        return commCorrPMID;
    }

    /**
     * Returns the {@code PMID} for the original article (to which
     * this comment or correction refers).
     *
     * @return the {@code PMID} for the original article (to which
     * this comment or correction refers).
     */
    public PMID getOriginalPMID() {
        return originalPMID;
    }
}
