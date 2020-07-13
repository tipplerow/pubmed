
package pubmed.flat;

import java.util.List;

import jam.flat.FlatRecord;
import jam.util.ListUtil;

import pubmed.article.PMID;
import pubmed.nlp.LemmaAnnotator;
import pubmed.xml.PubmedArticleElement;

/**
 * Represents a joining record containing the identifier for an
 * article and a lemmatized keyword or phrase contained in that
 * article.
 */
public final class KeywordRecord extends PubmedJoinRecord<String> {
    private KeywordRecord(PMID pmid, String keyword) {
        super(pmid, keyword);
    }

    /**
     * Creates a new record with a fixed identifier and keyword.
     *
     * @param pmid the article identifier.
     *
     * @param keyword the lemmatized keyword or phrase.
     *
     * @return a new record for the specified identifier and keyword.
     */
    public static KeywordRecord create(PMID pmid, String keyword) {
        return new KeywordRecord(pmid, keyword);
    }

    /**
     * Extracts the keyword records from a parsed XML article element.
     *
     * @param element a parsed XML article element.
     *
     * @return the keyword records contained in the article element.
     */
    public static List<KeywordRecord> from(PubmedArticleElement element) {
        List<String> keywordList = element.getKeywordList();

        if (keywordList.isEmpty())
            return List.of();
        else
            return ListUtil.apply(keywordList, keyword -> create(element.getPMID(), lemmatize(keyword)));
    }

    private static String lemmatize(String keyword) {
        return LemmaAnnotator.contentWords(keyword).join();
    }

    /**
     * Parses a delimited line that encodes a keyword record.
     *
     * @param line the delimited line to parse.
     *
     * @return a new record with the data encoded in the line.
     */
    public static KeywordRecord parse(String line) {
        String[] fields = FlatRecord.split(line, 2);
        return create(parsePMID(fields[0]), FlatRecord.parseString(fields[1]));
    }

    /**
     * Returns the article keyword.
     *
     * @return the article keyword.
     */
    public String getKeyword() {
        return fkey;
    }

    @Override public List<String> formatFields() {
        return List.of(format(pmid), format(fkey));
    }
}
