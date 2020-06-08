
package pubmed.subject;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jam.lang.JamException;
import jam.lang.KeyedObject;

import pubmed.mesh.MeshDB;
import pubmed.mesh.MeshRecordKey;
import pubmed.mesh.MeshRecordName;
import pubmed.mesh.MeshRecordType;
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
    public static final Subject CANCER = CancerSubject.INSTANCE;

    /**
     * Returns the {@code MeSH} record key for this subject.
     *
     * @return the {@code MeSH} record key for this subject
     * (or {@code null} if there is no corresponding record).
     */
    public abstract MeshRecordKey getMeshKey();

    /**
     * Returns the {@code MeSH} record name for this subject.
     *
     * @return the {@code MeSH} record name for this subject.
     * (or {@code null} if there is no corresponding record).
     */
    public abstract MeshRecordName getMeshName();

    /**
     * Returns the type of {@code MeSH} record that corresponds to
     * this subject.
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
     * Returns the (raw) keywords or phrases that may be used to
     * identify this subject.
     *
     * @return the (raw) keywords or phrases that may be used to
     * identify this subject.
     */
    public List<String> getKeywords() {
        MeshRecordKey meshKey = getMeshKey();

        if (meshKey != null) {
            MeshDB.load();
            return MeshDB.record(meshKey).termStrings();
        }
        else {
            return List.of(key);
        }
    }

    /**
     * Returns the {@code MeSH} record key for this subject.
     *
     * @return the {@code MeSH} record key for this subject
     * (an empty string if there is no corresponding record).
     */
    public String getMeshKeyString() {
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
     * (an empty string if there is no corresponding record).
     */
    public String getMeshNameString() {
        MeshRecordName meshName = getMeshName();

        if (meshName != null)
            return meshName.getName();
        else
            return "";
    }
}
