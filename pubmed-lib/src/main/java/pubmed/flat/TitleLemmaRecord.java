
package pubmed.flat;

import jam.flat.FlatRecord;

import pubmed.article.PMID;
import pubmed.nlp.LemmaAnnotator;
import pubmed.nlp.LemmaList;
import pubmed.xml.PubmedArticleElement;

/**
 * Represents a data record containing the identifier and lemmatized
 * title of a {@code PubMed} article.
 */
public final class TitleLemmaRecord extends ArticleLemmaRecord {
    private TitleLemmaRecord(PMID pmid, LemmaList lemmas) {
        super(pmid, lemmas);
    }

    /**
     * Creates a new record with a fixed identifier and title lemmas.
     *
     * @param pmid the unique article identifier.
     *
     * @param lemmas the title lemmas.
     *
     * @return a new record for the specified identifier and title
     * lemmas.
     */
    public static TitleLemmaRecord create(PMID pmid, LemmaList lemmas) {
        return new TitleLemmaRecord(pmid, lemmas);
    }

    /**
     * Creates a new record from a parsed XML article element.
     *
     * @param element a parsed XML article element.
     *
     * @return a new record with data extracted from the given
     * article element.
     */
    public static TitleLemmaRecord from(PubmedArticleElement element) {
        return create(element.getPMID(), LemmaAnnotator.contentWords(element.getArticleTitle()));
    }

    /**
     * Parses a delimited line that encodes a title lemma record.
     *
     * @param line the delimited line to parse.
     *
     * @return a new record with the data encoded in the line.
     */
    public static TitleLemmaRecord parse(String line) {
        String[] fields = FlatRecord.split(line, 2);
        return create(parsePMID(fields[0]), LemmaList.parse(fields[1]));
    }

    /**
     * Returns the title lemmas for the article.
     *
     * @return the title lemmas for the article.
     */
    public LemmaList getTitleLemmas() {
        return lemmas;
    }
}
