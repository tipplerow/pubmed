
package pubmed.flat;

import java.io.File;
import java.util.List;

import jam.lang.JamException;

import pubmed.xml.PubmedArticleElement;
import pubmed.xml.PubmedXmlDocument;

/**
 * Reads and writes joining files for articles and the journals where
 * they appeared.
 */
public final class JournalFile extends PubmedJoinFile<JournalRecord> {
    private JournalFile(File xmlFile) {
        super(xmlFile);
    }

    /**
     * The suffix for flat file names.
     */
    public static final String SUFFIX = "journal";

    /**
     * Returns the flat file derived from a bulk XML file.
     *
     * @param xmlFile a bulk XML file.
     *
     * @return the flat file derived from the given bulk XML file.
     */
    public static JournalFile from(File xmlFile) {
        return new JournalFile(xmlFile);
    }

    /**
     * Loads the data records in this flat file.
     *
     * @return a table containing the data records in this flat file.
     *
     * @throws RuntimeException unless the physical flat file has been
     * generated.
     */
    public JournalTable load() {
        if (!exists())
            throw JamException.runtime("Missing file [%s].", flatFile);

        JournalTable table = new JournalTable();
        table.parse(flatFile);

        return table;
    }

    @Override public List<JournalRecord> extractRecords(PubmedArticleElement element) {
        JournalRecord record = JournalRecord.from(element);

        if (record != null)
            return List.of(record);
        else
            return List.of();
    }

    @Override public String getBasenameSuffix() {
        return SUFFIX;
    }
}
