
package pubmed.sql;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import jam.util.ListUtil;

import pubmed.article.PMID;
import pubmed.nlp.LemmaAnnotator;

/**
 * Associates a <em>lemma</em> (base form of a word) with the article
 * in which it occurs.
 */
public final class ArticleLemmaRecord extends ArticleTextJoinRecord {
    private ArticleLemmaRecord(PMID pmid, String lemma) {
        super(pmid, lemma);
    }

    /**
     * Creates a new lemma record with fixed fields.
     *
     * @param pmid the unique identifier of the indexed article.
     *
     * @param lemma the lemma contained in the article.
     *
     * @return the 
     */
    public static ArticleLemmaRecord create(PMID pmid, String lemma) {
        return new ArticleLemmaRecord(pmid, lemma);
    }

    /**
     * Extracts lemmatized nouns from raw text (and article title
     * or abstract) and associates them with the identifier of the
     * article where they occur.
     *
     * @param pmid the identifier of the article.
     *
     * @param text the article text to lemmatize.
     *
     * @return a list of records for each noun in the text.
     */
    public static List<ArticleLemmaRecord> nouns(PMID pmid, String text) {
        //
        // Must eliminate duplicates...
        //
        List<String> lemmaList = LemmaAnnotator.nouns(text);
        Set<String>  lemmaSet  = new TreeSet<String>(lemmaList);

        return ListUtil.apply(lemmaSet, lemma -> create(pmid, lemma));
    }

    /**
     * Returns the lemma indexed by this record.
     *
     * @return the lemma indexed by this record.
     */
    public String getLemma() {
        return key2;
    }
}
