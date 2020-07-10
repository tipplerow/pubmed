
package pubmed.article;

/**
 * Enumerates categories of comments and corrections to previously
 * published articles.
 */
public enum CommentCorrectionType {
    AssociatedDataset(),
    AssociatedPublication(),
    Cites(),
    CommentIn(),
    CommentOn(ArticleType.COMMENT),
    CorrectedandRepublishedIn(),
    CorrectedandRepublishedFrom(),
    ErratumIn(),
    ErratumFor(ArticleType.ERRATUM),
    ExpressionOfConcernIn(),
    ExpressionOfConcernFor(ArticleType.EXPRESSION_OF_CONCERN),
    OriginalReportIn(),
    ReprintIn(),
    ReprintOf(),
    RepublishedIn(),
    RepublishedFrom(),
    RetractedandRepublishedIn(ArticleType.RETRACTED_PUBLICATION),
    RetractedandRepublishedFrom(ArticleType.RETRACTION_NOTICE),
    RetractionIn(ArticleType.RETRACTED_PUBLICATION),
    RetractionOf(ArticleType.RETRACTION_NOTICE),
    SummaryForPatientsIn(),
    UpdateIn(),
    UpdateOf();

    private final ArticleType articleType;

    private CommentCorrectionType() {
        this(null);
    }

    private CommentCorrectionType(ArticleType articleType) {
        this.articleType = articleType;
    }

    /**
     * Returns the article type corresponding to this comment or
     * correction.
     *
     * @return the article type corresponding to this comment or
     * correction ({@code null} if there is no unique mapping for
     * this type).
     */
    public ArticleType getArticleType() {
        return articleType;
    }
}
