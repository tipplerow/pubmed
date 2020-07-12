
package pubmed.flat;

import jam.flat.FlatRecord;

import pubmed.article.PMID;
import pubmed.mesh.MeshRecordKey;
import pubmed.nlp.LemmaList;

/**
 * A data record that may be written to a delimited flat file.
 */
public interface FlatRecordBase {
    /**
     * Formats a (possibly {@code null} or empty) lemma list.
     *
     * @param lemmas the (possibly {@code null} or empty) lemmas.
     *
     * @return the string value of the lemma list, if it is not
     * {@code null} or empty, otherwise the {@code NULL_STRING}.
     */
    public default String format(LemmaList lemmas) {
        return (lemmas != null && !lemmas.isEmpty()) ? lemmas.join() : FlatRecord.NULL_STRING;
    }

    /**
     * Formats a (possibly {@code null}) record key.
     *
     * @param key the (possibly {@code null}) record key.
     *
     * @return the string value of the record key, if it is not
     * {@code null}, otherwise the {@code NULL_STRING}.
     */
    public default String format(MeshRecordKey key) {
        return (key != null) ? key.getKey() : FlatRecord.NULL_STRING;
    }

    /**
     * Formats a (possibly {@code null}) article identifier.
     *
     * @param pmid the (possibly {@code null}) article identifier.
     *
     * @return the string value of the identifier, if it is not
     * {@code null}, otherwise the {@code NULL_STRING}.
     */
    public default String format(PMID pmid) {
        return (pmid != null) ? Integer.toString(pmid.intValue()) : FlatRecord.NULL_STRING;
    }
}
