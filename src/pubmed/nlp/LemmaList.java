
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
     * Creates a new lemma list by annotating raw text and retaining
     * only the <em>content words</em> (nouns, verbs, and adjectives).
     *
     * @param text the raw text to lemmatize.
     *
     * @return a new lemma list containing the lemmatized content
     * words in the specified text.
     */
    public static LemmaList contentWords(String text) {
        return contentWords(LemmaAnnotator.annotate(text));
    }

    /**
     * Creates new lemma lists by annotating raw text and retaining
     * only the <em>content words</em> (nouns, verbs, and adjectives).
     *
     * @param strings the raw text to lemmatize.
     *
     * @return new lemma lists containing the lemmatized content words
     * in the specified strings.
     */
    public static List<LemmaList> contentWords(Collection<String> strings) {
        return ListUtil.apply(strings, text -> contentWords(text));
    }

    /**
     * Creates a new lemma list by retaining only the <em>content
     * words</em> (nouns, verbs, and adjectives) from an annotated
     * document.
     *
     * @param document an annotated document.
     *
     * @return a new lemma list containing the lemmatized content
     * words in the specified document.
     */
    public static LemmaList contentWords(CoreDocument document) {
        List<String> lemmas = new ArrayList<String>();

        for (CoreLabel token : document.tokens())
            if (Token.isContentWord(token))
                lemmas.add(Token.lemma(token));

        return create(lemmas);
    }

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
     * @param subList the lemma sequence to search for.
     *
     * @return {@code true} iff the specified list is a sublist within
     * this list.
     */
    public boolean contains(List<String> subList) {
        return lemmas.contains(subList);
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
