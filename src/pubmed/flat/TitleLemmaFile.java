
package pubmed.flat;

import java.io.File;

import jam.lang.JamException;

import pubmed.xml.PubmedArticleElement;

/**
 * Reads and writes flat files containing lemmatized article titles.
 */
public final class TitleLemmaFile extends PubmedFlatFile<TitleLemmaRecord> {
    private TitleLemmaFile(File xmlFile) {
        super(xmlFile);
    }

    /**
     * The suffix for flat file names.
     */
    public static final String SUFFIX = "title_lemma";

    /**
     * Returns the flat file derived from a bulk XML file.
     *
     * @param xmlFile a bulk XML file.
     *
     * @return the flat file derived from the given bulk XML file.
     */
    public static TitleLemmaFile from(File xmlFile) {
        return new TitleLemmaFile(xmlFile);
    }

    /**
     * Loads the data records in this flat file.
     *
     * @return a table containing the data records in this flat file.
     *
     * @throws RuntimeException unless the physical flat file has been
     * generated.
     */
    public TitleLemmaTable load() {
        if (!exists())
            throw JamException.runtime("Missing file [%s].", flatFile);

        TitleLemmaTable table = new TitleLemmaTable();
        table.parse(flatFile);

        return table;
    }

    @Override public TitleLemmaRecord extractRecord(PubmedArticleElement element) {
        return TitleLemmaRecord.from(element);
    }

    @Override public String getBasenameSuffix() {
        return SUFFIX;
    }
}
