
package pubmed.sql;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import jam.app.JamLogger;
import jam.lang.JamException;
import jam.util.ListUtil;

import pubmed.article.PMID;
import pubmed.article.PubmedArticle;
import pubmed.article.PubmedXmlDocument;

/**
 * Identifies all {@code PubMed} bulk data files in a given directory,
 * populates the corresponding database tables, and marks the tables
 * as processed in the {@code process_history} table.
 */
public abstract class BulkFileProcessor {
    private List<File> bulkFileList;

    private File bulkFile;
    private List<BulkFileTarget> activeTargets;

    private PubmedXmlDocument document;

    /**
     * Creates a new bulk processor.
     */
    protected BulkFileProcessor() {
    }

    /**
     * A predicate that matches {@code PubMed} bulk data files.
     */
    public static final Predicate<String> BULK_FILE_PREDICATE =
        Pattern.compile("^pubmed20n[0-9]{4}\\.xml\\.gz$").asMatchPredicate();

    /**
     * A filter that selects {@code PubMed} bulk data files.
     */
    public static final FileFilter BULK_FILE_FILTER =
        new FileFilter() {
            @Override public boolean accept(File file) {
                return BULK_FILE_PREDICATE.test(file.getName());
            }
        };

    /**
     * Returns the tables that must be populated, in the order that
     * they should be populated.
     *
     * @return the tables that must be populated, in the order that
     * they should be populated.
     */
    public abstract List<BulkFileTarget> getTargetTables();

    /**
     * Processes the bulk files in a given directory.
     *
     * @param directory the bulk file directory to process.
     *
     * @throws RuntimeException unless the specified file is a
     * directory and all files are processed successfully.
     */
    public void process(File directory) {
        validateDirectory(directory);
        listBulkFiles(directory);

        for (int fileIndex = 0; fileIndex < bulkFileList.size(); ++fileIndex)
            processFile(fileIndex);

	closeTargets();
        JamLogger.info("DONE!");
    }

    private void validateDirectory(File directory) {
        if (!directory.isDirectory())
            throw JamException.runtime("File [%s] is not a directory.", directory);
    }

    private void listBulkFiles(File directory) {
        File[] bulkFileArray = directory.listFiles(BULK_FILE_FILTER);
        Arrays.sort(bulkFileArray);
        bulkFileList = Arrays.asList(bulkFileArray);
    }

    private void processFile(int fileIndex) {
        bulkFile = bulkFileList.get(fileIndex);

	JamLogger.info("************************************************************************");
        JamLogger.info("Processing file [%d] of [%d]...", fileIndex + 1, bulkFileList.size());
        JamLogger.info(bulkFile);
	JamLogger.info("************************************************************************");

        findActiveTargets();

        if (activeTargets.isEmpty()) {
            JamLogger.info("Already processed all tables for [%s]...", bulkFile);
            return;
        }
        
        document = PubmedXmlDocument.parse(bulkFile);

        for (BulkFileTarget target : activeTargets)
            processTable(target);
    }

    private void findActiveTargets() {
        Set<String> processedTargets =
            ProcessHistoryTable.instance().fetchProcessedTables(bulkFile);

        activeTargets =
            ListUtil.filter(getTargetTables(), target -> !processedTargets.contains(target.getTableName()));
    }

    private void processTable(BulkFileTarget target) {
        JamLogger.info("Processing table [%s]...", target.getTableName());

        processDeletions(target);
        processLatest(target);

        ProcessHistoryTable.instance().markAsProcessed(bulkFile, target.getTableName());
    }

    private void processDeletions(BulkFileTarget<?> target) {
        List<PMID> deleted = document.viewDeleted();

        if (!target.deleteCitations(deleted))
            throw JamException.runtime("Failed to delete articles from [%s].", target.getTableName());
    }

    private void processLatest(BulkFileTarget<?> target) {
        List<PubmedArticle> latest = document.viewLatest();

        if (!target.processLatest(latest))
            throw JamException.runtime("Article processing failed in [%s].", target.getTableName());
    }

    private void closeTargets() {
	for (BulkFileTarget target : getTargetTables())
	    target.close();
    }
}
