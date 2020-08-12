
package pubmed.flat;

import java.io.File;

import jam.flat.FlatTable;

import pubmed.article.PMID;

/**
 * Maintains a flat table of article DOI records.
 */
public final class ArticleDOITable extends FlatTable<PMID, ArticleDOIRecord> {
    /**
     * Creates a new table by parsing a flat file.
     *
     * @param file the flat file containing the table data.
     *
     * @return a new table containing the data in the given
     * flat file.
     */
    public static ArticleDOITable load(File file) {
        ArticleDOITable table = new ArticleDOITable();
        table.parse(file);
        return table;
    }

    @Override public ArticleDOIRecord parse(String line) {
        return ArticleDOIRecord.parse(line);
    }
}
