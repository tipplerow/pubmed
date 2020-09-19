
package pubmed.relev;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jam.app.JamLogger;
import jam.io.IOUtil;

import pubmed.article.DOI;
import pubmed.article.PMID;
import pubmed.article.PubmedJournal;
import pubmed.bulk.BulkContributorFile;
import pubmed.bulk.BulkFile;
import pubmed.flat.ArticleDOITable;
import pubmed.flat.ArticleTitleTable;
import pubmed.flat.JournalTable;
import pubmed.flat.PubDateTable;
import pubmed.flat.PMIDTable;
import pubmed.flat.RelevanceScoreRecord;
import pubmed.flat.RelevanceScoreTable;
import pubmed.subject.Subject;

/**
 * Generates and stores article-subject relevance summary records.
 */
public final class RelevanceSummarySubjectFile extends RelevanceSummaryFileBase {
    // The subject of this file...
    private final Subject subject;

    // A file containing the names of the bulk files that have
    // contributed to the contents of the summary file...
    private final BulkContributorFile contribFile;

    // One file per subject...
    private static final Map<Subject, RelevanceSummarySubjectFile> instances =
        new HashMap<Subject, RelevanceSummarySubjectFile>();

    private RelevanceSummarySubjectFile(Subject subject) {
        super(resolveSummaryFile(subject));

        this.subject = subject;
        this.contribFile = BulkContributorFile.instance(resolveContribFile(subject));
    }

    private static File resolveSummaryFile(Subject subject) {
        return new File(resolveRelevanceDir(), summaryBaseName(subject));
    }

    private static File resolveContribFile(Subject subject) {
        return new File(resolveRelevanceDir(), contribBaseName(subject));
    }

    private static String summaryBaseName(Subject subject) {
        return subject.getKey() + "_relevance_summary.txt";
    }

    private static String contribBaseName(Subject subject) {
        return subject.getKey() + "_relevance_contrib.txt";
    }

    /**
     * Returns the relevance summary file for a subject.
     *
     * @param subject the subject of the relevance file.
     *
     * @return the relevance summary file for the specified subject.
     */
    public static RelevanceSummarySubjectFile instance(Subject subject) {
        synchronized (subject) {
            RelevanceSummarySubjectFile instance = instances.get(subject);

            if (instance == null) {
                instance = new RelevanceSummarySubjectFile(subject);
                instances.put(subject, instance);
            }

            return instance;
        }
    }

    /**
     * Loads the relevance summary records from the file for a given
     * subject.
     *
     * @param subject the subject of interest.
     *
     * @return the relevance summary records from the file for the
     * specified subject.
     */
    public static RelevanceSummaryTable load(Subject subject) {
        return instance(subject).load();
    }

    /**
     * Loads the identifiers for articles that are deemed likely to be
     * relevant to a given subject.
     *
     * @param subject the subject of interest.
     *
     * @return a set containing the identifiers for articles that are
     * deemed likely to be relevant to the specified subject.
     */
    public static Set<PMID> loadRelevantPMID(Subject subject) {
        return load(subject).getRelevantPMID();
    }

    /**
     * Processes one bulk file:  Loads all relevance scores derived
     * from the bulk file, aggregates relevance scores into summary
     * records for each subject, appends the summary records to the
     * underlying physical files, and records the bulk file as
     * processed.
     *
     * @param bulkFile the bulk file to process.
     *
     * @param subjects the subjects to process.
     *
     * @throws RuntimeException if any errors occur.
     */
    public static synchronized void process(BulkFile bulkFile, Collection<Subject> subjects) {
        JamLogger.info("Updating relevance summary files with [%s]....", bulkFile.getBaseName());

        bulkFile.getRelevanceScoreFile().process(subjects);

        JournalTable journalTable = bulkFile.getJournalFile().load();
        PubDateTable pubDateTable = bulkFile.getPubDateFile().load();
        ArticleDOITable articleDOITable = bulkFile.getArticleDOIFile().load();
        ArticleTitleTable articleTitleTable = bulkFile.getArticleTitleFile().load();
        RelevanceScoreTable relevanceScoreTable = bulkFile.getRelevanceScoreFile().load();

        subjects.parallelStream().forEach(subject ->
                                          instance(subject).process(bulkFile,
                                                                    journalTable,
                                                                    pubDateTable,
                                                                    articleDOITable,
                                                                    articleTitleTable,
                                                                    relevanceScoreTable));
    }

    private void process(BulkFile bulkFile,
                         JournalTable journalTable,
                         PubDateTable pubDateTable,
                         ArticleDOITable articleDOITable,
                         ArticleTitleTable articleTitleTable,
                         RelevanceScoreTable relevanceScoreTable) {
        if (isContributor(bulkFile))
            return;

        JamLogger.info("Processing relevance summary file: [%s]...", subject);

        LocalDate reportDate = LocalDate.now();

        List<RelevanceScoreRecord> scoreRecords =
            relevanceScoreTable.selectForeign(subject.getKey());

        List<RelevanceSummaryRecord> summaryRecords =
            new ArrayList<RelevanceSummaryRecord>();

        for (RelevanceScoreRecord scoreRecord : scoreRecords) {
            PMID pmid = scoreRecord.getPMID();

            DOI doi = articleDOITable.selectDOI(pmid);
            String title = articleTitleTable.selectTitle(pmid);
            LocalDate pubDate = pubDateTable.selectPubDate(pmid);
            PubmedJournal journal = journalTable.selectJournal(pmid);

            if (doi == null)
                continue;

            if (title == null)
                continue;

            if (pubDate == null)
                continue;

            if (journal == null)
                continue;

            summaryRecords.add(RelevanceSummaryRecord.create(scoreRecord,
                                                             pubDate,
                                                             reportDate,
                                                             title,
                                                             journal.getISOAbbreviation(),
                                                             pmid.asURL(),
                                                             doi.asURL()));
        }

        appendSummaryRecords(summaryRecords);
        contribFile.addContributor(bulkFile);
    }

    /**
     * Deletes this relevance summary file and its corresponding
     * contributor file.
     *
     * @return {@code true} iff both the summary and contributor
     * files were successfully deleted.
     */
    @Override public boolean delete() {
        boolean status1 = super.delete();
        boolean status2 = contribFile.delete();

        return status1 && status2;
    }

    /**
     * Determines whether a bulk file has already contributed to this
     * summary file.
     *
     * @param bulkFile the bulk file in question.
     *
     * @return {@code true} iff the specified bulk file has already
     * contributed to this summary file.
     */
    public boolean isContributor(BulkFile bulkFile) {
        return contribFile.isContributor(bulkFile);
    }
}
