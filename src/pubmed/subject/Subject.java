
package pubmed.subject;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jam.lang.JamException;
import jam.lang.KeyedObject;

import pubmed.mesh.MeshDescriptor;
import pubmed.mesh.MeshRecord;
import pubmed.mesh.MeshTreeCategory;
import pubmed.mesh.MeshTreeNumber;
import pubmed.mesh.MeshTreeNumberList;
import pubmed.mesh.MeshTreeRecord;
import pubmed.nlp.LemmaList;

/**
 * Provides a base class for the subjects or topics discussed in
 * {@code pubmed} articles.
 */
public abstract class Subject extends KeyedObject<String> {
    private List<LemmaList> keywordLemmas = null;

    // Registry of all unique subjects indexed by key...
    private static final Map<String, Subject> subjects = new HashMap<String, Subject>();

    /**
     * Creates a new subject with a unique key.
     *
     * @param key the unique key for the subject.
     *
     * @throws RuntimeException unless the key is unique.
     */
    protected Subject(String key) {
        super(key);
        register();
    }

    private void register() {
        if (subjects.containsKey(key))
            throw JamException.runtime("Duplicate subject key: [%s]", key);
        else
            subjects.put(key, this);
    }

    /**
     * The <em>cancer</em> subject.
     */
    public static final Subject CANCER = MeshSubject.create("D009369");

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
        if (keywordLemmas == null)
            keywordLemmas = Collections.unmodifiableList(lemmatizeKeywords());

        return keywordLemmas;
    }

    private List<LemmaList> lemmatizeKeywords() {
        return LemmaList.contentWords(getKeywords());
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
     * Specifies whether this subject is a chemical substance with
     * a corresponding {@code MeSH} record that may appear in the
     * chemical substance list of an article.
     *
     * @return {@code true} iff this subject is a chemical substance
     * with a corresponding {@code MeSH} record.
     */
    public boolean isChemical() {
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
}
