
package pubmed.bulk;

import java.io.File;
import java.io.PrintWriter;
import java.util.Collection;

import jam.app.JamLogger;
import jam.io.IOUtil;
import jam.io.FileUtil;
import jam.io.LineReader;
import jam.io.ZipUtil;
import jam.flat.RecordStore;
import jam.lang.JamException;

import pubmed.flat.PubmedFlatRecord;

/**
 * Provides a base class for flat files containing records derived
 * from bulk XML files.
 */
public abstract class PubmedFlatFile<V extends PubmedFlatRecord> {
    /**
     * The bulk XML file.
     */
    protected final BulkFile bulkFile;

    /**
     * The path to this flat file.
     */
    protected final File flatFile;

    /**
     * The path to the gzipped version of this flat file.
     */
    protected final File gzipFile;

    /**
     * Creates a new flat file for records derived from a given bulk
     * XML file.
     *
     * @param bulkFile the bulk XML file containing articles to be
     * processed.
     */
    protected PubmedFlatFile(BulkFile bulkFile) {
        this.bulkFile = bulkFile;
        this.flatFile = resolveFlatFile();
        this.gzipFile = resolveGZipFile();
    }

    private File resolveFlatFile() {
        validateSuffix();

        String basePrefix = bulkFile.getBaseNamePrefix();
        String bulkDirName = bulkFile.getDirName();
        String flatFileDirName = FileUtil.join(bulkDirName, basePrefix);
        String flatFileBaseName = basePrefix + "_" + getSuffix() + EXTENSION;

        return FileUtil.newFile(flatFileDirName, flatFileBaseName);
    }

    private void validateSuffix() {
        String suffix = getSuffix();

        if (suffix.contains("."))
            throw JamException.runtime("Invalid suffix: [%s]; extensions are not allowed.", suffix);
    }

    private File resolveGZipFile() {
        String flatFileName = FileUtil.getCanonicalPath(flatFile);
        return new File(flatFileName + ZipUtil.GZIP_SUFFIX);
    }

    /**
     * The file name extension for all flat files.
     */
    public static final String EXTENSION = ".psv";

    /**
     * Returns the suffix to append to the bulk XML file prefix when
     * composing the name of this flat file (excluding the trailing
     * {@code .psv} extension).
     *
     * @return the suffix to append to the bulk XML file prefix when
     * composing the name of this flat file.
     */
    public abstract String getSuffix();

    /**
     * Creates a new empty store for the records in this flat file.
     *
     * @return a new empty store for the records in this flat file.
     */
    public abstract RecordStore<V> newStore();

    /**
     * Determines whether a flat file must be processed.
     *
     * @param overwrite whether to overwrite an existing flat file (or
     * skip processing if the flat file already exists).
     *
     * @return {@code true} if the physical flat file is missing or
     * the {@code overwrite} flag is {@code true}.
     */
    protected boolean mustProcess(boolean overwrite) {
        if (overwrite || !exists()) {
            return true;
        }
        else {
            JamLogger.info("File [%s] exists; not overwriting.", flatFile);
            return false;
        }
    }

    /**
     * Deletes the physical flat file.
     *
     * @return {@code true} iff the file existed and was successfully
     * deleted.
     */
    public boolean delete() {
        return flatFile.delete() || gzipFile.delete();
    }

    /**
     * Identifies existing flat files.
     *
     * @return {@code true} iff the physical flat file has already
     * been generated.
     */
    public boolean exists() {
        return flatFile.canRead() || gzipFile.canRead();
    }

    /**
     * Returns the bulk XML file providing data for this flat file.
     *
     * @return the bulk XML file providing data for this flat file.
     */
    public BulkFile getBulkFile() {
        return bulkFile;
    }

    /**
     * Returns the path to the physical flat file.
     *
     * @return the path to the physical flat file.
     */
    public File getFlatFile() {
        return flatFile;
    }

    /**
     * Returns the path to the gzipped version of the flat file.
     *
     * @return the path to the gzipped version of the flat file.
     */
    public File getGZipFile() {
        return gzipFile;
    }

    /**
     * Loads the records in this flat file.
     *
     * @return the records in this flat file.
     *
     * @throws RuntimeException unless the physical flat file exists
     * and can be parsed sucessfully.
     */
    public RecordStore<V> load() {
        RecordStore<V> records = newStore();

        try (LineReader reader = openReader()) {
            records.load(reader);
        }

        return records;
    }

    /**
     * Opens a line reader for this file.
     *
     * @return an open line reader for this file.
     *
     * @throws RuntimeException unless the physical flat file exists
     * and can be opened for reading.
     */
    public LineReader openReader() {
        if (flatFile.canRead())
            return LineReader.open(flatFile);

        if (gzipFile.canRead())
            return LineReader.open(gzipFile);

        throw JamException.runtime("Missing file: [%s].", flatFile);
    }

    /**
     * Opens a writer for this file.
     *
     * @param append whether to append ({@code true}) or truncate
     * ({@code false}) an existing file.
     *
     * @return an open writer for this file.
     *
     * @throws RuntimeException unless the physical flat file can be
     * opened for writing.
     */
    public PrintWriter openWriter(boolean append) {
        JamLogger.info("Writing file [%s]...", flatFile);
        return IOUtil.openWriter(flatFile, append);
    }

    /**
     * Writes records to the physical flat file.
     *
     * @param records the records to write.
     *
     * @param append whether to append ({@code true}) or truncate
     * ({@code false}) an existing file.
     *
     * @throws RuntimeException unless the physical flat file can be
     * opened for writing.
     */
    public void writeRecords(Collection<V> records, boolean append) {
        try (PrintWriter writer = openWriter(append)) {
            for (V record : records)
                if (record != null)
                    writer.println(record.format());
        }
    }
}
