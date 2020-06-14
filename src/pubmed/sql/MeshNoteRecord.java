
package pubmed.sql;

import jam.sql.BulkRecord;

import pubmed.mesh.MeshRecord;
import pubmed.mesh.MeshRecordKey;

/**
 * Represents a row in the {@code mesh_notes} table, which contains
 * the key of a {@code MeSH} record and the note or scope note that
 * accompanied the record in the XML file.
 */
public final class MeshNoteRecord implements BulkRecord {
    private final MeshRecordKey key;
    private final String note;

    private MeshNoteRecord(MeshRecordKey key, String note) {
        this.key = key;
        this.note = note;
    }

    /**
     * Creates the {@code pubmed.mesh_notes} record for a
     * {@code MeSH} record.
     *
     * @param record the {@code MeSH} record to store.
     *
     * @return the {@code pubmed.mesh_notes} record for the
     * specified record.
     */
    public static MeshNoteRecord create(MeshRecord record) {
        return create(record.getKey(), record.getNote());
    }

    /**
     * Creates a new {@code pubmed.mesh_notes} record from its
     * fields.
     *
     * @param key the key of the {@code MeSH} record.
     *
     * @param note the note that accompanies the record.
     *
     * @return the {@code pubmed.mesh_notes} record with the
     * specified fields.
     */
    public static MeshNoteRecord create(MeshRecordKey key, String note) {
        return new MeshNoteRecord(key, note);
    }

    /**
     * Returns the key of the {@code MeSH} record.
     *
     * @return the key of the {@code MeSH} record.
     */
    public MeshRecordKey getKey() {
        return key;
    }

    /**
     * Returns the key of the {@code MeSH} record.
     *
     * @return the key of the {@code MeSH} record.
     */
    public String getKeyString() {
        return key.getKey();
    }

    /**
     * Returns the note that accompanies the {@code MeSH} record.
     *
     * @return the note that accompanies the {@code MeSH} record.
     */
    public String getNote() {
        return note;
    }

    @Override public String formatBulk() {
        return joinBulk(formatBulk(key), formatBulk(note));
    }
}
