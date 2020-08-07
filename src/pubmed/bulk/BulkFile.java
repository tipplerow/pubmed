
package pubmed.bulk;

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.NavigableSet;
import java.util.TreeSet;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import jam.app.JamLogger;
import jam.io.FileUtil;
import jam.lang.JamException;
import jam.util.ListUtil;

import pubmed.article.PMID;
import pubmed.xml.PubmedArticleElement;
import pubmed.xml.PubmedXmlDocument;

/**
 * Represents a bulk XML file in the {@code PubMed} production system.
 */
public final class BulkFile {
    private final File file;

    private PubmedXmlDocument document = null;

    private AbstractLemmaFile   abstractLemmaFile   = null;
    private ArticleAbstractFile articleAbstractFile = null;
    private ArticleTitleFile    articleTitleFile    = null;
    private ChemicalFile        chemicalFile        = null;
    private HeadingFile         headingFile         = null;
    private JournalFile         journalFile         = null;
    private KeywordFile         keywordFile         = null;
    private TitleLemmaFile      titleLemmaFile      = null;

    private AbstractRelevanceFile abstractRelevanceFile = null;
    private ChemicalRelevanceFile chemicalRelevanceFile = null;
    private HeadingRelevanceFile  headingRelevanceFile  = null;
    private KeywordRelevanceFile  keywordRelevanceFile  = null;
    private MeshTreeRelevanceFile meshTreeRelevanceFile = null;
    private TitleRelevanceFile    titleRelevanceFile    = null;

    private NavigableSet<PMID> pmidSet = null;

    private BulkFile(File file) {
        this.file = FileUtil.getCanonicalFile(file);
    }

    /**
     * A predicate that matches {@code PubMed} bulk XML data files.
     */
    public static final Predicate<String> FILE_PREDICATE =
        Pattern.compile("^pubmed20n[0-9]{4}\\.xml\\.gz$").asMatchPredicate();

    /**
     * A filter that selects {@code PubMed} bulk XML data files.
     */
    public static final FileFilter FILE_FILTER =
        new FileFilter() {
            @Override public boolean accept(File file) {
                return FILE_PREDICATE.test(file.getName());
            }
        };

    /**
     * Creates a new bulk file.
     *
     * @param fileName the path to the physical file.
     *
     * @return the new bulk file for the specified path.
     */
    public static BulkFile create(String fileName) {
        return create(new File(fileName));
    }

    /**
     * Creates a new bulk file.
     *
     * @param file the path to the physical file.
     *
     * @return the new bulk file for the specified path.
     */
    public static BulkFile create(File file) {
        return new BulkFile(file);
    }

    /**
     * Lists all bulk data files in a given directory.
     *
     * @param dirName the bulk file directory to list.
     *
     * @return an array containing all bulk files in the specified
     * directory.
     */
    public static BulkFile[] list(String dirName) {
        return list(new File(dirName));
    }

    /**
     * Lists all bulk data files in a given directory.
     *
     * @param directory the bulk file directory to list.
     *
     * @return an array containing all bulk files in the specified
     * directory.
     */
    public static BulkFile[] list(File directory) {
        validateDirectory(directory);

        File[] fileArray = directory.listFiles(FILE_FILTER);
        BulkFile[] bulkFileArray = new BulkFile[fileArray.length];

        Arrays.sort(fileArray);

        for (int index = 0; index < fileArray.length; ++index)
            bulkFileArray[index] = create(fileArray[index]);

        return bulkFileArray;
    }

    private static void validateDirectory(File directory) {
        if (!directory.isDirectory())
            throw JamException.runtime("File [%s] is not a directory.", directory);
    }

    /**
     * Returns the basename of the underlying file.
     *
     * @return the basename of the underlying file.
     */
    public String getBaseName() {
        return FileUtil.getBaseName(file);
    }

    /**
     * Returns the prefix of the underlying file name with the
     * absolute path information retained.
     *
     * @return the prefix of the underlying file name with the
     * absolute path information retained.
     */
    public String getBaseNamePrefix() {
        return FileUtil.getBaseNamePrefix(file);
    }

    /**
     * Returns the prefix of the underlying file name with the
     * absolute path information retained.
     *
     * @return the prefix of the underlying file name with the
     * absolute path information retained.
     */
    public String getCanonicalPrefix() {
        return FileUtil.getCanonicalPrefix(file);
    }

    /**
     * Returns the name of the directory containing the underlying
     * file.
     *
     * @return the name of the directory containing the underlying
     * file.
     */
    public String getDirName() {
        return FileUtil.getDirName(file);
    }

    /**
     * Returns the underlying physical file.
     *
     * @return the underlying physical file.
     */
    public File getFile() {
        return file;
    }

    /**
     * Returns the parsed XML document contained in this bulk file.
     *
     * @return the parsed XML document contained in this bulk file.
     */
    public synchronized PubmedXmlDocument getDocument() {
        if (document == null)
            document = PubmedXmlDocument.parse(file);

        return document;
    }

    /**
     * Returns the lemmatized abstract flat file derived from this
     * bulk file.
     *
     * @return the lemmatized abstract flat file derived from this
     * bulk file.
     */
    public synchronized AbstractLemmaFile getAbstractLemmaFile() {
        if (abstractLemmaFile == null)
            abstractLemmaFile = AbstractLemmaFile.from(this);

        return abstractLemmaFile;
    }

    /**
     * Returns the abstract relevance score file derived from this
     * bulk file.
     *
     * @return the abstract relevance score file derived from this
     * bulk file.
     */
    public synchronized AbstractRelevanceFile getAbstractRelevanceFile() {
        if (abstractRelevanceFile == null)
            abstractRelevanceFile = AbstractRelevanceFile.from(this);

        return abstractRelevanceFile;
    }

    /**
     * Returns the article abstract flat file derived from this bulk
     * file.
     *
     * @return the article abstract flat file derived from this bulk
     * file.
     */
    public synchronized ArticleAbstractFile getArticleAbstractFile() {
        if (articleAbstractFile == null)
            articleAbstractFile = ArticleAbstractFile.from(this);

        return articleAbstractFile;
    }

    /**
     * Returns the article title flat file derived from this bulk
     * file.
     *
     * @return the article title flat file derived from this bulk
     * file.
     */
    public synchronized ArticleTitleFile getArticleTitleFile() {
        if (articleTitleFile == null)
            articleTitleFile = ArticleTitleFile.from(this);

        return articleTitleFile;
    }

    /**
     * Returns the chemical substance flat file derived from this bulk
     * file.
     *
     * @return the chemical substance flat file derived from this bulk
     * file.
     */
    public synchronized ChemicalFile getChemicalFile() {
        if (chemicalFile == null)
            chemicalFile = ChemicalFile.from(this);

        return chemicalFile;
    }

    /**
     * Returns the chemical substance relevance score file derived
     * from this bulk file.
     *
     * @return the chemical substance relevance score file derived
     * from this bulk file.
     */
    public synchronized ChemicalRelevanceFile getChemicalRelevanceFile() {
        if (chemicalRelevanceFile == null)
            chemicalRelevanceFile = ChemicalRelevanceFile.from(this);

        return chemicalRelevanceFile;
    }

    /**
     * Returns the heading descriptor flat file derived from this bulk
     * file.
     *
     * @return the heading descriptor flat file derived from this bulk
     * file.
     */
    public synchronized HeadingFile getHeadingFile() {
        if (headingFile == null)
            headingFile = HeadingFile.from(this);

        return headingFile;
    }

    /**
     * Returns the heading descriptor relevance score file derived
     * from this bulk file.
     *
     * @return the heading descriptor relevance score file derived
     * from this bulk file.
     */
    public synchronized HeadingRelevanceFile getHeadingRelevanceFile() {
        if (headingRelevanceFile == null)
            headingRelevanceFile = HeadingRelevanceFile.from(this);

        return headingRelevanceFile;
    }

    /**
     * Returns the journal flat file derived from this bulk file.
     *
     * @return the journal flat file derived from this bulk file.
     */
    public synchronized JournalFile getJournalFile() {
        if (journalFile == null)
            journalFile = JournalFile.from(this);

        return journalFile;
    }

    /**
     * Returns the keyword flat file derived from this bulk file.
     *
     * @return the keyword flat file derived from this bulk file.
     */
    public synchronized KeywordFile getKeywordFile() {
        if (keywordFile == null)
            keywordFile = KeywordFile.from(this);

        return keywordFile;
    }

    /**
     * Returns the keyword relevance score file derived from this bulk
     * file.
     *
     * @return the keyword relevance score file derived from this bulk
     * file.
     */
    public synchronized KeywordRelevanceFile getKeywordRelevanceFile() {
        if (keywordRelevanceFile == null)
            keywordRelevanceFile = KeywordRelevanceFile.from(this);

        return keywordRelevanceFile;
    }

    /**
     * Returns the {@code MeSH} tree relevance score file derived from
     * this bulk file.
     *
     * @return the {@code MeSH} tree relevance score file derived from
     * this bulk file.
     */
    public synchronized MeshTreeRelevanceFile getMeshTreeRelevanceFile() {
        if (meshTreeRelevanceFile == null)
            meshTreeRelevanceFile = MeshTreeRelevanceFile.from(this);

        return meshTreeRelevanceFile;
    }

    /**
     * Returns the lemmatized title flat file derived from this bulk
     * file.
     *
     * @return the lemmatized title flat file derived from this bulk
     * file.
     */
    public synchronized TitleLemmaFile getTitleLemmaFile() {
        if (titleLemmaFile == null)
            titleLemmaFile = TitleLemmaFile.from(this);

        return titleLemmaFile;
    }

    /**
     * Returns the title relevance score file derived from this bulk
     * file.
     *
     * @return the title relevance score file derived from this bulk
     * file.
     */
    public synchronized TitleRelevanceFile getTitleRelevanceFile() {
        if (titleRelevanceFile == null)
            titleRelevanceFile = TitleRelevanceFile.from(this);

        return titleRelevanceFile;
    }

    /**
     * Returns a read-only list of the document content flat files
     * derived from this bulk file.
     *
     * @return a read-only list of the document content flat files
     * derived from this bulk file.
     */
    public List<DocumentContentFile> getContentFiles() {
        return List.of(getAbstractLemmaFile(),
                       getArticleAbstractFile(),
                       getArticleTitleFile(),
                       getChemicalFile(),
                       getHeadingFile(),
                       getJournalFile(),
                       getKeywordFile(),
                       getTitleLemmaFile());
    }

    /**
     * Returns a read-only list of the relevance score flat files
     * derived from this bulk file.
     *
     * @return a read-only list of the relevance score flat files
     * derived from this bulk file.
     */
    public List<RelevanceScoreFile> getRelevanceScoreFiles() {
        return List.of(getAbstractRelevanceFile(),
                       getChemicalRelevanceFile(),
                       getHeadingRelevanceFile(),
                       getKeywordRelevanceFile(),
                       getMeshTreeRelevanceFile(),
                       getTitleRelevanceFile());
    }

    /**
     * Returns a read-only list of the document content flat files
     * that have not yet been generated from this bulk file.
     *
     * @return a read-only list of the document content flat files
     * that have not yet been generated from this bulk file.
     */
    public List<DocumentContentFile> getUnprocessedContentFiles() {
        return ListUtil.filter(getContentFiles(), file -> !file.exists());
    }

    /**
     * Returns a read-only list of the document content flat files
     * that have not yet been generated from this bulk file.
     *
     * @return a read-only list of the document content flat files
     * that have not yet been generated from this bulk file.
     */
    public List<RelevanceScoreFile> getUnprocessedRelevanceScoreFiles() {
        return ListUtil.filter(getRelevanceScoreFiles(), file -> !file.exists());
    }

    /**
     * Returns an immutable navigable set containing all article
     * identifiers contained in this bulk file.
     *
     * @return an immutable navigable set containing all article
     * identifiers contained in this bulk file.
     */
    public synchronized NavigableSet<PMID> getPMIDSet() {
        if (pmidSet == null)
            createPMIDSet();

        return pmidSet;
    }

    private void createPMIDSet() {
        PubmedXmlDocument document = getDocument();
        List<PubmedArticleElement> elements = document.getPubmedArticleElements();

        pmidSet = new TreeSet<PMID>(ListUtil.apply(elements, element -> element.getPMID()));
        pmidSet = Collections.unmodifiableNavigableSet(pmidSet);
    }

    @Override public boolean equals(Object obj) {
        return (obj instanceof BulkFile) && equalsFile((BulkFile) obj);
    }

    private boolean equalsFile(BulkFile that) {
        return this.file.equals(that.file);
    }

    @Override public int hashCode() {
        return file.hashCode();
    }

    @Override public String toString() {
        return String.format("BulkFile(%s)", file);
    }
}
