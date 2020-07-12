
package pubmed.flat;

import java.io.File;
import java.util.List;

import jam.lang.JamException;

import pubmed.xml.PubmedArticleElement;
import pubmed.xml.PubmedXmlDocument;

/**
 * Reads and writes joining files for articles and (lemmatized)
 * keywords.
 */
public final class KeywordFile extends PubmedJoinFile<KeywordRecord> {
    private KeywordFile(File xmlFile) {
        super(xmlFile);
    }

    /**
     * The suffix for flat file names.
     */
    public static final String SUFFIX = "keyword";

    /**
     * Returns the flat file derived from a bulk XML file.
     *
     * @param xmlFile a bulk XML file.
     *
     * @return the flat file derived from the given bulk XML file.
     */
    public static KeywordFile from(File xmlFile) {
        return new KeywordFile(xmlFile);
    }

    /**
     * Loads the data records in this flat file.
     *
     * @return a table containing the data records in this flat file.
     *
     * @throws RuntimeException unless the physical flat file has been
     * generated.
     */
    public KeywordTable load() {
        if (!exists())
            throw JamException.runtime("Missing file [%s].", flatFile);

        KeywordTable table = new KeywordTable();
        table.parse(flatFile);

        return table;
    }

    @Override public List<KeywordRecord> extractRecords(PubmedArticleElement element) {
        return KeywordRecord.from(element);
    }

    @Override public String getBasenameSuffix() {
        return SUFFIX;
    }
}
