
package pubmed.bulk;

import java.util.Collection;

import pubmed.relev.RelevanceSummarySubjectFile;
import pubmed.subject.Subject;

/**
 * Provides a base class to generate relevance score files and update
 * relevance summary files for all {@code PubMed} bulk XML files in a
 * given directory.
 */
public abstract class BulkFileRelevanceProcessor extends BulkFileProcessor {
    /**
     * Creates a new bulk file relevance score processor.
     */
    protected BulkFileRelevanceProcessor() {
        super();
    }

    /**
     * Returns the subjects used as the targets for article relevance
     * scoring.
     *
     * @return the subjects used as the targets for article relevance
     * scoring.
     */
    public abstract Collection<Subject> getSubjects();

    @Override public void processFile(BulkFile bulkFile) {
        //
        // The relevance score file must be processed even if it
        // already exists, because new subjects might be present...
        //
        Collection<Subject> subjects = getSubjects();

        bulkFile.getRelevanceScoreFile().process(subjects);
        RelevanceSummarySubjectFile.process(bulkFile, subjects);
    }
}
