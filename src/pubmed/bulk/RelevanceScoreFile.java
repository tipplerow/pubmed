
package pubmed.bulk;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import jam.app.JamLogger;
import jam.io.FileUtil;
import jam.io.IOUtil;
import jam.util.ListUtil;
import jam.util.StreamUtil;

import pubmed.article.PMID;
import pubmed.flat.ArticleAbstractRecord;
import pubmed.flat.ArticleAbstractTable;
import pubmed.flat.RelevanceScoreRecord;
import pubmed.flat.RelevanceScoreTable;
import pubmed.relev.AbstractRelevanceScorer;
import pubmed.relev.ChemicalRelevanceScorer;
import pubmed.relev.HeadingRelevanceScorer;
import pubmed.relev.KeywordRelevanceScorer;
import pubmed.relev.MeshTreeRelevanceScorer;
import pubmed.relev.TitleRelevanceScorer;
import pubmed.subject.Subject;

/**
 * Computes and stores relevance score records.
 */
public final class RelevanceScoreFile extends PubmedFlatFile<RelevanceScoreRecord> {
    //
    // A file containing keys of the subjects that have already been
    // processed, used to skip those subjects when new subjects are
    // added...
    //
    private final File tocFile;

    private final Set<PMID> pmidSet = new TreeSet<PMID>();

    private TitleRelevanceScorer    titleScorer;
    private AbstractRelevanceScorer abstractScorer;
    private MeshTreeRelevanceScorer meshTreeScorer;
    private HeadingRelevanceScorer  headingListScorer;
    private KeywordRelevanceScorer  keywordListScorer;
    private ChemicalRelevanceScorer chemicalListScorer;
    
    private RelevanceScoreFile(BulkFile bulkFile) {
        super(bulkFile);
        this.tocFile = resolveTOCFile();
    }

    private File resolveTOCFile() {
        String flatPrefix = FileUtil.getCanonicalPrefix(flatFile);
        String tocSuffix  = "_toc.txt";

        return new File(flatPrefix + tocSuffix);
    }

    /**
     * The suffix for flat file names.
     */
    public static final String SUFFIX = "relevance";

    /**
     * Creates a new relevance score file for records derived from
     * a given bulk XML file.
     *
     * @param bulkFile the bulk XML file containing articles to be
     * scored.
     *
     * @return the relevance score file for the given bulk file.
     */
    public static RelevanceScoreFile instance(BulkFile bulkFile) {
        return new RelevanceScoreFile(bulkFile);
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
    public synchronized void process(Collection<Subject> subjects) {
        JamLogger.info("Processing [%s]...", flatFile);
        List<Subject> unprocessed = findUnprocessed(subjects);

        if (unprocessed.isEmpty()) {
            JamLogger.info("No new subjects to process.");
            return;
        }

        JamLogger.info("Gathering required data...");

        loadPMIDs();
        createScorers();

        List<RelevanceScoreRecord> fileRecords =
            new ArrayList<RelevanceScoreRecord>();

        JamLogger.info("Computing relevance scores for [%s] subjects...", unprocessed.size());

        for (Subject subject : unprocessed)
            fileRecords.addAll(process(subject));

        if (!fileRecords.isEmpty())
            writeRecords(fileRecords, true);

        updateTOC(unprocessed);
    }

    private void loadPMIDs() {
        //
        // Only process articles with abstracts...
        //
        ArticleAbstractTable abstractTable =
            bulkFile.getArticleAbstractFile().load();

        for (ArticleAbstractRecord abstractRecord : abstractTable)
            pmidSet.add(abstractRecord.getPMID());
    }

    private void createScorers() {
        if (titleScorer == null)
            titleScorer = TitleRelevanceScorer.instance(bulkFile);

        if (abstractScorer == null)
            abstractScorer = AbstractRelevanceScorer.instance(bulkFile);

        if (meshTreeScorer == null)
            meshTreeScorer = MeshTreeRelevanceScorer.instance(bulkFile);

        if (headingListScorer == null)
            headingListScorer = HeadingRelevanceScorer.instance(bulkFile);

        if (keywordListScorer == null)
            keywordListScorer = KeywordRelevanceScorer.instance(bulkFile);

        if (chemicalListScorer == null)
            chemicalListScorer = ChemicalRelevanceScorer.instance(bulkFile);
    }

    private List<RelevanceScoreRecord> process(Subject subject) {
        List<RelevanceScoreRecord> subjectRecords;

        subjectRecords = StreamUtil.applyParallel(pmidSet, pmid -> process(pmid, subject));
        subjectRecords = ListUtil.filter(subjectRecords, record -> record.filter());

        return subjectRecords;
    }

    private RelevanceScoreRecord process(PMID pmid, Subject subject) {
        try {
            return RelevanceScoreRecord.compute(pmid,
                                                subject,
                                                titleScorer,
                                                abstractScorer,
                                                meshTreeScorer,
                                                headingListScorer,
                                                keywordListScorer,
                                                chemicalListScorer);
        }
        catch (RuntimeException ex) {
            JamLogger.warn(ex);
            return RelevanceScoreRecord.zero(pmid, subject.getKey());
        }
    }

    private void updateTOC(Collection<Subject> subjects) {
        JamLogger.info("Updating table of contents [%s]...", tocFile);
        IOUtil.writeObjects(tocFile, true, subjects, subject -> subject.getKey());
    }

    @Override public boolean delete() {
        super.delete();
        return tocFile.delete();
    }

    @Override public String getSuffix() {
        return SUFFIX;
    }

    @Override public RelevanceScoreTable load() {
        return (RelevanceScoreTable) super.load();
    }

    @Override public RelevanceScoreTable newStore() {
        return new RelevanceScoreTable();
    }
}
