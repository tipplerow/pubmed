
package pubmed.article;

import java.util.HashMap;
import java.util.Map;

/**
 * Enumerates relevant categories of comments and corrections to
 * previously published articles.
 */
public enum CommentCorrectionType {
    COMMENT_ON("CommentOn", ArticleType.COMMENT),
    ERRATUM_FOR("ErratumFor", ArticleType.ERRATUM),
    EXPRESSION_OF_CONCERN_FOR("ExpressionOfConcernFor", ArticleType.EXPRESSION_OF_CONCERN),
    RETRACTION_OF("RetractionOf", ArticleType.RETRACTION_NOTICE);

    private final String xmlAttribute;
    private final ArticleType articleType;

    private static final Map<String, CommentCorrectionType> attributeMap =
        new HashMap<String, CommentCorrectionType>();

    static {
        fillAttributeMap();
    }

    private static void fillAttributeMap() {
        for (CommentCorrectionType type : values())
            attributeMap.put(type.xmlAttribute, type);
    }

    private CommentCorrectionType(String xmlAttribute, ArticleType articleType) {
        this.articleType = articleType;
        this.xmlAttribute = xmlAttribute;
    }

    /**
     * Returns the type that corresponds to an XML attribute
     * string.
     *
     * @param xmlAttribute an attribute string from an XML
     * document file.
     *
     * @return the type that corresponds to the specified XML
     * attribute string.
     */
    public static CommentCorrectionType parseAttribute(String xmlAttribute) {
        return attributeMap.get(xmlAttribute);
    }

    /**
     * Returns the article type corresponding to this comment or
     * correction.
     *
     * @return the article type corresponding to this comment or
     * correction.
     */
    public ArticleType getArticleType() {
        return articleType;
    }
}
