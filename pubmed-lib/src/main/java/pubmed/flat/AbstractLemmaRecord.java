
package pubmed.flat;

import jam.flat.FlatRecord;

import pubmed.article.PMID;
import pubmed.nlp.LemmaAnnotator;
import pubmed.nlp.LemmaList;
import pubmed.xml.PubmedArticleElement;

/**
 * Represents a data record containing the identifier and lemmatized
 * abstract of a {@code PubMed} article.
 */
public final class AbstractLemmaRecord extends ArticleLemmaRecord {
    private AbstractLemmaRecord(PMID pmid, LemmaList lemmas) {
        super(pmid, lemmas);
    }

    /**
     * Creates a new record with a fixed identifier and abstract lemmas.
     *
     * @param pmid the unique article identifier.
     *
     * @param lemmas the abstract lemmas.
     *
     * @return a new record for the specified identifier and abstract
     * lemmas.
     */
    public static AbstractLemmaRecord create(PMID pmid, LemmaList lemmas) {
        return new AbstractLemmaRecord(pmid, lemmas);
    }

    /**
     * Creates a new record from a parsed XML article element.
     *
     * @param element a parsed XML article element.
     *
     * @return a new record with data extracted from the given
     * article element.
     */
    public static AbstractLemmaRecord from(PubmedArticleElement element) {
        String abstract_ = element.getAbstract();

        if (abstract_ != null)
            return create(element.getPMID(), LemmaAnnotator.contentWords(abstract_));
        else
            return null;
    }

    /**
     * Parses a delimited line that encodes a abstract lemma record.
     *
     * @param line the delimited line to parse.
     *
     * @return a new record with the data encoded in the line.
     */
    public static AbstractLemmaRecord parse(String line) {
        String[] fields = FlatRecord.split(line, 2);
        return create(parsePMID(fields[0]), LemmaList.parse(fields[1]));
    }

    /**
     * Returns the abstract lemmas for the article.
     *
     * @return the abstract lemmas for the article.
     */
    public LemmaList getAbstractLemmas() {
        return lemmas;
    }
}
