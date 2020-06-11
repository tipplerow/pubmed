
package pubmed.article;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import jam.lang.JamException;
import jam.text.TextMatcher;
import jam.util.FixedList;
import jam.util.ListUtil;

import pubmed.medline.MedlineJournal;
import pubmed.mesh.MeshDescriptorKey;
import pubmed.mesh.MeshHeading;
import pubmed.mesh.MeshQualifierKey;
import pubmed.mesh.MeshRecordKey;
import pubmed.mesh.MeshTreeNumber;
import pubmed.nlp.LemmaAnnotator;
import pubmed.nlp.LemmaList;

/**
 * Represents one article from the {@code PubMed} database.
 */
public final class PubmedArticle {
    private final PMID pmid;
    private final int version;
    private final DOI doi;
    private final String title;
    private final String abstract_;
    private final String coiStatement;
    private final PubmedDate pubDate;
    private final MedlineJournal medlineJournal;

    private final List<PMID> referenceList;
    private final List<String> keywordList;
    private final List<PubmedAuthor> authorList;
    private final List<MeshHeading> headingList;
    private final List<MeshRecordKey> chemicalList;
    private final List<MeshDescriptorKey> pubTypes;
    private final List<CommentCorrection> commCorrList;

    private final ArticleType articleType;

    // Lemmatized title, abstract, and keywords...
    private LemmaList titleLemmas = null;
    private LemmaList abstractLemmas = null;
    private List<LemmaList> keywordLemmas = null;

    private PubmedArticle(PMID pmid,
                          int version,
                          DOI doi,
                          String title,
                          String abstract_,
                          String coiStatement,
                          PubmedDate pubDate,
                          MedlineJournal medlineJournal,
                          List<PubmedAuthor> authorList,
                          List<PMID> referenceList,
                          List<String> keywordList,
                          List<MeshHeading> headingList,
                          List<MeshRecordKey> chemicalList,
                          List<MeshDescriptorKey> pubTypes,
                          List<CommentCorrection> commCorrList) {
        this.pmid = pmid;
        this.version = version;
        this.doi = doi;
        this.title = title;
        this.pubDate = pubDate;
        this.abstract_ = abstract_;
        this.coiStatement = coiStatement;
        this.medlineJournal = medlineJournal;

        this.pubTypes = FixedList.create(pubTypes);
        this.authorList = FixedList.create(authorList);
        this.keywordList = FixedList.create(keywordList);
        this.headingList = FixedList.create(headingList);
        this.chemicalList = FixedList.create(chemicalList);
        this.commCorrList = FixedList.create(commCorrList);
        this.referenceList = FixedList.create(referenceList);

        this.articleType = ArticleType.resolve(title, commCorrList, pubTypes);
    }

    private ArticleType resolveArticleType() {
        return null;
    }

    /**
     * Orders articles by their unique identifiers.
     */
    public static final Comparator<PubmedArticle> PMID_COMPARATOR =
        new Comparator<PubmedArticle>() {
            @Override public int compare(PubmedArticle article1, PubmedArticle article2) {
                return article1.pmid.compareTo(article2.pmid);
            }
        };

    /**
     * Creates a new {@code PubMed} article.
     *
     * @param pmid the unique {@code PubMed} identifier for the article.
     *
     * @param version the article version indicated in the bulk file.
     *
     * @param doi the unique {@code DOI} identifier for the article
     * (may be {@code null} if not known).
     *
     * @param title the article title.
     *
     * @param abstract_ the full text of the abstract.
     *
     * @param pubDate the first publication date.
     *
     * @param coiStatement the conflict-of-interest statement.
     *
     * @param medlineJournal the journal where the article appeared.
     *
     * @param authorList the authors of the article.
     *
     * @param referenceList the articles cited by the article.
     *
     * @param keywordList key words associated with the article.
     *
     * @param headingList {@code MeSH} headings associated with the
     * article.
     *
     * @param chemicalList {@code MeSH} descriptors or supplemental
     * records for substances mentioned in the article.
     *
     * @param pubTypes {@code MeSH} descriptor keys for the
     * publication types.
     *
     * @param commCorrList comment or correction records for the
     * article.
     *
     * @return the new {@code PubMed} article.
     */
    public static PubmedArticle create(PMID pmid,
                                       int version,
                                       DOI doi,
                                       String title,
                                       String abstract_,
                                       String coiStatement,
                                       PubmedDate pubDate,
                                       MedlineJournal medlineJournal,
                                       List<PubmedAuthor> authorList,
                                       List<PMID> referenceList,
                                       List<String> keywordList,
                                       List<MeshHeading> headingList,
                                       List<MeshRecordKey> chemicalList,
                                       List<MeshDescriptorKey> pubTypes,
                                       List<CommentCorrection> commCorrList) {
        return new PubmedArticle(pmid,
                                 version,
                                 doi,
                                 title,
                                 abstract_,
                                 coiStatement,
                                 pubDate,
                                 medlineJournal,
                                 authorList,
                                 referenceList,
                                 keywordList,
                                 headingList,
                                 chemicalList,
                                 pubTypes,
                                 commCorrList);
    }

    /**
     * Retains only articles with a {@code PMID} greater than a
     * reference (e.g., the largest PMID currently in a table).
     *
     * @param articles a collection to filter.
     *
     * @param threshold the {@code PMID} threshold.
     *
     * @return a list containing only articles whose {@code PMID} is
     * greater that the threshold.
     */
    public static List<PubmedArticle> filterGreaterPMID(Collection<PubmedArticle> articles, PMID threshold) {
	return ListUtil.filter(articles, article -> article.pmid.compareTo(threshold) > 0);
    }

    /**
     * Retains only the latest version of each article in a collection
     * but otherwise maintains the iteration order.
     *
     * @param articles a collection to filter.
     *
     * @return a list containing only the latest version of each
     * article in the input collection, in the iteration order of
     * the input collection.
     */
    public static List<PubmedArticle> latest(Collection<PubmedArticle> articles) {
        Map<PMID, PubmedArticle> latestMap =
            new LinkedHashMap<PMID, PubmedArticle>(articles.size());

        for (PubmedArticle challenger : articles) {
            //
            // An article with the same PMID that is already in the
            // map is the "champion".  The challenger must supersede
            // the champion (have a greater version number) in order
            // to replace it...
            //
            PMID key = challenger.getPMID();
            PubmedArticle champion = latestMap.get(key);

            if (champion == null || challenger.supersedes(champion))
                latestMap.put(key, challenger);
        }

        List<PubmedArticle> latestList =
            new ArrayList<PubmedArticle>(latestMap.values());

        return latestList;
    }

    /**
     * Extracts the identifiers from a collection of articles.
     *
     * @param articles a collection to process.
     *
     * @return a list containing the {@code PMID} for each article in
     * the input collection (in the collection iterator order).
     */
    public static List<PMID> listPMIDs(Collection<PubmedArticle> articles) {
        return ListUtil.apply(articles, article -> article.getPMID());
    }

    /**
     * Returns the number of {@code MeSH} chemicals in this article.
     *
     * @return the number of {@code MeSH} chemicals in this article.
     */
    public int countChemicals() {
        return chemicalList.size();
    }

    /**
     * Returns the number of {@code MeSH} headings in this article.
     *
     * @return the number of {@code MeSH} headings in this article.
     */
    public int countHeadings() {
        return headingList.size();
    }

    /**
     * Returns the number of keywords in this article.
     *
     * @return the number of keywords in this article.
     */
    public int countKeywords() {
        return keywordList.size();
    }

    /**
     * Returns the number of publication types for this article.
     *
     * @return the number of publication types for this article.
     */
    public int countPubTypes() {
        return pubTypes.size();
    }

    /**
     * Returns the enumerated type for this article.
     *
     * @return the enumerated type for this article.
     */
    public ArticleType getType() {
        return articleType;
    }

    /**
     * Returns the unique {@code PubMed} identifier for this article.
     *
     * @return the unique {@code PubMed} identifier for this article.
     */
    public PMID getPMID() {
        return pmid;
    }

    /**
     * Returns the article version as indicated in the bulk file.
     *
     * @return the article version as indicated in the bulk file.
     */
    public int getVersion() {
        return version;
    }

    /**
     * Returns the unique {@code DOI} identifier for this article.
     *
     * @return the unique {@code DOI} identifier for this article.
     */
    public DOI getDOI() {
        return doi;
    }

    /**
     * Returns the title of this article.
     *
     * @return the title of this article.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Returns the lemmatized content words from the title of this
     * article.
     *
     * @return the lemmatized content words from the title of this
     * article.
     */
    public LemmaList getTitleLemmas() {
        if (titleLemmas == null)
            lemmatizeTitle();

        return titleLemmas;
    }

    private void lemmatizeTitle() {
        if (hasTitle())
            titleLemmas = LemmaAnnotator.contentWords(title);
        else
            titleLemmas = LemmaList.EMPTY;
    }

    /**
     * Returns the full abstract text for this article.
     *
     * @return the full abstract text for this article.
     */
    public String getAbstract() {
        return abstract_;
    }

    /**
     * Returns the lemmatized content words from the abstract of this
     * article.
     *
     * @return the lemmatized content words from the abstract of this
     * article.
     */
    public LemmaList getAbstractLemmas() {
        if (abstractLemmas == null)
            lemmatizeAbstract();

        return abstractLemmas;
    }

    private void lemmatizeAbstract() {
        if (hasAbstract())
            abstractLemmas = LemmaAnnotator.contentWords(abstract_);
        else
            titleLemmas = LemmaList.EMPTY;
    }

    /**
     * Returns the conflict-of-interest statement for this article.
     *
     * @return the conflict-of-interest statement for this article.
     */
    public String getCOIStatement() {
        return coiStatement;
    }

    /**
     * Returns the keys of the descriptors in the heading list for
     * this article.
     *
     * @return the keys of the descriptors in the heading list for
     * this article.
     */
    public Set<MeshDescriptorKey> getHeadingDescriptors() {
	Set<MeshDescriptorKey> keys =
	    new TreeSet<MeshDescriptorKey>();

	for (MeshHeading heading : headingList)
	    keys.add(heading.getDescriptorKey());

	return keys;
    }

    /**
     * Returns the keys of the qualifiers in the heading list for
     * this article.
     *
     * @return the keys of the qualifiers in the heading list for
     * this article.
     */
    public Set<MeshQualifierKey> getHeadingQualifiers() {
	Set<MeshQualifierKey> keys =
	    new TreeSet<MeshQualifierKey>();

	for (MeshHeading heading : headingList)
	    keys.addAll(heading.viewQualifierKeys());

	return keys;
    }

    /**
     * Returns the journal where this article appeared.
     *
     * @return the journal where this article appeared.
     */
    public MedlineJournal getMedlineJournal() {
        return medlineJournal;
    }

    /**
     * Returns the first publication date for this article.
     *
     * @return the first publication date for this article.
     */
    public PubmedDate getPubDate() {
        return pubDate;
    }

    /**
     * Returns the {@code MeSH} tree numbers for the descriptors
     * contained in the heading list for this article.
     *
     * @return the {@code MeSH} tree numbers for all descriptors
     * contained in the heading list for this article.
     */
    public Set<MeshTreeNumber> getTreeNumbers() {
        Set<MeshTreeNumber> numbers =
            new TreeSet<MeshTreeNumber>();

	for (MeshHeading heading : headingList)
            numbers.addAll(heading.getNumberList());

        return numbers;
    }

    /**
     * Identifies articles that contain abstracts.
     *
     * @return {@code true} iff this article as a non-empty abstract.
     */
    public boolean hasAbstract() {
        return abstract_ != null && !abstract_.isEmpty();
    }

    /**
     * Identifies articles with one or more known authors.
     *
     * @return {@code true} iff this article has one or more authors.
     */
    public boolean hasAuthors() {
        return authorList != null && !authorList.isEmpty();
    }

    /**
     * Identifies articles with a chemical list component matching
     * a target substance.
     *
     * @param targetKey the key of the target substance.
     *
     * @return {@code true} iff this article contains the target
     * substance in its chemical list.
     */
    public boolean hasChemical(MeshRecordKey targetKey) {
        return chemicalList != null && chemicalList.contains(targetKey);
    }

    /**
     * Identifies articles with one or more associated chemical
     * substances.
     *
     * @return {@code true} iff one or more chemical substances are
     * associated with this article.
     */
    public boolean hasChemicalList() {
        return chemicalList != null && !chemicalList.isEmpty();
    }

    /**
     * Identifies articles with a {@code MeSH} heading that matches a
     * specific descriptor.
     *
     * @param descriptorKey the descriptor key of interest.
     *
     * @return {@code true} iff this article contains a {@code MeSH}
     * heading with the specified descriptor.
     */
    public boolean hasHeading(MeshDescriptorKey descriptorKey) {
	if (headingList == null)
	    return false;

	for (MeshHeading heading : headingList)
	    if (heading.getDescriptorKey().equals(descriptorKey))
		return true;

        return false;
    }

    /**
     * Identifies articles with one or more associated {@code MeSH}
     * headings.
     *
     * @return {@code true} iff one or more {@code MeSH} headings are
     * associated with this article.
     */
    public boolean hasHeadingList() {
        return headingList != null && !headingList.isEmpty();
    }

    /**
     * Identifies articles with one or more associated keywords.
     *
     * @return {@code true} iff one or more keywords are associated
     * with this article.
     */
    public boolean hasKeywordList() {
        return keywordList != null && !keywordList.isEmpty();
    }

    /**
     * Identifies articles with publication types in addition to
     * or in place of the standard {@code 'Journal Article'}.
     *
     * @return {@code true} iff this article contains one or more
     * publication types that are not {@code 'Journal Article'}.
     */
    public boolean hasNonTrivialPubTypes() {
        return pubTypes != null && !pubTypes.equals(List.of(MeshDescriptorKey.JOURNAL_ARTICLE));
    }

    /**
     * Identifies articles with one or more references.
     *
     * @return {@code true} iff this article cites one or more
     * other articles.
     */
    public boolean hasReferences() {
        return referenceList != null && !referenceList.isEmpty();
    }

    /**
     * Identifies articles that contain titles.
     *
     * @return {@code true} iff this article as a non-empty title.
     */
    public boolean hasTitle() {
        return title != null && !title.isEmpty();
    }

    /**
     * Identifies articles that are revisions of previous
     * {@code PubMed} articles.
     *
     * @return {@code true} iff this article is a revision
     * of a previous article.
     */
    public boolean isRevision() {
        return version > 1;
    }

    /**
     * Identifies articles that are revisions of previous versions of
     * the same article.
     *
     * @param that the other article to examine.
     *
     * @return {@code true} iff this article has the same {@code PMID}
     * as the input article <em>and a higher version number</em>.
     */
    public boolean supersedes(PubmedArticle that) {
        return this.pmid.equals(that.pmid) && this.version > that.version;
    }

    /**
     * Returns the authors of this article.
     *
     * @return the authors of this article.
     */
    public List<PubmedAuthor> viewAuthorList() {
        return authorList;
    }

    /**
     * Returns the {@code MeSH} descriptors or supplemental records
     * for substances mentioned in this article.
     *
     * @return the {@code MeSH} descriptors or supplemental records
     * for substances mentioned in this article.
     */
    public List<MeshRecordKey> viewChemicalList() {
        return chemicalList;
    }

    /**
     * Returns the comments or corrections in this article.
     *
     * @return the comments or corrections in this article.
     */
    public List<CommentCorrection> viewCommCorrList() {
        return commCorrList;
    }

    /**
     * Returns the {@code MeSH} headings associated with this article.
     *
     * @return the {@code MeSH} headings associated with this article.
     */
    public List<MeshHeading> viewHeadingList() {
        return headingList;
    }

    /**
     * Returns the <em>lemmatized</em> key words associated with this
     * article.
     *
     * @return the <em>lemmatized</em> key words associated with this
     * article.
     */
    public List<LemmaList> viewKeywordLemmas() {
        if (keywordLemmas == null)
            keywordLemmas = Collections.unmodifiableList(LemmaAnnotator.contentWords(keywordList));

        return keywordLemmas;
    }

    /**
     * Returns the key words associated with this article.
     *
     * @return the key words associated with this article.
     */
    public List<String> viewKeywordList() {
        return keywordList;
    }

    /**
     * Returns the {@code MeSH} publication types for this article.
     *
     * @return the {@code MeSH} publication types for this article.
     */
    public List<MeshDescriptorKey> viewPubTypes() {
        return pubTypes;
    }

    /**
     * Returns the {@code PubMed} identifiers of the articles cited by
     * this article.
     *
     * @return the {@code PubMed} identifiers of the articles cited by
     * this article.
     */
    public List<PMID> viewReferenceList() {
        return referenceList;
    }

    @Override public String toString() {
        return String.format("PubmedArticle(%d)", pmid.intValue());
    }
}
