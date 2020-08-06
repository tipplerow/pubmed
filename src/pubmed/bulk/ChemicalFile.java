
package pubmed.bulk;

import java.util.List;

import com.google.common.collect.ImmutableSetMultimap;
import com.google.common.collect.ImmutableSetMultimap.Builder;

import pubmed.article.PMID;
import pubmed.flat.ChemicalRecord;
import pubmed.flat.ChemicalTable;
import pubmed.xml.PubmedArticleElement;

/**
 * Reads and writes joining files for articles and chemical
 * substances.
 */
public final class ChemicalFile extends MultiContentFile<ChemicalRecord> {
    private ChemicalFile(BulkFile bulkFile) {
        super(bulkFile);
    }

    /**
     * The suffix for flat file names.
     */
    public static final String SUFFIX = "chemical";

    /**
     * Returns the flat file derived from a bulk XML file.
     *
     * @param bulkFile a bulk XML file.
     *
     * @return the flat file derived from the given bulk XML file.
     */
    public static ChemicalFile from(BulkFile bulkFile) {
        return new ChemicalFile(bulkFile);
    }

    @Override protected ImmutableSetMultimap.Builder<PMID, ChemicalRecord> createRecordMapBuilder() {
        return ImmutableSetMultimap.builder();
    }

    @Override public List<ChemicalRecord> extractRecords(PubmedArticleElement element) {
        return ChemicalRecord.from(element);
    }

    @Override public String getSuffix() {
        return SUFFIX;
    }

    @Override public ChemicalTable load() {
        return (ChemicalTable) super.load();
    }

    @Override public ChemicalTable newStore() {
        return new ChemicalTable();
    }
}
