
package pubmed.flat;

import java.util.List;

import pubmed.article.PMID;
import pubmed.nlp.LemmaList;

/**
 * Represents a data record containing the identifier of a
 * {@code PubMed} article and a list of lemmas (content words).
 */
public abstract class ArticleLemmaRecord extends PubmedFlatRecord {
    /**
     * The article lemmas.
     */
    protected final LemmaList lemmas;

    /**
     * Creates a new record with a fixed key and lemma list.
     *
     * @param pmid the primary key for the record.
     *
     * @param lemmas the lemmas (content words) for the record.
     */
    protected ArticleLemmaRecord(PMID pmid, LemmaList lemmas) {
        super(pmid);
        this.lemmas = lemmas;
    }

    /**
     * Returns the lemmas in the article.
     *
     * @return the lemmas in the article.
     */
    public LemmaList getLemmaList() {
        return lemmas;
    }

    @Override public boolean equalsData(Object record) {
        ArticleLemmaRecord that = (ArticleLemmaRecord) record;
        return this.lemmas.equals(that.lemmas);
    }

    @Override public List<String> formatFields() {
        return List.of(format(pmid), format(lemmas));
    }
}
