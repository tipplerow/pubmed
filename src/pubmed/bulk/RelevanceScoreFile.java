
package pubmed.bulk;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import jam.app.JamLogger;
import jam.io.FileUtil;
import jam.io.IOUtil;
import jam.util.ListUtil;
import jam.util.StreamUtil;

import pubmed.article.PMID;
import pubmed.flat.RelevanceScoreRecord;
import pubmed.flat.RelevanceScoreTable;
import pubmed.subject.Subject;

/**
 * Provides a base class for flat files that contain relevance score
 * records.
 */
public abstract class RelevanceScoreFile extends PubmedFlatFile<RelevanceScoreRecord> {
    //
    // A file containing keys of the subjects that have already been
    // processed, used to skip those subjects when new subjects are
    // added...
    //
    private final File tocFile;

    /**
     * Creates a new relevance score file for records derived from
     * a given bulk XML file.
     *
     * @param bulkFile the bulk XML file containing articles to be
     * scored.
     */
    protected RelevanceScoreFile(BulkFile bulkFile) {
        super(bulkFile);
        this.tocFile = resolveTOCFile();
    }

    private File resolveTOCFile() {
        String flatPrefix = FileUtil.getCanonicalPrefix(flatFile);
        String tocSuffix  = "_toc.txt";

        return new File(flatPrefix + tocSuffix);
    }

    /**
     * Computes the relevance score for an article and subject.
     *
     * @param pmid the identifier of the article to score.
     *
     * @param subject the subject of interest.
     *
     * @return the relevance score for the specified article and
     * subject.
     */
    public abstract int computeScore(PMID pmid, Subject subject);

    /**
     * Creates the score record for an article and subject.
     *
     * @param pmid the identifier of the article to score.
     *
     * @param subject the subject of interest.
     *
     * @return the score record for the specified article and subject.
     */
    public RelevanceScoreRecord createRecord(PMID pmid, Subject subject) {
        return RelevanceScoreRecord.create(pmid, subject, computeScore(pmid, subject));
    }

    /**
     * Determines whether to store a computed relevance record in the
     * physical flat file.  This default implementation retains only
     * records with non-zero scores.
     *
     * @param record the record to filter.
     *
     * @return {@code true} iff the record should be stored in the
     * physical flat file.
     */
    public boolean filterRecord(RelevanceScoreRecord record) {
        return record != null && record.getScore() != 0;
    }

    /**
     * Identifies subjects that have neven been processed by this
     * relevance file.
     *
     * @param subjects the subjects of interest.
     *
     * @return a collection containing the subjects that have never
     * been processed by this relevance file.
     */
    public List<Subject> findUnprocessed(Collection<Subject> subjects) {
        Set<String> processedKeys = loadTOC();
        List<Subject> unprocessed = new ArrayList<Subject>();

        for (Subject subject : subjects)
            if (!processedKeys.contains(subject.getKey()))
                unprocessed.add(subject);

        return unprocessed;
    }

    /**
     * Loads the table of contents: A set containing the keys of all
     * previously subjects.
     *
     * @return a set containing the keys of all previously subjects.
     */
    public Set<String> loadTOC() {
        if (tocFile.exists())
            return new LinkedHashSet<String>(IOUtil.readLines(tocFile));
        else
            return Set.of();
    }

    /**
     * Computes relevance scores for all articles in the bulk XML
     * file with respect to a collection of subjects and appends
     * the relevance records to the physical flat file.
     *
     * @param subjects the subjects of interest.
     */
    public void process(Collection<Subject> subjects) {
        List<Subject> unprocessed = findUnprocessed(subjects);
        List<RelevanceScoreRecord> fileRecords = new ArrayList<RelevanceScoreRecord>();

        if (unprocessed.isEmpty()) {
            JamLogger.info("No new subjects to process for [%s].", bulkFile);
            return;
        }

        for (Subject subject : unprocessed)
            fileRecords.addAll(process(subject));

        if (!fileRecords.isEmpty())
            writeRecords(fileRecords, true);

        updateTOC(unprocessed);
    }

    private List<RelevanceScoreRecord> process(Subject subject) {
        List<RelevanceScoreRecord> subjectRecords;

        subjectRecords = StreamUtil.applyParallel(bulkFile.getPMIDSet(), pmid -> createRecord(pmid, subject));
        subjectRecords = ListUtil.filter(subjectRecords, record -> filterRecord(record));

        return subjectRecords;
    }

    private void updateTOC(Collection<Subject> subjects) {
        JamLogger.info("Updating table of contents [%s]...", tocFile);
        IOUtil.writeObjects(tocFile, true, subjects, subject -> subject.getKey());
    }

    @Override public boolean delete() {
        super.delete();
        return tocFile.delete();
    }

    @Override public RelevanceScoreTable load() {
        return (RelevanceScoreTable) super.load();
    }

    @Override public RelevanceScoreTable newStore() {
        return new RelevanceScoreTable();
    }
}
