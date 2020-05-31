
package pubmed.article;

import java.util.List;

import jam.text.TextMatcher;
import jam.util.ListUtil;

import pubmed.mesh.MeshDescriptorKey;

/**
 * Enumerates the types of articles contained in the {@code PubMed}
 * database.
 */
public enum ArticleType {
    /**
     * Typically short reports on individual patient cases.
     */
    CASE_REPORT("D002363", List.of("case report"), List.of()),

    /**
     * A comment on a previously published article or the response to
     * a comment.
     */
    COMMENT("D016420", List.of(), List.of("Comment ", "Re: ", "In reply to ", "Reply to ", "Response to ")),

    /**
     * Editorial or introductory article.
     */
    EDITORIAL("D016421", List.of(), List.of()),

    /**
     * An error in a previously published article.
     */
    ERRATUM("D016425", List.of(), List.of("Correction", "Corrigendum", "Erratum")),

    /**
     * An editorial expression of concern about a previously published
     * article, often leading to a retraction.
     */
    EXPRESSION_OF_CONCERN("D000075742", List.of("expression of concern", "notice of concern"), List.of()),

    /**
     * A letter to the editors or readers, typically not peer-reviewed.
     */
    LETTER("D016422", List.of(), List.of("A letter to ", "Letter to ")),

    /**
     * A meta-analysis of previously published work, possibly
     * containing new conclusions but no original data.
     */
    META_ANALYSIS("D017418", List.of("meta-analysis"), List.of()),

    /**
     * An original, peer-reviewed journal article.
     */
    ORIGINAL_JOURNAL("D016428", List.of(), List.of()),

    /**
     * A retraction notice for a previously published article.
     */
    RETRACTION_NOTICE("D016440", List.of(), List.of("Retracted: ")),

    /**
     * A retracted article.
     */
    RETRACTED_PUBLICATION("D016441", List.of(), List.of()),

    /**
     * A review of prior work in a subject area.
     */
    REVIEW("D016454", List.of("review of", "systematic review"), List.of());

    private final MeshDescriptorKey pubTypeKey;
    private final List<String> titleKeywords;
    private final List<String> titlePrefixes;

    private final TextMatcher titleKeywordMatcher;

    private ArticleType(String pubTypeKey, List<String> titleKeywords, List<String> titlePrefixes) {
        this.pubTypeKey = MeshDescriptorKey.instance(pubTypeKey);
        this.titleKeywords = titleKeywords;
        this.titlePrefixes = titlePrefixes;

        this.titleKeywordMatcher =
            titleKeywords.isEmpty() ? null : TextMatcher.ignoreCase(titleKeywords);
    }

    /**
     * Determines the article type from the publication types and
     * title.
     *
     * @param title the title of the article.
     *
     * @param commCorrList the comment-correction records for the
     * article.
     *
     * @param pubTypeList the {@code MeSH} publication types for the
     * article.
     *
     * @return the enumerated type for the article.
     */
    public static ArticleType resolve(String title,
                                      List<CommentCorrection> commCorrList,
                                      List<MeshDescriptorKey> pubTypeList) {
        if (commCorrList.size() == 1)
            return commCorrList.get(0).getCommCorrType().getArticleType();
        
        for (ArticleType type : ArticleType.values()) {
            if (!type.equals(ORIGINAL_JOURNAL) && type.matchesPubType(pubTypeList))
                return type;

            if (type.matchesTitlePrefix(title))
                return type;

            if (type.matchesTitleKeyword(title))
                return type;
        }

        return ORIGINAL_JOURNAL;
    }

    private boolean matchesPubType(List<MeshDescriptorKey> pubTypes) {
        return pubTypes.contains(pubTypeKey);
    }

    private boolean matchesTitlePrefix(String title) {
        for (String prefix : titlePrefixes)
            if (title.startsWith(prefix))
                return true;

        return false;
    }

    private boolean matchesTitleKeyword(String title) {
        return titleKeywordMatcher != null && titleKeywordMatcher.matches(title);
    }

    /**
     * Returns the key of the {@code MeSH} descriptor that identifies
     * articles of this type.
     *
     * @return the key of the {@code MeSH} descriptor that identifies
     * articles of this type.
     */
    public MeshDescriptorKey getPubTypeKey() {
        return pubTypeKey;
    }

    /**
     * Returns a list of keywords or phrases contained in titles of
     * articles of this type.
     *
     * @return a list of keywords or phrases contained in titles of
     * articles of this type.
     */
    public List<String> getTitleKeywords() {
        return titleKeywords;
    }

    /**
     * Returns a list of title prefixes that identify articles of this
     * type (the title {@code startsWith} one of these prefixes).
     *
     * @return a list of title prefixes that identify articles of this
     * type (the title {@code startsWith} one of these prefixes).
     */
    public List<String> getTitlePrefixes() {
        return titlePrefixes;
    }
}
