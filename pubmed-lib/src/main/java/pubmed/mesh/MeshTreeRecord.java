
package pubmed.mesh;

/**
 * Represents records contained in the {@code MeSH} tree structure.
 */
public abstract class MeshTreeRecord extends MeshRecord {
    private final MeshTreeNumberList numbers;

    /**
     * Creates a new {@code MeSH} tree record with a fixed list of
     * tree numbers.
     *
     * @param key the unique key for the record.
     *
     * @param name the common name for the record.
     *
     * @param note the note associated with the record in the XML file.
     *
     * @param terms the terms associated with the record.
     *
     * @param numbers the {@code MeSH} tree numbers associated with
     * the record.
     */
    protected MeshTreeRecord(MeshRecordKey      key,
                             MeshRecordName     name,
                             String             note,
                             MeshTermList       terms,
                             MeshTreeNumberList numbers) {
        super(key, name, note, terms);
        this.numbers = numbers;
    }

    /**
     * Returns the enumerated type of this record.
     *
     * @return the enumerated type of this record.
     */
    public MeshTreeNumberList getNumberList() {
        return numbers;
    }

    /**
     * Determines if this record describes a related (on the same
     * branch) but broader concept (as represented by another tree
     * number).
     *
     * @param reference the reference tree number.
     *
     * @return {@code true} iff any of the tree numbers associated
     * with this record are broader than the reference number.
     */
    public boolean isBroader(MeshTreeNumber reference) {
        for (MeshTreeNumber number : numbers)
            if (number.isBroader(reference))
                return true;

        return false;
    }

    /**
     * Determines if this record describes a related (on the same
     * branch) but narrower concept (as represented by another tree
     * number).
     *
     * @param reference the reference tree number.
     *
     * @return {@code true} iff any of the tree numbers associated
     * with this record are narrower than the reference number.
     */
    public boolean isNarrower(MeshTreeNumber reference) {
        for (MeshTreeNumber number : numbers)
            if (number.isNarrower(reference))
                return true;

        return false;
    }

    @Override public boolean equals(Object obj) {
        return this.getClass().equals(obj.getClass()) && equalsRecord((MeshTreeRecord) obj);
    }

    private boolean equalsRecord(MeshTreeRecord that) {
        return super.equals(that) && this.numbers.equals(that.numbers);
    }
}
