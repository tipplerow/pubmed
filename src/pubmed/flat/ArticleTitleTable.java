
package pubmed.flat;

import java.io.File;

import jam.flat.FlatTable;

import pubmed.article.PMID;

/**
 * Maintains a flat table of article title records.
 */
public final class ArticleTitleTable extends FlatTable<PMID, ArticleTitleRecord> {
    /**
     * Creates a new table by parsing a flat file.
     *
     * @param file the flat file containing the table data.
     *
     * @return a new table containing the data in the given
     * flat file.
     */
    public static ArticleTitleTable load(File file) {
        ArticleTitleTable table = new ArticleTitleTable();
        table.parse(file);
        return table;
    }

    public String selectTitle(PMID pmid) {
        ArticleTitleRecord record = select(pmid);

        if (record != null)
            return record.getTitle();
        else
            return null;
    }

    @Override public ArticleTitleRecord parse(String line) {
        return ArticleTitleRecord.parse(line);
    }
}
