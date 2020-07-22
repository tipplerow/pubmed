
package pubmed.bulk;

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import jam.io.FileUtil;
import jam.lang.JamException;
import jam.util.ListUtil;

/**
 * Represents a bulk XML file in the {@code PubMed} production system.
 */
public final class BulkFile {
    private final File file;

    private BulkFile(File file) {
        this.file = file;
    }

    /**
     * A predicate that matches {@code PubMed} bulk XML data files.
     */
    public static final Predicate<String> FILE_PREDICATE =
        Pattern.compile("^pubmed20n[0-9]{4}\\.xml\\.gz$").asMatchPredicate();

    /**
     * A filter that selects {@code PubMed} bulk XML data files.
     */
    public static final FileFilter FILE_FILTER =
        new FileFilter() {
            @Override public boolean accept(File file) {
                return FILE_PREDICATE.test(file.getName());
            }
        };

    /**
     * Creates a new bulk file.
     *
     * @param fileName the path to the physical file.
     *
     * @return the new bulk file for the specified path.
     */
    public static BulkFile create(String fileName) {
        return create(new File(fileName));
    }

    /**
     * Creates a new bulk file.
     *
     * @param file the path to the physical file.
     *
     * @return the new bulk file for the specified path.
     */
    public static BulkFile create(File file) {
        return new BulkFile(file);
    }

    /**
     * Lists all bulk data files in a given directory.
     *
     * @param dirName the bulk file directory to list.
     *
     * @return a list containing all bulk files in the specified
     * directory.
     */
    public static List<BulkFile> list(String dirName) {
        return list(new File(dirName));
    }

    /**
     * Lists all bulk data files in a given directory.
     *
     * @param directory the bulk file directory to list.
     *
     * @return a list containing all bulk files in the specified
     * directory.
     */
    public static List<BulkFile> list(File directory) {
        File[] bulkFileArray = directory.listFiles(FILE_FILTER);
        Arrays.sort(bulkFileArray);

        return ListUtil.apply(Arrays.asList(bulkFileArray), file -> create(file));
    }

    /**
     * Returns the basename of the underlying file.
     *
     * @return the basename of the underlying file.
     */
    public String getBaseName() {
        return FileUtil.getBaseName(file);
    }

    /**
     * Returns the prefix of the underlying file name with the
     * absolute path information retained.
     *
     * @return the prefix of the underlying file name with the
     * absolute path information retained.
     */
    public String getBaseNamePrefix() {
        return FileUtil.getBaseNamePrefix(file);
    }

    /**
     * Returns the prefix of the underlying file name with the
     * absolute path information retained.
     *
     * @return the prefix of the underlying file name with the
     * absolute path information retained.
     */
    public String getCanonicalPrefix() {
        return FileUtil.getCanonicalPrefix(file);
    }

    /**
     * Returns the name of the directory containing the underlying
     * file.
     *
     * @return the name of the directory containing the underlying
     * file.
     */
    public String getDirName() {
        return FileUtil.getDirName(file);
    }

    /**
     * Returns the underlying physical file.
     *
     * @return the underlying physical file.
     */
    public File getFile() {
        return file;
    }

    @Override public boolean equals(Object obj) {
        return (obj instanceof BulkFile) && equalsFile((BulkFile) obj);
    }

    private boolean equalsFile(BulkFile that) {
        return this.file.equals(that.file);
    }

    @Override public int hashCode() {
        return file.hashCode();
    }

    @Override public String toString() {
        return String.format("BulkFile(%s)", file);
    }
}
