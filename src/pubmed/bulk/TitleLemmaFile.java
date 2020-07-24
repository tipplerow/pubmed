
package pubmed.bulk;

import pubmed.flat.TitleLemmaRecord;
import pubmed.flat.TitleLemmaTable;
import pubmed.xml.PubmedArticleElement;

/**
 * Reads and writes flat files containing lemmatized article titles.
 */
public final class TitleLemmaFile extends UniqueContentFile<TitleLemmaRecord> {
    private TitleLemmaFile(BulkFile bulkFile) {
        super(bulkFile);
    }

    /**
     * The suffix for flat file names.
     */
    public static final String SUFFIX = "title_lemma";

    /**
     * Returns the flat file derived from a bulk XML file.
     *
     * @param bulkFile a bulk XML file.
     *
     * @return the flat file derived from the given bulk XML file.
     */
    public static TitleLemmaFile from(BulkFile bulkFile) {
        return new TitleLemmaFile(bulkFile);
    }

    @Override public TitleLemmaRecord extractRecord(PubmedArticleElement element) {
        return TitleLemmaRecord.from(element);
    }

    @Override public String getSuffix() {
        return SUFFIX;
    }

    @Override public TitleLemmaTable load() {
        return (TitleLemmaTable) super.load();
    }

    @Override public TitleLemmaTable newStore() {
        return new TitleLemmaTable();
    }
}
