
package pubmed.relev;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import jam.app.JamLogger;
import jam.util.PairKeyTable;

import pubmed.article.PMID;
import pubmed.bulk.BulkFile;
import pubmed.subject.Subject;

/**
 * Generates and stores relevance summary records for subject pairs.
 */
public final class RelevanceSummaryPairFile extends RelevanceSummaryFileBase {
    // The subjects of this file...
    private final Subject subject1;
    private final Subject subject2;

    // One file per subject pair...
    private static final PairKeyTable<Subject, Subject, RelevanceSummaryPairFile> instances = PairKeyTable.hash();

    private RelevanceSummaryPairFile(Subject subject1, Subject subject2) {
        super(resolveSummaryFile(subject1, subject2));

        this.subject1 = subject1;
        this.subject2 = subject2;
    }

    private static File resolveSummaryFile(Subject subject1, Subject subject2) {
        return new File(resolvePairDir(subject1), summaryBaseName(subject2));
    }

    private static File resolvePairDir(Subject subject1) {
        return new File(resolveRelevanceDir(), subject1.getKey());
    }

    private static String summaryBaseName(Subject subject2) {
        return subject2.getKey() + "_relevance_summary.txt";
    }

    /**
     * Returns the relevance pair summary file for two subjects.
     *
     * @param subject1 the first subject of the relevance file.
     *
     * @param subject2 the second subject of the relevance file.
     *
     * @return the relevance pair summary file for the specified
     * subjects.
     */
    public static RelevanceSummaryPairFile instance(Subject subject1, Subject subject2) {
        RelevanceSummaryPairFile instance = instances.get(subject1, subject2);

        if (instance == null) {
            instance = new RelevanceSummaryPairFile(subject1, subject2);
            instances.put(subject1, subject2, instance);
        }

        return instance;
    }

    /**
     * Merges relevance summary data for pairs of subjects.
     *
     * @param subject1 the first subject to process.
     *
     * @param subjects2 the second subjects to process.
     *
     * @throws RuntimeException unless all single-subject summary
     * files exist.
     */
    public static synchronized void process(Subject subject1, Collection<? extends Subject> subjects2) {
        Set<PMID> subject1PMID =
            RelevanceSummarySubjectFile.loadRelevantPMID(subject1);

        subjects2.parallelStream().forEach(subject2 -> process(subject1, subject2, subject1PMID));
    }

    private static void process(Subject subject1, Subject subject2, Set<PMID> subject1PMID) {
        List<RelevanceSummaryRecord> pairRecords
            = new ArrayList<RelevanceSummaryRecord>();

        List<RelevanceSummaryRecord> sub2Records =
            RelevanceSummarySubjectFile.load(subject2).values();

        for (RelevanceSummaryRecord sub2Record : sub2Records)
            if (sub2Record.isLikelyMatch() && subject1PMID.contains(sub2Record.getPMID()))
                pairRecords.add(sub2Record);

        RelevanceSummaryPairFile.instance(subject1, subject2).appendSummaryRecords(pairRecords);
    }
}
