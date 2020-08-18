
package pubmed.relev;

import java.io.File;
import java.io.PrintWriter;
import java.util.List;

import jam.app.JamEnv;
import jam.app.JamProperties;
import jam.io.FileUtil;
import jam.io.IOUtil;

import pubmed.bulk.BulkFile;
import pubmed.subject.Subject;

/**
 * Provides a base class to generate and store article-subject
 * relevance summary records.
 */
public abstract class RelevanceSummaryFileBase {
    /**
     * The file containing the summary records.
     */
    protected final File summaryFile;

    /**
     * Creates a new relevance summary file with a fixed physical
     * file.
     *
     * @param summaryFile the file contining the summary records.
     */
    protected RelevanceSummaryFileBase(File summaryFile) {
        this.summaryFile = summaryFile;

        synchronized (RelevanceSummaryFileBase.class) {
            FileUtil.ensureParentDirs(summaryFile);
        }
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
        if (JamProperties.isSet(RELEV_DIR_PROPERTY))
            return new File(JamProperties.getRequired(RELEV_DIR_PROPERTY));
        else
            return new File(JamEnv.getRequired(BulkFile.LOCAL_DIRNAME_ENV), RELEV_DIR_NAME);
    }

    /**
     * Appends summary records to the summary file (writing the header
     * line when necessary).
     *
     * @param summaryRecords the summary records to append.
     *
     * @throws RuntimeException if any I/O errors occur.
     */
    protected void appendSummaryRecords(List<RelevanceSummaryRecord> summaryRecords) {
        long fileLen = summaryFile.length();

        try (PrintWriter writer = IOUtil.openWriter(summaryFile, true)) {
            if (fileLen < 1)
                writer.println(RelevanceSummaryRecord.header());

            for (RelevanceSummaryRecord summaryRecord : summaryRecords)
                writer.println(summaryRecord.format());
        }
    }

    /**
     * Deletes the physical relevance summary file.
     *
     * @return {@code true} iff the summary files was successfully
     * deleted.
     */
    public boolean delete() {
        return summaryFile.delete();
    }

    /**
     * Loads the records in this file into an indexed table.
     *
     * @return the records in this as an indexed table.
     */
    public RelevanceSummaryTable load() {
        return RelevanceSummaryTable.load(summaryFile);
    }
}
