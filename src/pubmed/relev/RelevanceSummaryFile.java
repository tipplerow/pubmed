
package pubmed.relev;

import java.io.File;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import jam.app.JamEnv;
import jam.app.JamLogger;
import jam.app.JamProperties;
import jam.io.FileUtil;
import jam.io.IOUtil;

import pubmed.article.DOI;
import pubmed.article.PMID;
import pubmed.article.PubmedJournal;
import pubmed.bulk.ArticleDOIFile;
import pubmed.bulk.ArticleTitleFile;
import pubmed.bulk.BulkFile;
import pubmed.bulk.JournalFile;
import pubmed.bulk.PubDateFile;
import pubmed.bulk.RelevanceScoreFile;
import pubmed.flat.ArticleDOITable;
import pubmed.flat.ArticleTitleTable;
import pubmed.flat.JournalTable;
import pubmed.flat.PubDateTable;
import pubmed.flat.RelevanceScoreRecord;
import pubmed.flat.RelevanceScoreTable;
import pubmed.subject.Subject;

/**
 * Provides a base class for flat files that contain relevance score
 * records.
 */
public final class RelevanceSummaryFile {
    // The subject of this file...
    private final Subject subject;

    // The file containing the summary records...
    private final File summaryFile;

    // A file containing the full path names of the bulk files that
    // have contributed to the contents of the summary file.
    private final File contribFile;

    private RelevanceSummaryFile(Subject subject) {
        this.subject = subject;
        this.summaryFile = resolveSummaryFile();
        this.contribFile = resolveContribFile();
    }

    private File resolveSummaryFile() {
        return new File(resolveRelevanceDir(), summaryBaseName());
    }

    private File resolveContribFile() {
        return new File(resolveRelevanceDir(), contribBaseName());
    }

    private String summaryBaseName() {
        return subject.getKey() + "_relevance_summary.txt";
    }

    private String contribBaseName() {
        return subject.getKey() + "_relevance_contrib.txt";
    }

    /**
     * Name of the subdirectory (under the PubMed parent) that
     * contains the relevance summary files.
     */
    public static final String RELEV_DIR_NAME = "relev";

    /**
     * Name of the system property that specifies the full path to
     * the relevance file directory.  If this property is not set,
     * the directory will resolve to $PUBMED_LOCAL_DIR/relev.
     */
    public static final String RELEV_DIR_PROPERTY = "pubmed.relev.dirName";

    /**
     * Returns the relevance summary file for a subject.
     *
     * @param subject the subject of the relevance file.
     *
     * @return the relevance summary file for the specified subject.
     */
    public static RelevanceSummaryFile instance(Subject subject) {
        return new RelevanceSummaryFile(subject);
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
        FileUtil.ensureDir(resolveRelevanceDir());
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

    /**
     * Resolves the location of the relevance summary file set.
     *
     * @return the directory containing the relevance summary files.
     */
    public static File resolveRelevanceDir() {
        if (JamProperties.isSet(RELEV_DIR_PROPERTY))
            return new File(JamProperties.getRequired(RELEV_DIR_PROPERTY));
        else
            return new File(JamEnv.getRequired(BulkFile.LOCAL_DIRNAME_ENV), RELEV_DIR_NAME);
    }

    /**
     * Deletes this relevance summary file and its corresponding
     * contributor file.
     *
     * @return {@code true} iff both the summary and contributor
     * files were successfully deleted.
     */
    public boolean delete() {
        boolean status1 = summaryFile.delete();
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
        return loadContrib().contains(contributorName(bulkFile));
    }

    private static String contributorName(BulkFile bulkFile) {
        // Always use the canonical path name of the contributors...
        return bulkFile.getCanonicalPath();
    }

    /**
     * Loads the records in this file into an indexed table.
     *
     * @return the records in this as an indexed table.
     */
    //public RelevanceSummaryTable load() {
    //return RelevanceSummaryTable.load(summaryFile);
    //}

    /**
     * Loads the names of the bulk files that have contributed to
     * the contents of this summary file.
     *
     * @return a set containing the names of the contributing bulk
     * files.
     */
    public Set<String> loadContrib() {
        if (contribFile.exists())
            return new LinkedHashSet<String>(IOUtil.readLines(contribFile));
        else
            return Set.of();
    }

    private void process(BulkFile bulkFile,
                         JournalTable journalTable,
                         PubDateTable pubDateTable,
                         ArticleDOITable articleDOITable,
                         ArticleTitleTable articleTitleTable,
                         RelevanceScoreTable relevanceScoreTable) {
        if (isContributor(bulkFile))
            return;

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

        addSummaryRecords(summaryRecords);
        addContributor(bulkFile);
    }

    private void addSummaryRecords(List<RelevanceSummaryRecord> summaryRecords) {
        long fileLen = summaryFile.length();

        try (PrintWriter writer = IOUtil.openWriter(summaryFile, true)) {
            if (fileLen < 1)
                writer.println(RelevanceSummaryRecord.header());

            for (RelevanceSummaryRecord summaryRecord : summaryRecords)
                writer.println(summaryRecord.format());
        }
    }

    private void addContributor(BulkFile bulkFile) {
        IOUtil.writeLines(contribFile, true, contributorName(bulkFile));
    }
}
