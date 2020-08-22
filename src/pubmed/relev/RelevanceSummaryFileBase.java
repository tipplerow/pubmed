
package pubmed.relev;

import java.io.File;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Set;

import jam.app.JamEnv;
import jam.app.JamLogger;
import jam.app.JamProperties;
import jam.io.FileUtil;
import jam.io.IOUtil;
import jam.io.UniqueFile;

import pubmed.article.PMID;
import pubmed.bulk.BulkFile;
import pubmed.delcit.DeleteCitationFile;

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
    public synchronized void appendSummaryRecords(Collection<RelevanceSummaryRecord> summaryRecords) {
        long fileLen = file.length();

        try (PrintWriter writer = IOUtil.openWriter(file, true)) {
            if (fileLen < 1)
                writer.println(RelevanceSummaryRecord.header());

            for (RelevanceSummaryRecord summaryRecord : summaryRecords)
                writer.println(summaryRecord.format());
        }
    }

    /**
     * Removes deleted citations from the summary file.
     *
     * @throws RuntimeException if any I/O errors occur.
     */
    public synchronized void removeDeletedCitations() {
        if (!file.exists())
            return;

        Set<PMID> deletedPMID = DeleteCitationFile.instance().viewDeleted();

        if (deletedPMID.isEmpty())
            return;

        JamLogger.info("Loading existing summary records...");
        RelevanceSummaryTable table = load();

        JamLogger.info("Deleting citations...");
        Collection<RelevanceSummaryRecord> deletedRec = table.removeOuter(deletedPMID);

        // If any records have been deleted, we just rewrite the
        // entire file.  Not particularly elegant, but effective...
        JamLogger.info("Deleted [%d] citations.", deletedRec.size());

        if (!deletedRec.isEmpty())
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
    public synchronized void writeSummaryRecords(Collection<RelevanceSummaryRecord> summaryRecords) {
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
