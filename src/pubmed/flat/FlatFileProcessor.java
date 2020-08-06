
package pubmed.flat;

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import jam.app.JamLogger;
import jam.lang.JamException;

import pubmed.xml.PubmedXmlDocument;

/**
 * Identifies all {@code PubMed} bulk XML files in a given directory,
 * parses them, and writes flat files containing the article component
 * records.
 */
public final class FlatFileProcessor {
    private final File bulkDirectory;
    private final List<File> bulkFileList;

    private FlatFileProcessor(File bulkDirectory) {
        this.bulkDirectory = bulkDirectory;
        this.bulkFileList  = listBulkFiles();
    }

    private List<File> listBulkFiles() {
        if (!bulkDirectory.isDirectory())
            throw JamException.runtime("File [%s] is not a directory.", bulkDirectory);

        File[] bulkFileArray = bulkDirectory.listFiles(BULK_XML_FILE_FILTER);
        Arrays.sort(bulkFileArray);

        return Arrays.asList(bulkFileArray);
    }

    /**
     * A predicate that matches {@code PubMed} bulk XML data files.
     */
    public static final Predicate<String> BULK_XML_FILE_PREDICATE =
        Pattern.compile("^pubmed20n[0-9]{4}\\.xml\\.gz$").asMatchPredicate();

    /**
     * A filter that selects {@code PubMed} bulk XML data files.
     */
    public static final FileFilter BULK_XML_FILE_FILTER =
        new FileFilter() {
            @Override public boolean accept(File file) {
                return BULK_XML_FILE_PREDICATE.test(file.getName());
            }
        };

    /**
     * Processes the bulk data files in a given directory.
     *
     * @param directory the bulk file directory to process.
     *
     * @throws RuntimeException unless the specified file is a
     * directory and all files are processed successfully.
     */
    public static void processDirectory(String directory) {
        processDirectory(new File(directory));
    }

    /**
     * Processes the bulk data files in a given directory.
     *
     * @param directory the bulk file directory to process.
     *
     * @throws RuntimeException unless the specified file is a
     * directory and all files are processed successfully.
     */
    public static void processDirectory(File directory) {
        FlatFileProcessor processor = new FlatFileProcessor(directory);
        processor.processDirectory();
    }

    private void processDirectory() {
        for (int fileIndex = 0; fileIndex < bulkFileList.size(); ++fileIndex)
            processFile(fileIndex);

        JamLogger.info("DONE!");
    }

    private void processFile(int fileIndex) {
        File bulkFile = bulkFileList.get(fileIndex);

	JamLogger.info("************************************************************************");
        JamLogger.info("Processing file [%d] of [%d]...", fileIndex + 1, bulkFileList.size());
        JamLogger.info(bulkFile);
	JamLogger.info("************************************************************************");

        processFile(bulkFile);
    }

    private void processFile(File bulkFile) {
        PubmedXmlDocument document =
            PubmedXmlDocument.parse(bulkFile);

        ArticleTitleFile.from(bulkFile).processDocument(document, false);
        ArticleAbstractFile.from(bulkFile).processDocument(document, false);
        ChemicalFile.from(bulkFile).processDocument(document, false);
        HeadingFile.from(bulkFile).processDocument(document, false);
        JournalFile.from(bulkFile).processDocument(document, false);
        KeywordFile.from(bulkFile).processDocument(document, false);

        TitleLemmaFile.from(bulkFile).processDocument(document, false);
        AbstractLemmaFile.from(bulkFile).processDocument(document, false);
    }

    public static void main(String[] args) {
        for (String directory : args)
            processDirectory(directory);
    }
}
