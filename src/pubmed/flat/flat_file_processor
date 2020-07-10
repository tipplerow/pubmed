
package pubmed.bulk;

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import jam.app.JamLogger;
import jam.io.FileUtil;
import jam.lang.JamException;

/**
 * Identifies all {@code PubMed} bulk XML files in a given directory,
 * parses them, and writes flat files containing the article component
 * records.
 */
public final class FlatFileProcessor {
    private final File bulkDirectory;
    private List<File> bulkFileList;

    /**
     * Creates a new bulk file processor.
     */
    protected BulkFileProcessor() {
    }

    /**
     * A predicate that matches {@code PubMed} bulk XML data files.
     */
    public static final Predicate<String> BULK_XML_FILE_PREDICATE =
        Pattern.compile("^pubmed20n[0-9]{4}\\.xml\\.gz$").asMatchPredicate();

    /**
     * A predicate that matches title lemma files.
     */
    public static final Predicate<String> TITLE_LEMMA_FILE_PREDICATE =
        Pattern.compile("^pubmed20n[0-9]{4}_title_lemma\\.psv\\.gz$").asMatchPredicate();

    /**
     * A predicate that matches abstract lemma files.
     */
    public static final Predicate<String> ABSTRACT_LEMMA_FILE_PREDICATE =
        Pattern.compile("^pubmed20n[0-9]{4}_abstract_lemma\\.psv\\.gz$").asMatchPredicate();

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
     * A filter that selects title lemma files.
     */
    public static final FileFilter TITLE_LEMMA_FILE_FILTER =
        new FileFilter() {
            @Override public boolean accept(File file) {
                return TITLE_LEMMA_FILE_PREDICATE.test(file.getName());
            }
        };

    /**
     * A filter that selects abstract lemma files.
     */
    public static final FileFilter ABSTRACT_LEMMA_FILE_FILTER =
        new FileFilter() {
            @Override public boolean accept(File file) {
                return ABSTRACT_LEMMA_FILE_PREDICATE.test(file.getName());
            }
        };

    /**
     * Returns the abstract lemma file corresponding to a given bulk XML
     * data file.
     *
     * @param bulkFile a bulk XML data file.
     *
     * @return the abstract lemma file corresponding to the specified
     * bulk XML data file.
     */
    public static File getAbstractLemmaFile(File bulkFile) {
        return new File(FileUtil.getCanonicalPrefix(bulkFile) + "_abstract_lemma.psv.gz");
    }

    /**
     * Returns the title lemma file corresponding to a given bulk XML
     * data file.
     *
     * @param bulkFile a bulk XML data file.
     *
     * @return the title lemma file corresponding to the specified
     * bulk XML data file.
     */
    public static File getTitleLemmaFile(File bulkFile) {
        return new File(FileUtil.getCanonicalPrefix(bulkFile) + "_title_lemma.psv.gz");
    }

    /**
     * Processes a bulk data file.
     *
     * @param file the bulk file to process.
     *
     * @throws RuntimeException unless the specified file is
     * processed successfully.
     */
    public abstract void processFile(File file);

    /**
     * Processes the bulk data files in a given directory.
     *
     * @param directory the bulk file directory to process.
     *
     * @throws RuntimeException unless the specified file is a
     * directory and all files are processed successfully.
     */
    public void processDirectory(String directory) {
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
    public void processDirectory(File directory) {
        validateDirectory(directory);
        listBulkFiles(directory);

        for (int fileIndex = 0; fileIndex < bulkFileList.size(); ++fileIndex)
            processFile(fileIndex);

        JamLogger.info("DONE!");
    }

    private void validateDirectory(File directory) {
        if (!directory.isDirectory())
            throw JamException.runtime("File [%s] is not a directory.", directory);
    }

    private void listBulkFiles(File directory) {
        File[] bulkFileArray = directory.listFiles(BULK_XML_FILE_FILTER);
        Arrays.sort(bulkFileArray);
        bulkFileList = Arrays.asList(bulkFileArray);
    }

    private void processFile(int fileIndex) {
        File bulkFile = bulkFileList.get(fileIndex);

	JamLogger.info("************************************************************************");
        JamLogger.info("Processing file [%d] of [%d]...", fileIndex + 1, bulkFileList.size());
        JamLogger.info(bulkFile);
	JamLogger.info("************************************************************************");

        processFile(bulkFile);
    }
}
