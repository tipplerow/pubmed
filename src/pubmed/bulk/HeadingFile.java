
package pubmed.bulk;

import java.util.List;

import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.ImmutableListMultimap.Builder;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.SetMultimap;

import pubmed.article.PMID;
import pubmed.flat.HeadingRecord;
import pubmed.flat.HeadingTable;
import pubmed.mesh.MeshDescriptor;
import pubmed.xml.PubmedArticleElement;

/**
 * Reads and writes joining files for articles and {@code MeSH}
 * descriptors.
 */
public final class HeadingFile extends MultiContentFile<HeadingRecord> {
    private SetMultimap<PMID, MeshDescriptor> descriptorMap;

    private HeadingFile(BulkFile bulkFile) {
        super(bulkFile);
    }

    /**
     * The suffix for flat file names.
     */
    public static final String SUFFIX = "heading";

    /**
     * Returns the flat file derived from a bulk XML file.
     *
     * @param bulkFile a bulk XML file.
     *
     * @return the flat file derived from the given bulk XML file.
     */
    public static HeadingFile from(BulkFile bulkFile) {
        return new HeadingFile(bulkFile);
    }

    public synchronized SetMultimap<PMID, MeshDescriptor> getDescriptorMap() {
        if (descriptorMap == null)
            mapDescriptors();

        return descriptorMap;
    }

    private void mapDescriptors() {
        if (!exists())
            processFile(false);

        /*
        RecordStore<HeadingRecord> recordStore = load();
        ImmutableSetMultimap.Builder<PMID, MeshDescriptor> builder = ImmutableSetMultimap.builder();

        for (HeadingRecord record : recordStore)
            builder.put(record.getPMID(), MeshDescriptor.instance(record.get);

        recordMap = builder.build();
        ListMultimap<PMID, HeadingRecord> recordMap = getRecordMap();        
        ImmutableListMultimap.Builder<PMID, MeshDescriptor> builder = ImmutableListMultimap.builder();

        for (HeadingRecord record : recordStore)
            builder.put(record.getPMID(), record);

        descriptorMap = builder.build();
        */
    }

    @Override protected ImmutableListMultimap.Builder<PMID, HeadingRecord> createRecordMapBuilder() {
        return ImmutableListMultimap.builder();
    }

    @Override public List<HeadingRecord> extractRecords(PubmedArticleElement element) {
        return HeadingRecord.from(element);
    }

    @Override public String getSuffix() {
        return SUFFIX;
    }

    @Override public HeadingTable load() {
        return (HeadingTable) super.load();
    }

    @Override public HeadingTable newStore() {
        return new HeadingTable();
    }
}
