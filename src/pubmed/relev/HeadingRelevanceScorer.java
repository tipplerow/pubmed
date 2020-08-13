
package pubmed.relev;

import pubmed.article.PMID;
import pubmed.bulk.BulkFile;
import pubmed.flat.HeadingTable;
import pubmed.subject.Subject;

/**
 * Computes heading relevance score records.
 *
 * <p>If the subject maps to a {@code MeSH} descriptor record and the
 * article has a {@code MeSH} heading list, the score is {@code +1} if
 * the heading list contains the subject record, {@code -1} if it does
 * not.  The score is {@code 0} if the subject does not map to a MeSH
 * descriptor record or if the article does not have a heading list.
 */
public final class HeadingRelevanceScorer implements RelevanceScorer {
    private final HeadingTable table;

    private HeadingRelevanceScorer(HeadingTable table) {
        this.table = table;
    }

    /**
     * Returns the relevance scorer for a given bulk XML file.
     *
     * @param bulkFile the bulk XML file to process.
     *
     * @return the relevance scorer for the given bulk XML file.
     */
    public static HeadingRelevanceScorer instance(BulkFile bulkFile) {
        return instance(bulkFile.getHeadingFile().load());
    }

    /**
     * Returns the relevance scorer for a table of heading records.
     *
     * @param table heading records derived from a bulk XML file.
     *
     * @return the relevance scorer for the given lemma table.
     */
    public static HeadingRelevanceScorer instance(HeadingTable table) {
        return new HeadingRelevanceScorer(table);
    }

    @Override public int computeScore(PMID pmid, Subject subject) {
        if (!subject.isDescriptor())
            return 0;

        if (!table.contains(pmid))
            return 0;

        if (table.contains(pmid, subject.getDescriptorKey()))
            return +1;
        else
            return -1;
    }
}
