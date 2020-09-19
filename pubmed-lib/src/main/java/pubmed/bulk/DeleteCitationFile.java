
package pubmed.bulk;

import java.util.List;

import jam.util.ListUtil;

import pubmed.flat.PMIDRecord;
import pubmed.flat.PMIDTable;
import pubmed.xml.PubmedXmlDocument;

/**
 * Provides a base class to for flat files containing unique article
 * attribute data (one row per article).
 */
public final class DeleteCitationFile extends DocumentContentFile<PMIDRecord> {
    private DeleteCitationFile(BulkFile bulkFile) {
        super(bulkFile);
    }

    /**
     * The suffix for flat file names.
     */
    public static final String SUFFIX = "delete_citation";

    /**
     * Returns the flat file derived from a bulk XML file.
     *
     * @param bulkFile a bulk XML file.
     *
     * @return the flat file derived from the given bulk XML file.
     */
    public static DeleteCitationFile instance(BulkFile bulkFile) {
        return new DeleteCitationFile(bulkFile);
    }

    @Override public List<PMIDRecord> extractRecords(PubmedXmlDocument document) {
        return ListUtil.apply(document.getDeletedCitations(), pmid -> PMIDRecord.create(pmid));
    }

    @Override public String getSuffix() {
        return SUFFIX;
    }

    @Override public PMIDTable load() {
        return (PMIDTable) super.load();
    }

    @Override public PMIDTable newStore() {
        return new PMIDTable();
    }
}
