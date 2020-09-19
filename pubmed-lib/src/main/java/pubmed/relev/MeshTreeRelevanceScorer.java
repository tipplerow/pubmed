
package pubmed.relev;

import java.util.Set;

import jam.app.JamLogger;

import pubmed.article.PMID;
import pubmed.bulk.BulkFile;
import pubmed.flat.HeadingRecord;
import pubmed.flat.HeadingTable;
import pubmed.mesh.MeshDescriptor;
import pubmed.mesh.MeshTreeNumber;
import pubmed.mesh.MeshTreeNumberList;
import pubmed.subject.Subject;

/**
 * Computes {@code MeSH} tree relevance score records.
 *
 * <p>If the subject is described by one or more {@code MeSH} tree
 * numbers and the article has a {@code MeSH} heading list, the score
 * is {@code +1} if one or more of the descriptors in the heading list
 * has a tree number equal to or more specific than a tree number for
 * the subject descriptor; the score is {@code -1} if there are no
 * such heading descriptors.  The score is {@code 0} if the subject is
 * not described by a {@code MeSH} tree number or if the article does
 * not have a heading list.
 */
public final class MeshTreeRelevanceScorer implements RelevanceScorer {
    private final HeadingTable table;

    private MeshTreeRelevanceScorer(HeadingTable table) {
        this.table = table;
    }

    /**
     * Returns the relevance scorer for a given bulk XML file.
     *
     * @param bulkFile the bulk XML file to process.
     *
     * @return the relevance scorer for the given bulk XML file.
     */
    public static MeshTreeRelevanceScorer instance(BulkFile bulkFile) {
        return instance(bulkFile.getHeadingFile().load());
    }

    /**
     * Returns the relevance scorer for a table of heading records.
     *
     * @param table heading records derived from a bulk XML file.
     *
     * @return the relevance scorer for the given lemma table.
     */
    public static MeshTreeRelevanceScorer instance(HeadingTable table) {
        return new MeshTreeRelevanceScorer(table);
    }

    @Override public int computeScore(PMID pmid, Subject subject) {
        MeshTreeNumberList subjectNumberList = subject.getMeshTreeNumbers();

        if (subjectNumberList.isEmpty())
            return 0;

        Set<HeadingRecord> articleHeadingRecords = table.select(pmid);

        if (articleHeadingRecords.isEmpty())
            return 0;

        try {
            return computeScore(subjectNumberList, articleHeadingRecords);
        }
        catch (Exception ex) {
            JamLogger.warn(ex);
            return 0;
        }
    }

    private static int computeScore(MeshTreeNumberList subjectNumberList, Set<HeadingRecord> articleHeadingRecords) {
        for (HeadingRecord articleHeadingRecord : articleHeadingRecords) {
            MeshDescriptor headingDescriptor = articleHeadingRecord.getDescriptor();
            MeshTreeNumberList headingNumberList = headingDescriptor.getNumberList();

            for (MeshTreeNumber subjectNumber : subjectNumberList) 
                for (MeshTreeNumber headingNumber : headingNumberList)
                    if (headingNumber.onSubTree(subjectNumber))
                        return +1;
        }

        return -1;
    }
}
