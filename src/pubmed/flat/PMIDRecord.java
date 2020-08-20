
package pubmed.flat;

import java.util.List;

import jam.flat.FlatRecord;

import pubmed.article.PMID;

/**
 * Represents a single unique article identifier as a standalone data
 * record.
 */
public final class PMIDRecord extends PubmedFlatRecord {
    private PMIDRecord(PMID pmid) {
        super(pmid);
    }

    /**
     * Creates a new record with a fixed identifier.
     *
     * @param pmid the unique article identifier.
     *
     * @return a new record with the specified identifier.
     */
    public static PMIDRecord create(PMID pmid) {
        return new PMIDRecord(pmid);
    }

    /**
     * Parses a line that encodes a PMID record.
     *
     * @param line the delimited line to parse.
     *
     * @return a new record with the data encoded in the line.
     */
    public static PMIDRecord parse(String line) {
        String[] fields = FlatRecord.split(line, 1);
        return create(parsePMID(fields[0]));
    }

    @Override public boolean equalsData(Object record) {
        return true; // No non-key data...
    }

    @Override public List<String> formatFields() {
        return List.of(format(pmid));
    }
}
