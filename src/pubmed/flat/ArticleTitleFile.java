
package pubmed.flat;

import java.io.File;

import jam.lang.JamException;

import pubmed.xml.PubmedArticleElement;

/**
 * Reads and writes flat files containing article titles.
 */
public final class ArticleTitleFile extends PubmedFlatFile<ArticleTitleRecord> {
    private ArticleTitleFile(File xmlFile) {
        super(xmlFile);
    }

    /**
     * The suffix for flat file names.
     */
    public static final String SUFFIX = "title";

    /**
     * Returns the flat file derived from a bulk XML file.
     *
     * @param xmlFile a bulk XML file.
     *
     * @return the flat file derived from the given bulk XML file.
     */
    public static ArticleTitleFile from(File xmlFile) {
        return new ArticleTitleFile(xmlFile);
    }

    /**
     * Loads the data records in this flat file.
     *
     * @return a table containing the data records in this flat file.
     *
     * @throws RuntimeException unless the physical flat file has been
     * generated.
     */
    public ArticleTitleTable load() {
        if (!exists())
            throw JamException.runtime("Missing file [%s].", flatFile);

        ArticleTitleTable table = new ArticleTitleTable();
        table.parse(flatFile);

        return table;
    }

    @Override public ArticleTitleRecord extractRecord(PubmedArticleElement element) {
        return ArticleTitleRecord.from(element);
    }

    @Override public String getBasenameSuffix() {
        return SUFFIX;
    }
}
