
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
    private HeadingDescFile     headingDescFile     = null;
    private JournalFile         journalFile         = null;
    private KeywordFile         keywordFile         = null;
    private TitleLemmaFile      titleLemmaFile      = null;

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
     * @return a list containing all bulk files in the specified
     * directory.
     */
    public static List<BulkFile> list(String dirName) {
        return list(new File(dirName));
    }

    /**
     * Lists all bulk data files in a given directory.
     *
     * @param directory the bulk file directory to list.
     *
     * @return a list containing all bulk files in the specified
     * directory.
     */
    public static List<BulkFile> list(File directory) {
        File[] bulkFileArray = directory.listFiles(FILE_FILTER);
        Arrays.sort(bulkFileArray);

        return ListUtil.apply(Arrays.asList(bulkFileArray), file -> create(file));
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
     * Returns the heading descriptor flat file derived from this bulk
     * file.
     *
     * @return the heading descriptor flat file derived from this bulk
     * file.
     */
    public synchronized HeadingDescFile getHeadingDescFile() {
        if (headingDescFile == null)
            headingDescFile = HeadingDescFile.from(this);

        return headingDescFile;
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
                       getHeadingDescFile(),
                       getJournalFile(),
                       getKeywordFile(),
                       getTitleLemmaFile());
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

    /**
     * Executes the production process for this bulk XML file.
     *
     * <p>This method parses the XML document and then generates all
     * content and analysis files.
     *
     * @throws RuntimeException if any errors occur.
     */
    public void process() {
        JamLogger.info("Processing [%s]...", file.getPath());

        processContentFiles();
    }

    private void processContentFiles() {
        List<DocumentContentFile> unprocessed =
            getUnprocessedContentFiles();

        if (unprocessed.isEmpty()) {
            JamLogger.info("All content files have been processed.");
            return;
        }

        PubmedXmlDocument document = getDocument();

        for (DocumentContentFile contentFile : unprocessed)
            contentFile.processDocument(document, false);
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
