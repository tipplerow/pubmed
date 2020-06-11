
package pubmed.nlp;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.CoreDocument;

import jam.util.ListUtil;
import jam.util.TargetList;

/**
 * Represents an immutable sequence of lemmas extracted from text.
 */
public final class LemmaList extends AbstractList<String> {
    private int hashCode = 0;
    private final TargetList<String> lemmas;

    private LemmaList(List<String> lemmas) {
        this.lemmas = TargetList.create(lemmas);
    }

    /**
     * The default delimiter to be used when joining the lemmas into a
     * single string.
     */
    public static final String DELIM = " ";

    /**
     * A sharable empty list.
     */
    public static final LemmaList EMPTY = create();

    /**
     * Creates a new lemma list.
     *
     * @param lemmas the lemmas in the list.
     *
     * @return the new lemma list.
     */
    public static LemmaList create(String... lemmas) {
        return create(List.of(lemmas));
    }

    /**
     * Creates a new lemma list.
     *
     * @param lemmas the lemmas in the list.
     *
     * @return the new lemma list.
     */
    public static LemmaList create(List<String> lemmas) {
        return new LemmaList(lemmas);
    }

    /**
     * Determines whether this list contains a sequence of lemmas (as
     * a sublist of this list).
     *
     * <p>This method executes in <em>amortized</em> {@code O(n)} time,
     * where {@code n} is the length of the input list.
     *
     * @param sequence the lemma sequence to search for.
     *
     * @return {@code true} iff the specified list is a sublist within
     * this list.
     */
    public boolean containsSequence(List<String> sequence) {
        return lemmas.containsSequence(sequence);
    }

    /**
     * Counts the number of times a lemma occurs in this list.
     *
     * <p>This method executes in <em>amortized constant time</em>.
     *
     * @param lemma the lemma to count.
     *
     * @return the number of times the specified lemma occurs in this
     * list.
     */
    public int count(String lemma) {
        return lemmas.count(lemma);
    }

    /**
     * Counts the number of times a sequence of lemmas occurs in this
     * list.
     *
     * <p>This method executes in <em>amortized constant time</em>.
     *
     * @param sequence the lemma sequence to count.
     *
     * @return the number of times the specified lemma sequence occurs
     * in this list.
     */
    public int countSequence(List<String> sequence) {
        return lemmas.countSequence(sequence);
    }

    /**
     * Constructs a single string composed of the lemmas in this list.
     *
     * @return a single string composed of the lemmas in this list.
     */
    public String join() {
        return String.join(DELIM, lemmas);
    }

    /**
     * Determines whether this list contains a specific lemma.
     *
     * <p>This method executes in <em>amortized constant time</em>.
     *
     * @param lemma the lemma to search for.
     *
     * @return {@code true} iff this list contains the specified
     * lemma.
     */
    @Override public boolean contains(Object lemma) {
        return lemmas.contains(lemma);
    }

    @Override public String get(int index) {
        return lemmas.get(index);
    }

    @Override public int hashCode() {
        if (hashCode == 0)
            hashCode = lemmas.hashCode();

        return hashCode;
    }

    @Override public int size() {
        return lemmas.size();
    }
}
