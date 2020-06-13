
package pubmed.sql;

import java.io.File;
import java.util.Set;
import java.util.TreeSet;

import jam.app.JamEnv;
import jam.app.JamLogger;
import jam.io.FileUtil;
import jam.process.JamProcess;
import jam.util.ListUtil;

/**
 * Downloads and processes the daily PubMed update files.
 */
public abstract class BulkDailyUpdate {
    private final File dailyDir;
    private final File updateDir;
    private final String remoteDirName;

    private final Set<String> processed = new TreeSet<String>();
    private final Set<String> unprocessed = new TreeSet<String>();

    /**
     * Creates a new daily update driver.
     */
    protected BulkDailyUpdate() {
        this.dailyDir = resolveDailyDir();
        this.updateDir = resolveUpdateDir();
        this.remoteDirName = resolveRemoteDirName();
    }

    private static File resolveDailyDir() {
        return new File(JamEnv.getRequired(LOCAL_DIRNAME_ENV), "daily");
    }

    private static File resolveUpdateDir() {
        return new File(JamEnv.getRequired(LOCAL_DIRNAME_ENV), "updatefiles");
    }

    private static String resolveRemoteDirName() {
        //
        // The FTP directory name must end with a slash to allow the
        // file listing...
        //
        String dirName = JamEnv.getRequired(REMOTE_DIRNAME_ENV);

        if (!dirName.endsWith("/"))
            dirName = dirName + "/";

        return dirName;
    }

    /**
     * Name of the environment variable that specifies the 
     * remote directory that contains the daily update files.
     */
    public static final String LOCAL_DIRNAME_ENV = "PUBMED_LOCAL_DIR";

    /**
     * Name of the environment variable that specifies the URL of the
     * remote directory that contains the daily update files.
     */
    public static final String REMOTE_DIRNAME_ENV = "PUBMED_REMOTE_DIR";

    /**
     * Returns the bulk file processor that will process the new daily
     * files.
     *
     * @return the bulk file processor that will process the new daily
     * files.
     */
    public abstract BulkFileProcessor getBulkFileProcessor();

    /**
     * Executes the daily update.
     */
    public void run() {
        FileUtil.ensureDir(dailyDir);
        FileUtil.ensureDir(updateDir);

        findProcessed();
        findUnprocessed();
        downloadUnprocessed();
        getBulkFileProcessor().process(dailyDir);
        moveProcessed();
    }

    private void findProcessed() {
        File[] files = updateDir.listFiles();

        for (File file : files)
            processed.add(file.getName());
    }

    private void findUnprocessed() {
        JamProcess process = JamProcess.create("curl", "--list-only", remoteDirName);
        process.run();

        for (String fileName : process.stdout())
            if (BulkFileProcessor.BULK_FILE_PREDICATE.test(fileName) && !processed.contains(fileName))
                unprocessed.add(fileName);
    }

    private void downloadUnprocessed() {
        for (String fileName : unprocessed)
            downloadUnprocessed(fileName);
    }

    private void downloadUnprocessed(String fileName) {
        //
        // The FTP directory already ends with a slash, so just add
        // the file name without the separator...
        //
        String src = remoteDirName + fileName;
        String dst = FileUtil.join(FileUtil.getCanonicalPath(dailyDir), fileName);

        JamProcess process = JamProcess.create("curl", "-o", dst, src);
        process.run();
    }

    private void moveProcessed() {
        for (String fileName : unprocessed)
            moveProcessed(fileName);
    }

    private void moveProcessed(String fileName) {
        File srcFile = new File(dailyDir, fileName);
        File dstFile = new File(updateDir, fileName);

        if (!srcFile.renameTo(dstFile))
            JamLogger.warn("Failed to move file [%s].", fileName);
    }
}
