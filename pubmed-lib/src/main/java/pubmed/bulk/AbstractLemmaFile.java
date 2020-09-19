
package pubmed.bulk;

import pubmed.flat.AbstractLemmaRecord;
import pubmed.flat.AbstractLemmaTable;
import pubmed.xml.PubmedArticleElement;

/**
 * Reads and writes flat files containing lemmatized article
 * abstracts.
 */
public final class AbstractLemmaFile extends UniqueContentFile<AbstractLemmaRecord> {
    private AbstractLemmaFile(BulkFile bulkFile) {
        super(bulkFile);
    }

    /**
     * The suffix for flat file names.
     */
    public static final String SUFFIX = "abstract_lemma";

    /**
     * Returns the flat file derived from a bulk XML file.
     *
     * @param bulkFile a bulk XML file.
     *
     * @return the flat file derived from the given bulk XML file.
     */
    public static AbstractLemmaFile instance(BulkFile bulkFile) {
        return new AbstractLemmaFile(bulkFile);
    }

    @Override public AbstractLemmaRecord extractRecord(PubmedArticleElement element) {
        return AbstractLemmaRecord.from(element);
    }

    @Override public String getSuffix() {
        return SUFFIX;
    }

    @Override public AbstractLemmaTable load() {
        return (AbstractLemmaTable) super.load();
    }

    @Override public AbstractLemmaTable newStore() {
        return new AbstractLemmaTable();
    }
}
