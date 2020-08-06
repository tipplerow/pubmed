
package pubmed.bulk;

import pubmed.article.PMID;
import pubmed.flat.HeadingTable;
import pubmed.subject.Subject;

/**
 * Computes and stores heading relevance score records.
 *
 * <p>If the subject maps to a {@code MeSH} descriptor record and the
 * article has a {@code MeSH} heading list, the score is {@code +1} if
 * the heading list contains the subject record, {@code -1} if it does
 * not.  The score is {@code 0} if the subject does not map to a MeSH
 * descriptor record or if the article does not have a heading list.
 */
public final class HeadingRelevanceFile extends RelevanceScoreFile {
    private final HeadingTable table;

    private HeadingRelevanceFile(BulkFile bulkFile) {
        super(bulkFile);
        this.table = bulkFile.getHeadingFile().load();
    }

    /**
     * The suffix for flat file names.
     */
    public static final String SUFFIX = "heading_relevance";

    /**
     * Returns the flat file derived from a bulk XML file.
     *
     * @param bulkFile a bulk XML file.
     *
     * @return the flat file derived from the given bulk XML file.
     */
    public static HeadingRelevanceFile from(BulkFile bulkFile) {
        return new HeadingRelevanceFile(bulkFile);
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

    @Override public String getSuffix() {
        return SUFFIX;
    }
}
