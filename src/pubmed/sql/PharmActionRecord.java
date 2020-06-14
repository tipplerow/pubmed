
package pubmed.sql;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import jam.sql.SQLPairRecord;

import pubmed.mesh.MeshDB;
import pubmed.mesh.MeshDescriptorKey;
import pubmed.mesh.MeshPharmAction;
import pubmed.mesh.MeshRecordKey;

/**
 * Represents a row in the {@code pharm_actions} table.
 */
public final class PharmActionRecord
    extends SQLPairRecord<MeshRecordKey, MeshDescriptorKey>
    implements Comparable<PharmActionRecord>, PubmedBulkRecord {

    private PharmActionRecord(MeshRecordKey substanceKey, MeshDescriptorKey actionKey) {
        super(substanceKey, actionKey);
    }

    /**
     * Creates a new pharmacological action record for a
     * substance and its action.
     *
     * @param substanceKey the {@code MeshRecordKey} for the
     * substance.
     *
     * @param actionKey the {@code MeshDescriptorKey} for the
     * action.
     *
     * @return the {@code pubmed.pharm_actions} record with the
     * specified fields.
     */
    public static PharmActionRecord create(MeshRecordKey substanceKey, MeshDescriptorKey actionKey) {
        return new PharmActionRecord(substanceKey, actionKey);
    }

    /**
     * Creates records for all pharmacological actions contained in
     * the {@code MeSH} database.
     *
     * @return a list of {@code pubmed.pharm_actions} records for the
     * pharmacological actions contained in the {@code MeSH} database.
     */
    public static List<PharmActionRecord> list() {
        List<PharmActionRecord> records =
            new ArrayList<PharmActionRecord>();

        Collection<MeshRecordKey> substanceKeys =
            MeshPharmAction.allActors();

        for (MeshRecordKey substanceKey : substanceKeys) {
            Collection<MeshPharmAction> actions =
                MeshPharmAction.viewActions(substanceKey);

            for (MeshPharmAction action : actions)
                records.add(create(substanceKey, action.getKey()));
        }

        records.sort(null);
        return records;
    }

    /**
     * Returns the {@code MeshRecordKey} for the substance that
     * performs this pharmacological action.
     *
     * @return the {@code MeshRecordKey} for the substance that
     * performs this pharmacological action.
     */
    public MeshRecordKey getSubstanceKey() {
        return key1;
    }

    /**
     * Returns the {@code MeshDescriptorKey} of the pharmacological
     * action performed by the substance.
     *
     * @return the {@code MeshDescriptorKey} of the pharmacological
     * action performed by the substance.
     */
    public MeshDescriptorKey getActionKey() {
        return key2;
    }

    @Override public int compareTo(PharmActionRecord that) {
        //
        // The substance keys may be MeshDescriptorKeys or
        // MeshSupplementalKeys, so they are not directly
        // comparable: compare their key strings instead...
        //
        int cmp = this.key1.getKey().compareTo(that.key1.getKey());

        if (cmp != 0)
            return cmp;
        else
            return this.getActionKey().compareTo(that.getActionKey());
    }

    @Override public String formatBulk() {
        return joinBulk(formatBulk(getSubstanceKey()), formatBulk(getActionKey()));
    }
}
