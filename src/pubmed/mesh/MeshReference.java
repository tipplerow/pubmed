
package pubmed.mesh;

import java.util.List;

/**
 * Provides a common interface for substances, targets, cancer types,
 * and cancer hallmarks that are mapped to a {@code MeSH} descriptor
 * or supplemental record.
 */
public interface MeshReference {
    /**
     * Returns the {@code MeSH} record key for this object.
     *
     * @return the {@code MeSH} record key for this object.
     */
    public abstract MeshRecordKey getMeshKey();

    /**
     * Returns the {@code MeSH} record key for this object.
     *
     * @return the {@code MeSH} record key for this object.
     */
    public default String getMeshKeyString() {
        MeshRecordKey meshKey = getMeshKey();
        
        if (meshKey != null)
            return meshKey.getKey();
        else
            return "";
    }

    /**
     * Returns the {@code MeSH} record name for this object.
     *
     * @return the {@code MeSH} record name for this object.
     */
    public abstract MeshRecordName getMeshName();

    /**
     * Returns the {@code MeSH} record name for this object.
     *
     * @return the {@code MeSH} record name for this object.
     */
    public default String getMeshNameString() {
        MeshRecordName meshName = getMeshName();

        if (meshName != null)
            return meshName.getName();
        else
            return "";
    }

    /**
     * Returns the terms associated with the {@code MeSH} record
     * mapped to this object.
     *
     * @return the terms associated with the {@code MeSH} record
     * mapped to this object.
     */
    public default MeshTermList getMeshTermList() {
        MeshRecordKey meshKey = getMeshKey();

        if (meshKey != null) {
            MeshDB.load();
            return MeshDB.record(meshKey).termStrings();
        }

        String longName = getLongName();
        String shortName = getShortName();

        if (shortName.equals(longName))
            return MeshTermList.create(List.of(shortName));
        else
            return MeshTermList.create(List.of(shortName, longName));
    }

    /**
     * Returns the short name of this object.
     *
     * @return the short name of this object.
     */
    public abstract String getShortName();

    /**
     * Returns the long name of this object.
     *
     * @return the long name of this object.
     */
    public abstract String getLongName();

    /**
     * Returns the name required to create this object instance.
     *
     * @return the name required to create this object instance.
     */
    public abstract String javaName();
}
