
package pubmed.flat;

import java.io.File;

import jam.lang.JamException;

import pubmed.xml.PubmedArticleElement;

/**
 * Reads and writes flat files containing article abstracts.
 */
public final class ArticleAbstractFile extends PubmedFlatFile<ArticleAbstractRecord> {
    private ArticleAbstractFile(File xmlFile) {
        super(xmlFile);
    }

    /**
     * The suffix for flat file names.
     */
    public static final String SUFFIX = "abstract";

    /**
     * Returns the flat file derived from a bulk XML file.
     *
     * @param xmlFile a bulk XML file.
     *
     * @return the flat file derived from the given bulk XML file.
     */
    public static ArticleAbstractFile from(File xmlFile) {
        return new ArticleAbstractFile(xmlFile);
    }

    /**
     * Loads the data records in this flat file.
     *
     * @return a table containing the data records in this flat file.
     *
     * @throws RuntimeException unless the physical flat file has been
     * generated.
     */
    public ArticleAbstractTable load() {
        if (!exists())
            throw JamException.runtime("Missing file [%s].", flatFile);

        ArticleAbstractTable table = new ArticleAbstractTable();
        table.parse(flatFile);

        return table;
    }

    @Override public ArticleAbstractRecord extractRecord(PubmedArticleElement element) {
        return ArticleAbstractRecord.from(element);
    }

    @Override public String getBasenameSuffix() {
        return SUFFIX;
    }
}
