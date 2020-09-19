
package pubmed.app;

import pubmed.bulk.BulkFTP;

/**
 * Downloads daily {@code PubMed} bulk XML files.
 */
public final class BulkFTPDownload {
    /**
     * Downloads daily {@code PubMed} bulk XML files.
     */
    public static void main(String[] args) {
        BulkFTP.downloadUpdateFiles();
    }
}
