
package pubmed.bulk;

import java.util.Collection;

import pubmed.subject.Subject;

/**
 * Provides a base class to generate the relevance score files for all
 * {@code PubMed} bulk XML files in a given directory.
 */
public abstract class BulkFileRelevanceScoreProcessor extends BulkFileProcessor {
    /**
     * Creates a new bulk file relevance score processor.
     */
    protected BulkFileRelevanceScoreProcessor() {
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
        // The relevance score files must be processed even if it
        // already exists, because new subjects might be present...
        //
        bulkFile.getRelevanceScoreFile().process(getSubjects());
    }
}
