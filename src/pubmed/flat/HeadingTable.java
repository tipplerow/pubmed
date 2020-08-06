
package pubmed.flat;

import java.io.File;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.SetMultimap;

import jam.flat.RecordStore;

import pubmed.article.PMID;
import pubmed.mesh.MeshDescriptorKey;
import pubmed.mesh.MeshQualifierKey;

public final class HeadingTable extends RecordStore<HeadingRecord> {
    private final SetMultimap<PMID, HeadingRecord> pmidMap = LinkedHashMultimap.create();
    private final SetMultimap<MeshQualifierKey, HeadingRecord> qualMap = LinkedHashMultimap.create();
    private final SetMultimap<MeshDescriptorKey, HeadingRecord> descMap = LinkedHashMultimap.create();
    
    /**
     * Creates a new table by parsing a flat file.
     *
     * @param file the flat file containing the table data.
     *
     * @return a new table containing the data in the given
     * flat file.
     */
    public static HeadingTable load(File file) {
        HeadingTable table = new HeadingTable();
        table.parse(file);
        return table;
    }

    /**
     * Returns a read-only set view of the article identifiers in this
     * table.
     *
     * @return a read-only set view of the article identifiers in this
     * table.
     */
    public Set<PMID> pmidSet() {
        return Collections.unmodifiableSet(pmidMap.keySet());
    }

    /**
     * Returns a read-only set view of the descriptor keys in this
     * table.
     *
     * @return a read-only set view of the descriptor keys in this
     * table.
     */
    public Set<MeshDescriptorKey> descriptorKeySet() {
        return Collections.unmodifiableSet(descMap.keySet());
    }

    /**
     * Returns a read-only set view of the qualifier keys in this
     * table.
     *
     * @return a read-only set view of the qualifier keys in this
     * table.
     */
    public Set<MeshQualifierKey> qualifierKeySet() {
        return Collections.unmodifiableSet(qualMap.keySet());
    }

    /**
     * Returns a read-only set view of the heading records containing
     * a specific article identifier.
     *
     * @param pmid the article identifier to select.
     *
     * @return a read-only set view of the heading records containing
     * the specified article identifier.
     */
    public Set<HeadingRecord> select(PMID pmid) {
        return Collections.unmodifiableSet(pmidMap.get(pmid));
    }

    /**
     * Returns a read-only set view of the heading records containing
     * a specific descriptor key.
     *
     * @param desc the descriptor key to select.
     *
     * @return a read-only set view of the heading records containing
     * the specified descriptor key.
     */
    public Set<HeadingRecord> select(MeshDescriptorKey desc) {
        return Collections.unmodifiableSet(descMap.get(desc));
    }

    /**
     * Returns a read-only set view of the heading records containing
     * a specific qualifier key.
     *
     * @param qual the qualifier key to select.
     *
     * @return a read-only set view of the heading records containing
     * the specified qualifier key.
     */
    public Set<HeadingRecord> select(MeshQualifierKey qual) {
        return Collections.unmodifiableSet(qualMap.get(qual));
    }

    @Override public HeadingRecord parse(String line) {
        return HeadingRecord.parse(line);
    }

    @Override public int count() {
        return pmidMap.size();
    }

    @Override public void insert(HeadingRecord record) {
        PMID pmid = record.getPMID();
        MeshQualifierKey qual = record.getQualifierKey();
        MeshDescriptorKey desc = record.getDescriptorKey();

        pmidMap.put(pmid, record);
        descMap.put(desc, record);

        if (qual != null)
            qualMap.put(qual, record);
    }

    @Override public Iterator<HeadingRecord> iterator() {
        return pmidMap.values().iterator();
    }
}
