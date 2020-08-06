
package pubmed.bulk;

import java.util.Collection;

import com.google.common.collect.Multimap;

import pubmed.article.PMID;
import pubmed.flat.ChemicalRecord;
import pubmed.subject.Subject;

/**
 * Computes and stores chemical relevance score records.
 *
 * <p>If the subject maps to a {@code MeSH} chemical record and the
 * article has a chemical list, the score is {@code +1} if the list
 * contains the subject record, {@code -1} if it does not.  The score
 * is {@code 0} if the subject does not map to a {@code MeSH} chemical
 * record or if the article does not have a chemical list.
 */
public final class ChemicalRelevanceFile extends RelevanceScoreFile {
    private final Multimap<PMID, ChemicalRecord> recordMap;

    private ChemicalRelevanceFile(BulkFile bulkFile) {
        super(bulkFile);
        this.recordMap = bulkFile.getChemicalFile().getRecordMap();
    }

    /**
     * The suffix for flat file names.
     */
    public static final String SUFFIX = "chemical_relevance";

    /**
     * Returns the flat file derived from a bulk XML file.
     *
     * @param bulkFile a bulk XML file.
     *
     * @return the flat file derived from the given bulk XML file.
     */
    public static ChemicalRelevanceFile from(BulkFile bulkFile) {
        return new ChemicalRelevanceFile(bulkFile);
    }

    @Override public int computeScore(PMID pmid, Subject subject) {
        if (!subject.isChemical())
            return 0;

        Collection<ChemicalRecord> records = recordMap.get(pmid);
        
        if (records.isEmpty())
            return 0;

        for (ChemicalRecord record : records)
            if (record.getMeshKey().equals(subject.getMeshKey()))
                return 1;

        return -1;
    }

    @Override public String getSuffix() {
        return SUFFIX;
    }
}
