
package pubmed.relev;

import java.io.File;
import java.io.PrintWriter;
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

import pubmed.article.PMID;
import pubmed.bulk.BulkFile;
import pubmed.bulk.RelevanceScoreFile;
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
        return subject.getKey() + "_relevance_summary.psv";
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
     * Processes one bulk file: Loads all subject relevance scores
     * derived from the bulk file, aggregates the relevance scores
     * into summary records for each subject, and appends summary
     * records to the underlying physical files.
     *
     * @param bulkFile the bulk file to process.
     *
     * @param subjects the subjects to process.
     *
     * @throws RuntimeException if any errors occur.
     */
    public static synchronized void process(BulkFile bulkFile, Collection<Subject> subjects) {
        BulkProcessor.process(bulkFile, subjects);
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
    public RelevanceSummaryTable load() {
        return RelevanceSummaryTable.load(summaryFile);
    }

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

    private void addSummaryRecords(List<RelevanceSummaryRecord> summaryRecords) {
        try (PrintWriter writer = IOUtil.openWriter(summaryFile, true)) {
            for (RelevanceSummaryRecord summaryRecord : summaryRecords)
                writer.println(summaryRecord.format());
        }
    }

    private void addContributor(BulkFile bulkFile) {
        IOUtil.writeLines(contribFile, true, contributorName(bulkFile));
    }

    private static final class BulkProcessor {
        private final BulkFile bulkFile;
        private final Collection<Subject> subjects;

        private Set<PMID> pmidSet;

        private RelevanceScoreTable titleScoreTable;
        private RelevanceScoreTable abstractScoreTable;
        private RelevanceScoreTable meshTreeScoreTable;
        private RelevanceScoreTable headingListScoreTable;
        private RelevanceScoreTable keywordListScoreTable;
        private RelevanceScoreTable chemicalListScoreTable;

        private BulkProcessor(BulkFile bulkFile, Collection<Subject> subjects) {
            this.bulkFile = bulkFile;
            this.subjects = subjects;
        }

        private static void process(BulkFile bulkFile, Collection<Subject> subjects) {
            BulkProcessor processor = new BulkProcessor(bulkFile, subjects);
            processor.process();
        }

        private void process() {
            FileUtil.ensureDir(resolveRelevanceDir());

            pmidSet = bulkFile.getPMIDSet();

            // Process and load all relevance files in parallel...
            bulkFile.getRelevanceScoreFiles().parallelStream().forEach(scoreFile -> prepareScoreFile(scoreFile));

            titleScoreTable = getScoreTable(bulkFile.getTitleRelevanceFile());
            abstractScoreTable = getScoreTable(bulkFile.getAbstractRelevanceFile());
            meshTreeScoreTable = getScoreTable(bulkFile.getMeshTreeRelevanceFile());
            headingListScoreTable = getScoreTable(bulkFile.getHeadingRelevanceFile());
            keywordListScoreTable = getScoreTable(bulkFile.getKeywordRelevanceFile());
            chemicalListScoreTable = getScoreTable(bulkFile.getChemicalRelevanceFile());

            subjects.parallelStream().forEach(subject -> process(subject));
        }

        private void prepareScoreFile(RelevanceScoreFile scoreFile) {
            scoreFile.process(subjects);
            scoreFile.cache();
        }

        private RelevanceScoreTable getScoreTable(RelevanceScoreFile scoreFile) {
            if (scoreFile.exists())
                return scoreFile.cache();
            else
                return new RelevanceScoreTable();
        }

        private void process(Subject subject) {
            RelevanceSummaryFile summaryFile = instance(subject);

            if (summaryFile.isContributor(bulkFile))
                return;

            JamLogger.info("Generating relevance summary for subject [%s]...", subject.getKey());

            List<RelevanceSummaryRecord> summaryRecords =
                createSummaryRecords(subject);

            summaryFile.addSummaryRecords(summaryRecords);
            summaryFile.addContributor(bulkFile);
        }

        private List<RelevanceSummaryRecord> createSummaryRecords(Subject subject) {
            List<RelevanceSummaryRecord> summaryRecords =
                new ArrayList<RelevanceSummaryRecord>(pmidSet.size());

            for (PMID pmid : pmidSet) {
                RelevanceSummaryRecord summaryRecord = createSummaryRecord(pmid, subject);

                if (summaryRecord.filter())
                    summaryRecords.add(summaryRecord);
            }

            return summaryRecords;
        }

        private RelevanceSummaryRecord createSummaryRecord(PMID pmid, Subject subject) {
            int titleScore = titleScoreTable.getScore(pmid, subject);
            int abstractScore = abstractScoreTable.getScore(pmid, subject);
            int meshTreeScore = meshTreeScoreTable.getScore(pmid, subject);
            int headingListScore = headingListScoreTable.getScore(pmid, subject);
            int keywordListScore = keywordListScoreTable.getScore(pmid, subject);
            int chemicalListScore = chemicalListScoreTable.getScore(pmid, subject);

            return RelevanceSummaryRecord.create(pmid,
                                                 subject.getKey(),
                                                 titleScore,
                                                 abstractScore,
                                                 meshTreeScore,
                                                 headingListScore,
                                                 keywordListScore,
                                                 chemicalListScore);
        }
    }
}
