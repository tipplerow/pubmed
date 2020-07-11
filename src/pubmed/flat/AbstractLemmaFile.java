
package pubmed.flat;

import java.io.File;

import jam.lang.JamException;

import pubmed.xml.PubmedArticleElement;
import pubmed.xml.PubmedXmlDocument;

/**
 * Represents a data record containing the identifier and abstract of a
 * {@code PubMed} article.
 */
public final class AbstractLemmaFile extends PubmedFlatFile<AbstractLemmaRecord> {
    private AbstractLemmaFile(File xmlFile) {
        super(xmlFile);
    }

    /**
     * The suffix for flat file names.
     */
    public static final String SUFFIX = "abstract_lemma";

    /**
     * Returns the flat file derived from a bulk XML file.
     *
     * @param xmlFile a bulk XML file.
     *
     * @return the flat file derived from the given bulk XML file.
     */
    public static AbstractLemmaFile from(File xmlFile) {
        return new AbstractLemmaFile(xmlFile);
    }

    /**
     * Loads the data records in this flat file.
     *
     * @return a table containing the data records in this flat file.
     *
     * @throws RuntimeException unless the physical flat file has been
     * generated.
     */
    public AbstractLemmaTable load() {
        if (!exists())
            throw JamException.runtime("Missing file [%s].", flatFile);

        AbstractLemmaTable table = new AbstractLemmaTable();
        table.parse(flatFile);

        return table;
    }

    @Override public AbstractLemmaRecord extractRecord(PubmedArticleElement element) {
        return AbstractLemmaRecord.from(element);
    }

    @Override public String getBasenameSuffix() {
        return SUFFIX;
    }
}
