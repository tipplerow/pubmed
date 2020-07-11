
package pubmed.flat;

import java.io.File;

import jam.flat.FlatTable;

import pubmed.article.PMID;

/**
 * Maintains a flat table of article abstract records.
 */
public final class ArticleAbstractTable extends FlatTable<PMID, ArticleAbstractRecord> {
    /**
     * Creates a new table by parsing a flat file.
     *
     * @param file the flat file containing the table data.
     *
     * @return a new table containing the data in the given
     * flat file.
     */
    public static ArticleAbstractTable load(File file) {
        ArticleAbstractTable table = new ArticleAbstractTable();
        table.parse(file);
        return table;
    }

    @Override public ArticleAbstractRecord parse(String line) {
        return ArticleAbstractRecord.parse(line);
    }
}
