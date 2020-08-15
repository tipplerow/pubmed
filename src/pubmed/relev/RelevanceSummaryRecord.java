
package pubmed.relev;

import java.time.LocalDate;

import jam.io.Delimiter;
import jam.report.LineBuilder;

import pubmed.article.PMID;
import pubmed.flat.RelevanceScoreRecord;

/**
 * Represents a joining record containing the identifier of an
 * article, the key of a subject covered in that article, scoring
 * metrics that quantify the degree of relevance of the article to
 * the subject, and article attributes like the title, publication
 * date, PubMed URL, and DOI.
 */
public final class RelevanceSummaryRecord {
    private final PMID pmid;
    private final String subjectKey;

    private final String title;
    private final String journal;
    private final String doiURL;
    private final String pubMedURL;
    private final LocalDate pubDate;
    private final LocalDate reportDate;

    private final int titleScore;
    private final int abstractScore;
    private final int meshTreeScore;
    private final int headingListScore;
    private final int keywordListScore;
    private final int chemicalListScore;

    private RelevanceSummaryRecord(PMID      pmid,
                                   String    subjectKey,
                                   LocalDate pubDate,
                                   LocalDate reportDate,
                                   String    title,
                                   String    journal,
                                   String    pubMedURL,
                                   String    doiURL,
                                   int       titleScore,
                                   int       abstractScore,
                                   int       meshTreeScore,
                                   int       headingListScore,
                                   int       keywordListScore,
                                   int       chemicalListScore) {
        this.pmid = pmid;
        this.subjectKey = subjectKey;
        
        this.title = title;
        this.doiURL = doiURL;
        this.journal = journal;
        this.pubDate = pubDate;
        this.pubMedURL = pubMedURL;
        this.reportDate = reportDate;

        this.titleScore = titleScore;
        this.abstractScore = abstractScore;
        this.meshTreeScore = meshTreeScore;
        this.headingListScore = headingListScore;
        this.keywordListScore = keywordListScore;
        this.chemicalListScore = chemicalListScore;
    }

    /**
     * The minimum number of keyword occurrences required in the
     * article abstract in order to qualify as relevant (barring
     * other qualifying criteria).
     */
    public static final int ABSTRACT_KEYWORD_THRESHOLD = 3;

    /**
     * The flat file delimiter for relevance summary records.
     */
    public static final Delimiter DELIMITER = Delimiter.TAB;

    /**
     * Creates a new record with fixed attributes.
     *
     * @param pmid the article identifier.
     *
     * @param subjectKey a key of the subject covered in the article.
     *
     * @param pubDate the publication date of the article.
     *
     * @param reportDate the date when the record was added to the
     * report file.
     *
     * @param title the title of the article.
     *
     * @param journal the journal where the article appeared.
     *
     * @param pubMedURL the URL to the PubMed article page.
     *
     * @param doiURL the URL for the Digital Object Identifier.
     *
     * @param titleScore the title relevance score.
     *
     * @param abstractScore the abstract relevance score.
     *
     * @param meshTreeScore the MeSH tree relevance score.
     *
     * @param headingListScore the heading list relevance score.
     *
     * @param keywordListScore the keyword list relevance score.
     *
     * @param chemicalListScore the chemical list relevance score.
     *
     * @return a new record with the specified attributes.
     */
    public static RelevanceSummaryRecord create(PMID      pmid,
                                                String    subjectKey,
                                                LocalDate pubDate,
                                                LocalDate reportDate,
                                                String    title,
                                                String    journal,
                                                String    pubMedURL,
                                                String    doiURL,
                                                int       titleScore,
                                                int       abstractScore,
                                                int       meshTreeScore,
                                                int       headingListScore,
                                                int       keywordListScore,
                                                int       chemicalListScore) {
        return new RelevanceSummaryRecord(pmid,
                                          subjectKey,
                                          pubDate,
                                          reportDate,
                                          title,
                                          journal,
                                          pubMedURL,
                                          doiURL,
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
     * @param scoreRecord the relevance score record for the summary.
     *
     * @param pubDate the publication date of the article.
     *
     * @param reportDate the date when the record was added to the
     * report file.
     *
     * @param title the title of the article.
     *
     * @param journal the journal where the article appeared.
     *
     * @param pubMedURL the URL to the PubMed article page.
     *
     * @param doiURL the URL for the Digital Object Identifier.
     *
     * @return a new record with the specified attributes.
     */
    public static RelevanceSummaryRecord create(RelevanceScoreRecord scoreRecord,
                                                LocalDate pubDate,
                                                LocalDate reportDate,
                                                String title,
                                                String journal,
                                                String pubMedURL,
                                                String doiURL) {
        return create(scoreRecord.getPMID(),
                      scoreRecord.getSubjectKey(),
                      pubDate,
                      reportDate,
                      title,
                      journal,
                      pubMedURL,
                      doiURL,
                      scoreRecord.getTitleScore(),
                      scoreRecord.getAbstractScore(),
                      scoreRecord.getMeshTreeScore(),
                      scoreRecord.getHeadingListScore(),
                      scoreRecord.getKeywordListScore(),
                      scoreRecord.getChemicalListScore());
    }

    /**
     * Returns the header line for relevance summary files.
     *
     * @return the header line for relevance summary files.
     */
    public static String header() {
        LineBuilder builder = new LineBuilder(DELIMITER);

        builder.append("pmid");
        builder.append("subject");
        builder.append("pubDate");
        builder.append("reportDate");
        builder.append("title");
        builder.append("journal");
        builder.append("pubMedURL");
        builder.append("doiURL");
        builder.append("titleScore");
        builder.append("abstractScore");
        builder.append("meshTreeScore");
        builder.append("headingListScore");
        builder.append("keywordListScore");
        builder.append("chemicalListScore");
        builder.append("isLikelyMatch");

        return builder.toString();
    }

    /**
     * Parses a delimited line that encodes a relevance record.
     *
     * @param line the delimited line to parse.
     *
     * @return a new record with the data encoded in the line.
     */
    public static RelevanceSummaryRecord parse(String line) {
        //
        // The last field is the "likelyMatch" flag, which is derived
        // from the individual scores rather than parsed...
        //
        String[] fields = DELIMITER.split(line, 15);

        return create(PMID.instance(fields[0]),
                      fields[1],
                      LocalDate.parse(fields[2]),
                      LocalDate.parse(fields[3]),
                      fields[4],
                      fields[5],
                      fields[6],
                      fields[7],
                      Integer.parseInt(fields[8]),
                      Integer.parseInt(fields[9]),
                      Integer.parseInt(fields[10]),
                      Integer.parseInt(fields[11]),
                      Integer.parseInt(fields[12]),
                      Integer.parseInt(fields[13]));
    }

    /**
     * Formats this record as a delimited text field.
     *
     * @return a delimited string representation of this record.
     */
    public String format() {
        LineBuilder builder = new LineBuilder(DELIMITER);

        builder.append(pmid.intValue());
        builder.append(subjectKey);
        builder.append(pubDate);
        builder.append(reportDate);
        builder.append(title);
        builder.append(journal);
        builder.append(pubMedURL);
        builder.append(doiURL);
        builder.append(titleScore);
        builder.append(abstractScore);
        builder.append(meshTreeScore);
        builder.append(headingListScore);
        builder.append(keywordListScore);
        builder.append(chemicalListScore);
        builder.append(isLikelyMatch());

        return builder.toString();
    }

    /**
     * Returns the unique identifier for the article.
     *
     * @return the unique identifier for the article.
     */
    public PMID getPMID() {
        return pmid;
    }

    /**
     * Returns the key of the subject covered in the article.
     *
     * @return the key of the subject covered in the article.
     */
    public String getSubjectKey() {
        return subjectKey;
    }

    /**
     * Returns the publication date of the article.
     *
     * @return the publication date of the article.
     */
    public LocalDate getPubDate() {
        return pubDate;
    }

    /**
     * Returns the date when this record was added to the report file.
     *
     * @return the date when this record was added to the report file.
     */
    public LocalDate getReportDate() {
        return reportDate;
    }

    /**
     * Returns the title of the article.
     *
     * @return the title of the article.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Returns the journal where the article appeared.
     *
     * @return the journal where the article appeared.
     */
    public String getJournal() {
        return journal;
    }

    /**
     * Returns the URL to the PubMed article page.
     *
     * @return the URL to the PubMed article page.
     */
    public String getPubMedURL() {
        return pubMedURL;
    }

    /**
     * Returns the URL for the Digital Object Identifier.
     *
     * @return the URL for the Digital Object Identifier.
     */
    public String getDoiURL() {
        return doiURL;
    }

    /**
     * Returns the title relevance score.
     *
     * @return the title relevance score.
     */
    public int getTitleScore() {
        return titleScore;
    }

    /**
     * Returns the abstract relevance score.
     *
     * @return the abstract relevance score.
     */
    public int getAbstractScore() {
        return abstractScore;
    }

    /**
     * Returns the MeSH tree relevance score.
     *
     * @return the MeSH tree relevance score.
     */
    public int getMeshTreeScore() {
        return meshTreeScore;
    }

    /**
     * Returns the heading list relevance score.
     *
     * @return the heading list relevance score.
     */
    public int getHeadingListScore() {
        return headingListScore;
    }

    /**
     * Returns the keyword list relevance score.
     *
     * @return the keyword list relevance score.
     */
    public int getKeywordListScore() {
        return keywordListScore;
    }

    /**
     * Returns the chemical list relevance score.
     *
     * @return the chemical list relevance score.
     */
    public int getChemicalListScore() {
        return chemicalListScore;
    }

    /**
     * Denotes whether this record is likely to contain a relevant
     * article for its subject.
     *
     * @return {@code true} iff the record has been deemed likely to
     * contain a relevant article for its subject.
     */
    public boolean isLikelyMatch() {
        //
        // The MeSH taxonomy and keywords are targeted and specific,
        // so any positive match should identify a relevant article...
        //
        if (meshTreeScore > 0)
            return true;

        if (headingListScore > 0)
            return true;

        if (chemicalListScore > 0)
            return true;

        if (keywordListScore > 0)
            return true;

        // The MeSH taxonomy is very precise, so the absence of a
        // positive match likely indicates that the article is not
        // relevant. We exclude the keyword list from the rejection
        // criteria because many keyword lists are incomplete or use
        // non-standard abbreviations....
        if (meshTreeScore < 0)
            return false;

        if (headingListScore < 0)
            return false;

        if (chemicalListScore < 0)
            return false;

        // Okay, the MeSH taxonomy and keyword list could not decide
        // the relevance.  Require at least one keyword in the title
        // or three or more keyword occurrences in the abstract...
        return titleScore > 0 || abstractScore >= ABSTRACT_KEYWORD_THRESHOLD;
    }
}
