
package pubmed.flat;

import java.io.File;
import java.util.List;

import jam.lang.JamException;

import pubmed.xml.PubmedArticleElement;

/**
 * Reads and writes joining files for articles and {@code MeSH}
 * heading records.
 */
public final class HeadingFile extends PubmedJoinFile<HeadingRecord> {
    private HeadingFile(File xmlFile) {
        super(xmlFile);
    }

    /**
     * The suffix for flat file names.
     */
    public static final String SUFFIX = "heading";

    /**
     * Returns the flat file derived from a bulk XML file.
     *
     * @param xmlFile a bulk XML file.
     *
     * @return the flat file derived from the given bulk XML file.
     */
    public static HeadingFile from(File xmlFile) {
        return new HeadingFile(xmlFile);
    }

    /**
     * Loads the data records in this flat file.
     *
     * @return a table containing the data records in this flat file.
     *
     * @throws RuntimeException unless the physical flat file has been
     * generated.
     */
    public HeadingTable load() {
        if (!exists())
            throw JamException.runtime("Missing file [%s].", flatFile);

        HeadingTable table = new HeadingTable();
        table.parse(flatFile);

        return table;
    }

    @Override public List<HeadingRecord> extractRecords(PubmedArticleElement element) {
        return HeadingRecord.from(element);
    }

    @Override public String getBasenameSuffix() {
        return SUFFIX;
    }
}
