
package pubmed.relev;

import pubmed.article.PMID;
import pubmed.bulk.BulkFile;
import pubmed.flat.ChemicalTable;
import pubmed.subject.Subject;

/**
 * Computes chemical relevance score records.
 *
 * <p>If the subject maps to a {@code MeSH} chemical record and the
 * article has a chemical list, the score is {@code +1} if the list
 * contains the subject record, {@code -1} if it does not.  The score
 * is {@code 0} if the subject does not map to a {@code MeSH} chemical
 * record or if the article does not have a chemical list.
 */
public final class ChemicalRelevanceScorer implements RelevanceScorer {
    private final ChemicalTable table;

    private ChemicalRelevanceScorer(ChemicalTable table) {
        this.table = table;
    }

    /**
     * Returns the relevance scorer for a given bulk XML file.
     *
     * @param bulkFile the bulk XML file to process.
     *
     * @return the relevance scorer for the given bulk XML file.
     */
    public static ChemicalRelevanceScorer instance(BulkFile bulkFile) {
        return instance(bulkFile.getChemicalFile().load());
    }

    /**
     * Returns the relevance scorer for a table of chemical records.
     *
     * @param table chemical records derived from a bulk XML file.
     *
     * @return the relevance scorer for the given lemma table.
     */
    public static ChemicalRelevanceScorer instance(ChemicalTable table) {
        return new ChemicalRelevanceScorer(table);
    }

    @Override public int computeScore(PMID pmid, Subject subject) {
        if (!subject.isChemical())
            return 0;

        if (!table.containsPrimary(pmid))
            return 0;

        if (table.contains(pmid, subject.getMeshKey()))
            return +1;
        else
            return -1;
    }
}
