
package pubmed.bulk;

import java.util.Set;

import jam.app.JamLogger;

import pubmed.article.PMID;
import pubmed.flat.HeadingRecord;
import pubmed.flat.HeadingTable;
import pubmed.mesh.MeshDescriptor;
import pubmed.mesh.MeshTreeNumber;
import pubmed.mesh.MeshTreeNumberList;
import pubmed.subject.Subject;

/**
 * Computes and stores {@code MeSH} tree relevance score records.
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
public final class MeshTreeRelevanceFile extends RelevanceScoreFile {
    private final HeadingTable table;

    private MeshTreeRelevanceFile(BulkFile bulkFile) {
        super(bulkFile);
        this.table = bulkFile.getHeadingFile().load();
    }

    /**
     * The suffix for flat file names.
     */
    public static final String SUFFIX = "mesh_tree_relevance";

    /**
     * Returns the flat file derived from a bulk XML file.
     *
     * @param bulkFile a bulk XML file.
     *
     * @return the flat file derived from the given bulk XML file.
     */
    public static MeshTreeRelevanceFile from(BulkFile bulkFile) {
        return new MeshTreeRelevanceFile(bulkFile);
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

    @Override public String getSuffix() {
        return SUFFIX;
    }
}
