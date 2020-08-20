
package pubmed.relev;

import java.io.File;
import java.io.PrintWriter;
import java.util.Collection;

import jam.app.JamEnv;
import jam.app.JamLogger;
import jam.app.JamProperties;
import jam.io.FileUtil;
import jam.io.IOUtil;
import jam.io.UniqueFile;

import pubmed.article.PMID;
import pubmed.bulk.BulkFile;

/**
 * Provides a base class to generate and store article-subject
 * relevance summary records.
 */
public abstract class RelevanceSummaryFileBase extends UniqueFile {
    /**
     * Creates a new relevance summary file with a fixed physical
     * file.
     *
     * @param file the file contining the summary records.
     */
    protected RelevanceSummaryFileBase(File file) {
        super(file);
    }

    /**
     * Name of the subdirectory (under the PubMed parent) that
     * contains the root of the relevance summary file tree.
     */
    public static final String RELEV_DIR_NAME = "relev";

    /**
     * Name of the system property that specifies the full path to the
     * root directory of the relevance file tree.  If this property is
     * not set, the directory will resolve to $PUBMED_LOCAL_DIR/relev.
     */
    public static final String RELEV_DIR_PROPERTY = "pubmed.relev.dirName";

    /**
     * Resolves the root directory of the relevance file tree.
     *
     * @return the root directory of the relevance file tree.
     */
    public static File resolveRelevanceDir() {
        File relevanceDir;

        if (JamProperties.isSet(RELEV_DIR_PROPERTY))
            relevanceDir = new File(JamProperties.getRequired(RELEV_DIR_PROPERTY));
        else
            relevanceDir = new File(JamEnv.getRequired(BulkFile.LOCAL_DIRNAME_ENV), RELEV_DIR_NAME);

        return FileUtil.getCanonicalFile(relevanceDir);
    }

    /**
     * Appends summary records to the summary file (writing the header
     * line when necessary).
     *
     * @param summaryRecords the summary records to append.
     *
     * @throws RuntimeException if any I/O errors occur.
     */
    protected synchronized void appendSummaryRecords(Collection<RelevanceSummaryRecord> summaryRecords) {
        long fileLen = file.length();

        try (PrintWriter writer = IOUtil.openWriter(file, true)) {
            if (fileLen < 1)
                writer.println(RelevanceSummaryRecord.header());

            for (RelevanceSummaryRecord summaryRecord : summaryRecords)
                writer.println(summaryRecord.format());
        }
    }

    /**
     * Deletes summary records from the summary file.
     *
     * @param deleted the identifiers for the records to delete.
     *
     * @throws RuntimeException if any I/O errors occur.
     */
    protected synchronized void deleteSummaryRecords(Collection<PMID> deleted) {
        //
        // Much time can be saved by checking for empty input...
        //
        if (deleted.isEmpty() || !file.exists())
            return;

        JamLogger.info("Loading existing summary records...");

        RelevanceSummaryTable table = load();
        int initSize = 0;

        // There may be thousands or millions of records in the file,
        // but only tens of deleted citations, so iterating over the
        // deleted citations is more efficient...
        JamLogger.info("Deleting citations...");

        for (PMID pmid : deleted)
            table.removeOuter(pmid);

        // If any records have been deleted, we just rewrite the
        // entire file...
        int deletedCount = initSize - table.size();

        JamLogger.info("Deleted [%d] citations.", deletedCount);

        if (deletedCount > 0)
            writeSummaryRecords(table.values());
    }

    /**
     * Writes the heading line and summary records to the summary file
     * (erasing the previous contents, if any).
     *
     * @param summaryRecords the summary records to write.
     *
     * @throws RuntimeException if any I/O errors occur.
     */
    protected synchronized void writeSummaryRecords(Collection<RelevanceSummaryRecord> summaryRecords) {
        JamLogger.info("Writing [%d] summary records to [%s]...", summaryRecords.size(), file);

        try (PrintWriter writer = IOUtil.openWriter(file, false)) {
            writer.println(RelevanceSummaryRecord.header());

            for (RelevanceSummaryRecord summaryRecord : summaryRecords)
                writer.println(summaryRecord.format());
        }
    }

    /**
     * Loads the records in this file into an indexed table.
     *
     * @return the records in this as an indexed table.
     */
    public RelevanceSummaryTable load() {
        return RelevanceSummaryTable.load(file);
    }
}
