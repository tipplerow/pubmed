
package pubmed.sql;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import jam.sql.SQLColumn;
import jam.sql.SQLTable;

import pubmed.article.PMID;
import pubmed.article.PubmedArticle;

/**
 * Maintains the {@code pubmed.headings} table: a many-to-many mapping
 * between articles, descriptors, and qualifiers in their heading lists.
 */
public final class HeadingTable extends SQLTable implements BulkFileTarget<HeadingRecord>, PubmedResource {
    private static HeadingTable instance = null;

    private HeadingTable() {
        super(DbManager.instance());
    }

    /**
     * The name of the {@code pubmed.headings} table.
     */
    public static final String TABLE_NAME = "headings";

    /**
     * The name of the {@code desc_key} column.
     */
    public static final String DESCRIPTOR_KEY_NAME = "desc_key";

    /**
     * The name of the {@code qual_key} column.
     */
    public static final String QUALIFIER_KEY_NAME = "qual_key";

    /**
     * Meta-data for the {@code pmid} column.
     */
    public static final SQLColumn PMID_COLUMN =
        SQLColumn.create(PMID_NAME, PMID_TYPE)
        .compositeKey();

    /**
     * Meta-data for the {@code descriptor_key} column.
     */
    public static final SQLColumn DESCRIPTOR_COLUMN =
        SQLColumn.create(DESCRIPTOR_KEY_NAME, "text")
        .compositeKey();

    /**
     * Meta-data for the {@code qualifier_key} column.
     */
    public static final SQLColumn QUALIFIER_COLUMN =
        SQLColumn.create(QUALIFIER_KEY_NAME, "text")
        .compositeKey();

    /**
     * Returns the single instance.
     *
     * @return the single instance.
     */
    public static synchronized HeadingTable instance() {
        if (instance == null)
            instance = new HeadingTable();

        instance.require();
        return instance;
    }

    /**
     * Drops the physical database table.
     */
    public static synchronized void dropTable() {
        DbManager.instance().dropTable(TABLE_NAME);
    }

    @Override public Collection<HeadingRecord> bulkRecords(Collection<PubmedArticle> articles) {
        List<HeadingRecord> records =
            new ArrayList<HeadingRecord>();

        for (PubmedArticle article : articles)
            records.addAll(HeadingRecord.list(article));

        return records;
    }

    @Override public List<SQLColumn> getColumns() {
        return List.of(PMID_COLUMN, DESCRIPTOR_COLUMN, QUALIFIER_COLUMN);
    }

    @Override public String getTableName() {
        return TABLE_NAME;
    }

    @Override public boolean updateArticles(Collection<PubmedArticle> articles) {
        //
        // It is very unlikely that a revision to the article will
        // change its headings, so this is a no-op...
        //
        return true;
    }

    @Override public boolean updateRecords(Collection<HeadingRecord> records) {
        //
        // It is very unlikely that a revision to the article will
        // change its headings, so this is a no-op...
        //
        return true;
    }
}
