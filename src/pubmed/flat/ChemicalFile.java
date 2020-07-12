
package pubmed.flat;

import java.io.File;
import java.util.List;

import jam.lang.JamException;

import pubmed.xml.PubmedArticleElement;
import pubmed.xml.PubmedXmlDocument;

/**
 * Reads and writes joining files for articles and chemical
 * substances.
 */
public final class ChemicalFile extends PubmedJoinFile<ChemicalRecord> {
    private ChemicalFile(File xmlFile) {
        super(xmlFile);
    }

    /**
     * The suffix for flat file names.
     */
    public static final String SUFFIX = "chemical";

    /**
     * Returns the flat file derived from a bulk XML file.
     *
     * @param xmlFile a bulk XML file.
     *
     * @return the flat file derived from the given bulk XML file.
     */
    public static ChemicalFile from(File xmlFile) {
        return new ChemicalFile(xmlFile);
    }

    /**
     * Loads the data records in this flat file.
     *
     * @return a table containing the data records in this flat file.
     *
     * @throws RuntimeException unless the physical flat file has been
     * generated.
     */
    public ChemicalTable load() {
        if (!exists())
            throw JamException.runtime("Missing file [%s].", flatFile);

        ChemicalTable table = new ChemicalTable();
        table.parse(flatFile);

        return table;
    }

    @Override public List<ChemicalRecord> extractRecords(PubmedArticleElement element) {
        return ChemicalRecord.from(element);
    }

    @Override public String getBasenameSuffix() {
        return SUFFIX;
    }
}
