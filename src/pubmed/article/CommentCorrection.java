
package pubmed.article;

/**
 * Represents a comment on or correction to a previously published
 * article.
 */
public final class CommentCorrection {
    private final PMID originalPMID;
    private final CommentCorrectionType commCorrType;

    private CommentCorrection(PMID originalPMID, CommentCorrectionType commCorrType) {
        this.originalPMID = originalPMID;
        this.commCorrType = commCorrType;
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
        return new CommentCorrection(originalPMID, commCorrType);
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

    /**
     * Returns the enumerated type for this comment or correction.
     *
     * @return the enumerated type for this comment or correction.
     */
    public CommentCorrectionType getCommCorrType() {
        return commCorrType;
    }
}
