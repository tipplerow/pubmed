
package pubmed.sql;

import java.time.LocalDate;

import pubmed.article.ArticleType;
import pubmed.article.DOI;
import pubmed.article.PMID;
import pubmed.article.PubmedArticle;
import pubmed.article.PubmedDate;
import pubmed.medline.MedlineTA;

/**
 * Represents a row in the {@code articles} table.
 */
public final class ArticleRecord extends ArticleAttrRecord {
    private final DOI doi;

    private final MedlineTA   medlineTA;
    private final PubmedDate  pubDate;
    private final LocalDate   loadDate;
    private final ArticleType type;

    private final boolean hasTitle;
    private final boolean hasAbstract;
    private final boolean hasChemicalList;
    private final boolean hasHeadingList;
    private final boolean hasKeywordList;

    private ArticleRecord(PMID        pmid,
			  ArticleType type,
			  DOI         doi,
			  MedlineTA   medlineTA,
			  PubmedDate  pubDate,
                          LocalDate   loadDate,
			  boolean     hasTitle,
			  boolean     hasAbstract,
			  boolean     hasChemicalList,
			  boolean     hasHeadingList,
			  boolean     hasKeywordList) {
        super(pmid);
        
        this.doi = doi;
        this.type = type;
        this.pubDate = pubDate;
        this.loadDate = loadDate;
        this.medlineTA = medlineTA;

	this.hasTitle = hasTitle;
	this.hasAbstract = hasAbstract;
	this.hasHeadingList = hasHeadingList;
	this.hasKeywordList = hasKeywordList;
	this.hasChemicalList = hasChemicalList;
    }

    /**
     * Creates the {@code articles} record for an article.
     *
     * @param article the article to store.
     *
     * @return the {@code articles} record for the specified article.
     */
    public static ArticleRecord create(PubmedArticle article) {
        return create(article.getPMID(),
                      article.getType(),
                      article.getDOI(),
                      article.getMedlineJournal().getMedlineTA(),
                      article.getPubDate(),
                      LocalDate.now(),
                      article.hasTitle(),
		      article.hasAbstract(),
		      article.hasChemicalList(),
		      article.hasHeadingList(),
		      article.hasKeywordList());
    }

    /**
     * Creates a new article record from its fields.
     *
     * @param pmid the unique {@code PMID} identifier for the article.
     *
     * @param doi the unique {@code DOI} identifier for the article.
     *
     * @param type the enumerated article type.
     *
     * @param medlineTA the {@code MEDLINE} title abbreviation for the
     * journal where the article appeared.
     *
     * @param pubDate the first publication date for the article.
     *
     * @param loadDate the date on which the original record was created.
     *
     * @param hasTitle whether the article contains a non-{@code null}
     * title.
     *
     * @param hasAbstract whether the article contains a non-{@code null}
     * abstract.
     *
     * @param hasChemicalList whether the article contains a non-empty
     * {@code MeSH} chemical list.
     *
     * @param hasHeadingList whether the article contains a non-empty
     * {@code MeSH} heading list.
     *
     * @param hasKeywordList whether the article contains a non-empty
     * keyword list.
     *
     * @return the {@code articles} record with the specified fields.
     */
    public static ArticleRecord create(PMID        pmid,
                                       ArticleType type,
                                       DOI         doi,
                                       MedlineTA   medlineTA,
                                       PubmedDate  pubDate,
                                       LocalDate   loadDate,
				       boolean     hasTitle,
				       boolean     hasAbstract,
				       boolean     hasChemicalList,
				       boolean     hasHeadingList,
				       boolean     hasKeywordList) {
        return new ArticleRecord(pmid,
				 type,
				 doi,
				 medlineTA,
				 pubDate,
                                 loadDate,
				 hasTitle,
				 hasAbstract,
				 hasChemicalList,
				 hasHeadingList,
				 hasKeywordList);
    }

    /**
     * Returns the unique {@code DOI} identifier for the article.
     *
     * @return the unique {@code DOI} identifier for the article.
     */
    public DOI getDOI() {
        return doi;
    }

    /**
     * Returns the {@code MEDLINE} title abbreviation for the journal
     * where the article appeared.
     *
     * @return the {@code MEDLINE} title abbreviation for the journal
     * where the article appeared.
     */
    public MedlineTA getMedlineTA() {
        return medlineTA;
    }

    /**
     * Returns the first publication date for the article.
     *
     * @return the first publication date for the article.
     */
    public PubmedDate getPubDate() {
        return pubDate;
    }

    /**
     * Returns the date on which this record was created.
     *
     * @return the date on which this record was created.
     */
    public LocalDate getLoadDate() {
        return loadDate;
    }

    /**
     * Returns the enumerated type for the article.
     *
     * @return the enumerated type for the article.
     */
    public ArticleType getType() {
        return type;
    }

    /**
     * Indicates whether the article contains a non-{@code null}
     * title.
     *
     * @return {@code true} iff the article contains a non-{@code null}
     * title.
     */
    public boolean hasTitle() {
	return hasTitle;
    }

    /**
     * Indicates whether the article contains a non-{@code null}
     * abstract.
     *
     * @return {@code true} iff the article contains a non-{@code null}
     * abstract.
     */
    public boolean hasAbstract() {
	return hasAbstract;
    }

    /**
     * Indicates whether the article contains a non-empty {@code MeSH}
     * chemical list.
     *
     * @return {@code true} iff the article contains a non-empty
     * {@code MeSH} chemical list.
     */
    public boolean hasChemicalList() {
	return hasChemicalList;
    }

    /**
     * Indicates whether the article contains a non-empty {@code MeSH}
     * heading list.
     *
     * @return {@code true} iff the article contains a non-empty
     * {@code MeSH} heading list.
     */
    public boolean hasHeadingList() {
	return hasHeadingList;
    }

    /**
     * Indicates whether the article contains a non-empty keyword
     * list.
     *
     * @return {@code true} iff the article contains a non-empty
     * keyword list.
     */
    public boolean hasKeywordList() {
	return hasKeywordList;
    }

    @Override public String formatBulk() {
        return joinBulk(formatBulk(pmid),
                        formatBulk(type),
                        formatBulk(doi),
                        formatBulk(medlineTA),
                        formatBulk(pubDate),
                        formatBulk(loadDate),
			formatBulk(hasTitle),
			formatBulk(hasAbstract),
			formatBulk(hasChemicalList),
			formatBulk(hasHeadingList),
			formatBulk(hasKeywordList));
    }
}
