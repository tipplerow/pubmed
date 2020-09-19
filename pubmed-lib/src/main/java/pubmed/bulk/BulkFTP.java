
package pubmed.bulk;

import java.io.File;
import java.util.List;
import java.util.Set;

import jam.app.JamEnv;
import jam.io.FTPUtil;
import jam.io.FileUtil;
import jam.util.StreamUtil;

/**
 * Downloads daily {@code PubMed} bulk XML files.
 */
public final class BulkFTP {
    private static final File localDir = BulkFile.resolveUpdateDir();
    private static final File processDir = BulkFile.resolveProcessDir();
    private static final String remoteDirName = resolveRemoteDirName();

    /**
     * Name of the environment variable that specifies the URL of the
     * remote directory that contains the daily update files.
     */
    public static final String REMOTE_DIRNAME_ENV = "PUBMED_REMOTE_DIR";

    /**
     * Downloads a single daily update file from the remote FTP server.
     *
     * @param fileName the base name of the file to download.
     *
     * @return {@code true} iff the file was downloaded successfully.
     */
    public static boolean downloadUpdateFile(String fileName) {
        FileUtil.ensureDir(processDir);

        File localFile = new File(processDir, fileName);
        String remoteName = remoteDirName + fileName;

        return FTPUtil.download(localFile, remoteName);
    }

    /**
     * Downloads daily update files that are available on the remote
     * FTP server but not present in the local update directory.
     *
     * @return {@code true} iff all files were downloaded successfully.
     */
    public static boolean downloadUpdateFiles() {
        boolean status = true;

        Set<String> localFiles = listLocalUpdateFiles();
        Set<String> remoteFiles = listRemoteUpdateFiles();

        for (String fileName : remoteFiles)
            if (!localFiles.contains(fileName))
                if (!downloadUpdateFile(fileName))
                    status = false;

        return status;
    }

    /**
     * Lists the daily update files present in the local update
     * directory.
     *
     * @return a set containg the names of the daily update files
     * present in the local update directory.
     */
    public static Set<String> listLocalUpdateFiles() {
        return filterFileNames(List.of(localDir.list()));
    }

    /**
     * Lists the daily update files available on the remote FTP
     * server.
     *
     * @return a set containg the names of the daily update files
     * available on the remote FTP server.
     */
    public static Set<String> listRemoteUpdateFiles() {
        return filterFileNames(FTPUtil.list(remoteDirName));
    }

    private static Set<String> filterFileNames(List<String> fileNames) {
        return StreamUtil.toTreeSet(fileNames.stream().filter(BulkFile.FILE_PREDICATE));
    }

    /**
     * Returns the name of the remote (FTP) directory containing the
     * daily update files.
     *
     * @return the name of the remote (FTP) directory containing the
     * daily update files.
     */
    public static String resolveRemoteDirName() {
        //
        // The FTP directory name must end with a slash to allow the
        // file listing...
        //
        String dirName = JamEnv.getRequired(REMOTE_DIRNAME_ENV);

        if (!dirName.endsWith("/"))
            dirName = dirName + "/";

        return dirName;
    }

    public static void main(String[] args) {
        downloadUpdateFiles();
    }
}
