
package pubmed.subject;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jam.lang.JamException;
import jam.lang.KeyedObject;

import pubmed.mesh.MeshDescriptor;
import pubmed.mesh.MeshRecord;
import pubmed.mesh.MeshTreeCategory;
import pubmed.mesh.MeshTreeNumber;
import pubmed.mesh.MeshTreeNumberList;
import pubmed.mesh.MeshTreeRecord;
import pubmed.nlp.LemmaAnnotator;
import pubmed.nlp.LemmaList;

/**
 * Provides a base class for the subjects or topics discussed in
 * {@code pubmed} articles.
 */
public abstract class Subject extends KeyedObject<String> {
    private final String name;

    // Registry of all unique subjects indexed by key and name...
    private static final Map<String, Subject> registry = new HashMap<String, Subject>();

    /**
     * Creates a new subject with a unique key and name.
     *
     * @param key the unique key for the subject.
     *
     * @param name the unique name for the subject.
     *
     * @throws RuntimeException unless the key and name are unique.
     */
    protected Subject(String key, String name) {
        super(key);
        this.name = name;
        register();
    }

    private void register() {
        // The key must be unique...
        register(key);

        // The name may be the same as the key, but if not, it must be
        // unique...
        if (!name.equals(key))
            register(name);
    }

    private void register(String identifier) {
        if (registry.containsKey(identifier))
            throw JamException.runtime("Duplicate subject identifier: [%s]", identifier);
        else
            registry.put(identifier, this);
    }

    /**
     * Retrieves the subject having a specific key or name.
     *
     * @param identifier the unique key or name of a subject.
     *
     * @return the subject with the specified identifier (or
     * {@code null} if there is no matching subject).
     */
    public static Subject instance(String identifier) {
        return registry.get(identifier);
    }

    /**
     * Returns the {@code MeSH} record for this subject.
     *
     * @return the {@code MeSH} record for this subject (or
     * {@code null} if there is no corresponding record).
     */
    public abstract MeshRecord getMeshRecord();

    /**
     * Returns the {@code MeSH} descriptor for this subject (or
     * {@code null} if there is no corresponding descriptor).
     *
     * @return the {@code MeSH} descriptor for this subject (or
     * {@code null} if there is no corresponding descriptor).
     */
    public MeshDescriptor getDescriptor() {
        MeshRecord record = getMeshRecord();

        if (record != null && record.isDescriptor())
            return (MeshDescriptor) record;
        else
            return null;
    }

    /**
     * Returns the (raw) keywords or phrases that may be used to
     * identify this subject.
     *
     * @return the (raw) keywords or phrases that may be used to
     * identify this subject.
     */
    public List<String> getKeywords() {
        MeshRecord record = getMeshRecord();

        if (record != null)
            return record.termStrings();
        else
            return List.of(key);
    }

    /**
     * Returns lemmatized keywords or phrases that may be used to
     * identify this subject.
     *
     * @return lemmatized keywords or phrases that may be used to
     * identify this subject.
     */
    public List<LemmaList> getKeywordLemmas() {
        MeshRecord record = getMeshRecord();

        if (record != null)
            return record.termLemmas();
        else
            return List.of(LemmaAnnotator.contentWords(key));
    }

    /**
     * Returns the {@code MeSH} tree numbers associated with this
     * subject.
     *
     * @return the {@code MeSH} tree numbers associated with this
     * subject (an empty list unless this subject has a descriptor
     * or qualifier record).
     */
    public MeshTreeNumberList getMeshTreeNumbers() {
        MeshRecord record = getMeshRecord();

        if (record != null && (record instanceof MeshTreeRecord))
            return ((MeshTreeRecord) record).getNumberList();
        else
            return MeshTreeNumberList.EMPTY;
    }

    /**
     * Returns the name of this subject.
     *
     * @return the name of this subject.
     */
    public String getName() {
        return name;
    }

    /**
     * Specifies whether this subject is a chemical substance with
     * a corresponding {@code MeSH} record that may appear in the
     * chemical substance list of an article.
     *
     * @return {@code true} iff this subject is a chemical substance
     * with a corresponding {@code MeSH} record.
     */
    public boolean isChemical() {
        return isSupplemental() || (isDescriptor() && hasChemicalTreeNumber());
    }

    private boolean hasChemicalTreeNumber() {
        MeshTreeNumberList treeNumbers = getMeshTreeNumbers();

        for (MeshTreeNumber treeNumber : treeNumbers)
            if (treeNumber.getCategory().equals(MeshTreeCategory.Chemicals_and_Drugs))
                return true;

        return false;
    }

    /**
     * Specifies whether this subject has a corresponding {@code MeSH}
     * descriptor record that may appear in the heading list of an
     * article.
     *
     * @return {@code true} iff this subject has a corresponding
     * {@code MeSH} descriptor record.
     */
    public boolean isDescriptor() {
        MeshRecord record = getMeshRecord();
        return record != null && record.isDescriptor();
    }

    /**
     * Specifies whether this subject has a corresponding {@code MeSH}
     * qualifier record that may appear in the heading list of an
     * article.
     *
     * @return {@code true} iff this subject has a corresponding
     * {@code MeSH} qualifier record.
     */
    public boolean isQualifier() {
        MeshRecord record = getMeshRecord();
        return record != null && record.isQualifier();
    }

    /**
     * Specifies whether this subject has a corresponding {@code MeSH}
     * supplemental record that may appear in the heading list of an
     * article.
     *
     * @return {@code true} iff this subject has a corresponding
     * {@code MeSH} supplemental record.
     */
    public boolean isSupplemental() {
        MeshRecord record = getMeshRecord();
        return record != null && record.isSupplemental();
    }

    @Override public String toString() {
        return String.format("%s(%s)", name, key);
    }
}
