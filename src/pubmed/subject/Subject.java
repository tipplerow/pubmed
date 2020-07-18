
package pubmed.subject;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jam.lang.JamException;
import jam.lang.KeyedObject;
import jam.util.UniqueList;

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
    // Names for the subject: the first serves as the primary name...
    private final List<String> names = UniqueList.create();

    // Keywords, assembled on demand...
    private List<String> keywords = null;

    // Lemmatized keywords, computed on demand...
    private List<LemmaList> keywordLemmas = null;

    // Registry of all unique subjects indexed by key...
    private static final Map<String, Subject> registry = new HashMap<String, Subject>();

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
        if (registry.containsKey(key))
            throw JamException.runtime("Duplicate subject key: [%s]", key);
        else
            registry.put(key, this);
    }

    /**
     * Identifies existing subjects.
     *
     * @param key the unique key of a subject.
     *
     * @return {@code true} iff a subject with the specified key has
     * been created.
     */
    public static boolean exists(String key) {
        return registry.containsKey(key);
    }

    /**
     * Retrieves the subject having a specific key.
     *
     * @param key the unique key of a subject.
     *
     * @return the subject with the specified key (or
     * {@code null} if there is no matching subject).
     */
    public static Subject instance(String key) {
        return registry.get(key);
    }

    /**
     * Adds a name for this subject. The first name added will serve
     * as the primary name; {@code null} and duplicate names will be
     * ignored.
     *
     * @param name the name to add.
     */
    protected void addName(String name) {
        if (name != null)
            names.add(name);
    }

    /**
     * Returns the {@code MeSH} record for this subject.
     *
     * @return the {@code MeSH} record for this subject (or
     * {@code null} if there is no corresponding record).
     */
    public abstract MeshRecord getMeshRecord();

    /**
     * Returns the prefix used to create database table names for
     * articles related to this subject.
     *
     * @return the prefix used to create database table names for
     * articles related to this subject.
     */
    public abstract String getTablePrefix();

    /**
     * Returns the suffix used to create database table names for
     * articles related to this subject.
     *
     * @return the suffix used to create database table names for
     * articles related to this subject ({@code null} if no suffix
     * is necessary).
     */
    public abstract String getTableSuffix();

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
        if (keywords == null)
            assembleKeywords();

        return keywords;
    }

    private void assembleKeywords() {
        keywords = UniqueList.create();
        MeshRecord record = getMeshRecord();

        if (record != null)
            keywords.addAll(record.termStrings());
        else
            keywords.add(key);

        keywords.addAll(names);
        keywords = Collections.unmodifiableList(keywords);
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
            lemmatizeKeywords();

        return keywordLemmas;
    }

    private void lemmatizeKeywords() {
        keywordLemmas = LemmaAnnotator.contentWords(getKeywords());
        keywordLemmas = UniqueList.create(keywordLemmas);
        keywordLemmas = Collections.unmodifiableList(keywordLemmas);
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
     * Returns the names assigned to this subject.
     *
     * @return the names assigned to this subject.
     */
    public List<String> getNames() {
        return Collections.unmodifiableList(names);
    }

    /**
     * Returns the primary name for this subject.
     *
     * @return the primary name for this subject.
     */
    public String getPrimaryName() {
        if (names.isEmpty())
            return getKey();
        else
            return names.get(0);
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
        return String.format("%s(%s)", getClass().getSimpleName(), key);
    }
}
