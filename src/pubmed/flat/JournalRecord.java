
package pubmed.flat;

import java.util.List;

import jam.flat.FlatRecord;

import pubmed.article.ISSN;
import pubmed.article.PMID;
import pubmed.article.PubmedJournal;
import pubmed.xml.PubmedArticleElement;

/**
 * Represents a joining record containing the identifier of a
 * {@code PubMed} article and the journal where it appeared.
 */
public final class JournalRecord extends PubmedJoinRecord<ISSN> {
    private final PubmedJournal journal;

    private JournalRecord(PMID pmid, PubmedJournal journal) {
        super(pmid, journal.getISSN());
        this.journal = journal;
    }

    /**
     * Creates a new record with fixed attributes.
     *
     * @param pmid the primary key for the article.
     *
     * @param journal the journal where the article appeared.
     *
     * @return the new journal record.
     */
    public static JournalRecord create(PMID pmid, PubmedJournal journal) {
        return new JournalRecord(pmid, journal);
    }

    /**
     * Creates a new record from a parsed XML article element.
     *
     * @param element a parsed XML article element.
     *
     * @return a new record with data extracted from the given article
     * element ({@code null} if the journal has no ISSN).
     */
    public static JournalRecord from(PubmedArticleElement element) {
        PubmedJournal journal = element.getPubmedJournal();

        if (journal.hasISSN())
            return create(element.getPMID(), journal);
        else
            return null;
    }

    /**
     * Parses a delimited line that encodes a abstract record.
     *
     * @param line the delimited line to parse.
     *
     * @return a new record with the data encoded in the line.
     */
    public static JournalRecord parse(String line) {
        String[] fields = FlatRecord.split(line, 4);

        PMID pmid = PMID.instance(fields[0]);
        ISSN issn = ISSN.instance(fields[1]);
        String title = fields[2];
        String abbrev = fields[3];

        return create(pmid, PubmedJournal.create(issn, title, abbrev));
    }

    /**
     * Returns the journal in which the article was published.
     *
     * @return the journal in which the article was published.
     */
    public PubmedJournal getJournal() {
        return journal;
    }

    @Override public List<String> formatFields() {
        return List.of(format(pmid),
                       format(journal.getISSN()),
                       format(journal.getTitle()),
                       format(journal.getISOAbbreviation()));
    }
}
