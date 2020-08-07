
package pubmed.bulk;

import java.util.List;

import jam.util.ListUtil;

import pubmed.flat.KeywordRecord;
import pubmed.flat.KeywordTable;
import pubmed.xml.PubmedArticleElement;

/**
 * Reads and writes joining files for articles and (lemmatized)
 * keywords.
 */
public final class KeywordFile extends MultiContentFile<KeywordRecord> {
    private KeywordFile(BulkFile bulkFile) {
        super(bulkFile);
    }

    /**
     * The suffix for flat file names.
     */
    public static final String SUFFIX = "keyword";

    /**
     * Returns the flat file derived from a bulk XML file.
     *
     * @param bulkFile a bulk XML file.
     *
     * @return the flat file derived from the given bulk XML file.
     */
    public static KeywordFile from(BulkFile bulkFile) {
        return new KeywordFile(bulkFile);
    }

    @Override public List<KeywordRecord> extractRecords(PubmedArticleElement element) {
        return ListUtil.filter(KeywordRecord.from(element), record -> !record.getKeyword().isEmpty());
    }

    @Override public String getSuffix() {
        return SUFFIX;
    }

    @Override public KeywordTable load() {
        return (KeywordTable) super.load();
    }

    @Override public KeywordTable newStore() {
        return new KeywordTable();
    }
}
