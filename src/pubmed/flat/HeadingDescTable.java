
package pubmed.flat;

import java.io.File;

import jam.flat.JoinTable;

import pubmed.article.PMID;
import pubmed.mesh.MeshDescriptorKey;

/**
 * Maintains a flat table of article title records.
 */
public final class HeadingDescTable extends JoinTable<PMID, MeshDescriptorKey, HeadingDescRecord> {
    /**
     * Creates a new table by parsing a flat file.
     *
     * @param file the flat file containing the table data.
     *
     * @return a new table containing the data in the given
     * flat file.
     */
    public static HeadingDescTable load(File file) {
        HeadingDescTable table = new HeadingDescTable();
        table.parse(file);
        return table;
    }

    @Override public HeadingDescRecord parse(String line) {
        return HeadingDescRecord.parse(line);
    }
}
