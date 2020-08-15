
package pubmed.relev;

import jam.util.PairKeyTreeTable;

import pubmed.article.PMID;

public final class RelevanceSummaryTable extends PairKeyTreeTable<PMID, String, RelevanceSummaryRecord> {
    private RelevanceSummaryTable() {
        super();
    }

    @SuppressWarnings("unchecked")
    public static RelevanceSummaryTable create() {
        return new RelevanceSummaryTable();
    }
}
