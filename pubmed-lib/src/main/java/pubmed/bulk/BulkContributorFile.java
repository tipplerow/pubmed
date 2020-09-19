
package pubmed.bulk;

import java.io.File;

import jam.io.TOCFile;

/**
 * Maintains a file that contains the names of the bulk XML files that
 * have contributed to a monitored production process (the contents of
 * a content or analysis file).
 */
public final class BulkContributorFile {
    private final TOCFile tocFile;

    private BulkContributorFile(File file) {
        this.tocFile = TOCFile.instance(file);
    }

    private static String contributorName(BulkFile bulkFile) {
        //
        // In the standard daily update, the bulk files move from the
        // "process" directory to the "updatefiles" directory, so only
        // the basename is relevant...
        //
        return bulkFile.getBaseName();
    }

    /**
     * Returns the contributor file with a fixed physical file path.
     *
     * @param file the path of the underlying physical file.
     *
     * @return the contributor file with the specified physical file.
     */
    public static BulkContributorFile instance(File file) {
        return new BulkContributorFile(file);
    }

    /**
     * Returns the contributor file with a fixed physical file path.
     *
     * @param fileName the path of the underlying physical file.
     *
     * @return the contributor file with the specified physical file.
     */
    public static BulkContributorFile instance(String fileName) {
        return instance(new File(fileName));
    }

    /**
     * Adds a bulk file to the set of production process contributors
     * (both in-memory and in the underlying physical file).
     *
     * @param bulkFile the contributing bulk file.
     */
    public synchronized void addContributor(BulkFile bulkFile) {
        tocFile.add(contributorName(bulkFile));
    }

    /**
     * Deletes the underlying physical file.
     *
     * @return {@code true} iff the underlying file was successfully
     * deleted.
     */
    public boolean delete() {
        return tocFile.delete();
    }

    /**
     * Identifies existing contributor files.
     *
     * @return {@code true} iff the underlying file exists.
     */
    public boolean exists() {
        return tocFile.exists();
    }

    /**
     * Determines whether a bulk file has already contributed to the
     * monitored production process.
     *
     * @param bulkFile the bulk file in question.
     *
     * @return {@code true} iff the specified bulk file has already
     * contributed to the monitored production process.
     */
    public boolean isContributor(BulkFile bulkFile) {
        return tocFile.contains(contributorName(bulkFile));
    }
}
