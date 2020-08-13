
package pubmed.bulk;

import pubmed.flat.JournalRecord;
import pubmed.flat.JournalTable;
import pubmed.xml.PubmedArticleElement;

/**
 * Reads and writes flat files containing article journals.
 */
public final class JournalFile extends UniqueContentFile<JournalRecord> {
    private JournalFile(BulkFile bulkFile) {
        super(bulkFile);
    }

    /**
     * The suffix for flat file names.
     */
    public static final String SUFFIX = "journal";

    /**
     * Returns the flat file derived from a bulk XML file.
     *
     * @param bulkFile a bulk XML file.
     *
     * @return the flat file derived from the given bulk XML file.
     */
    public static JournalFile instance(BulkFile bulkFile) {
        return new JournalFile(bulkFile);
    }

    @Override public JournalRecord extractRecord(PubmedArticleElement element) {
        return JournalRecord.from(element);
    }

    @Override public String getSuffix() {
        return SUFFIX;
    }

    @Override public JournalTable load() {
        return (JournalTable) super.load();
    }

    @Override public JournalTable newStore() {
        return new JournalTable();
    }
}
