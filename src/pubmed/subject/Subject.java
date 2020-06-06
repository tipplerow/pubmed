
package pubmed.subject;

import java.util.List;

import pubmed.mesh.MeshDB;
import pubmed.mesh.MeshRecordKey;
import pubmed.mesh.MeshRecordName;
import pubmed.mesh.MeshRecordType;

/**
 * Provides a common interface for the subjects or topics discussed in
 * articles.
 */
public interface Subject {
    /**
     * Returns the canonical name of this subject.
     *
     * @return the canonical name of this subject.
     */
    public abstract String getName();

    /**
     * Returns keywords that may be used to identify this subject.
     *
     * @return keywords that may be used to identify this subject.
     */
    public default List<String> getKeywords() {
        MeshRecordKey meshKey = getMeshKey();

        if (meshKey != null) {
            MeshDB.load();
            return MeshDB.record(meshKey).termStrings();
        }
        else {
            return List.of(getName());
        }
    }

    /**
     * Returns the {@code MeSH} record key for this subject.
     *
     * @return the {@code MeSH} record key for this subject
     * ({@code null} if there is no corresponding record).
     */
    public abstract MeshRecordKey getMeshKey();

    /**
     * Returns the {@code MeSH} record key for this subject.
     *
     * @return the {@code MeSH} record key for this subject
     * (an empty string if there is no corresponding record).
     */
    public default String getMeshKeyString() {
        MeshRecordKey meshKey = getMeshKey();
        
        if (meshKey != null)
            return meshKey.getKey();
        else
            return "";
    }

    /**
     * Returns the {@code MeSH} record name for this subject.
     *
     * @return the {@code MeSH} record name for this subject.
     * ({@code null} if there is no corresponding record).
     */
    public abstract MeshRecordName getMeshName();

    /**
     * Returns the {@code MeSH} record name for this subject.
     *
     * @return the {@code MeSH} record name for this subject.
     * (an empty string if there is no corresponding record).
     */
    public default String getMeshNameString() {
        MeshRecordName meshName = getMeshName();

        if (meshName != null)
            return meshName.getName();
        else
            return "";
    }

    /**
     * Returns the type of {@code MeSH} record that corresponds to
     * this subject (or {@code null} if there is no corresponding
     * record).
     *
     * @return the type of {@code MeSH} record that corresponds to
     * this subject (or {@code null} if there is no corresponding
     * record).
     */
    public abstract MeshRecordType getMeshType();

    /**
     * Specifies whether this subject is a chemical substance with
     * a corresponding {@code MeSH} record that may appear in the
     * chemical substance list of an article.
     *
     * @return {@code true} iff this subject is a chemical substance
     * with a corresponding {@code MeSH} record.
     */
    public abstract boolean isChemical();
}
