
package pubmed.sql;

import java.util.Collection;

import pubmed.medline.MedlineJournal;
import pubmed.medline.MedlineTA;

/**
 * Provides in-memory caching of {@code MedlineJournal} objects and
 * persistent storage in the {@code pubmed.journals} database table.
 */
public final class JournalCache extends PubmedCache<MedlineTA, MedlineJournal> {
    private static JournalCache instance = null;

    private JournalCache() {
        super(JournalTable.instance());
    }

    /**
     * Returns the single cache.
     *
     * @return the single cache.
     */
    public static synchronized JournalCache instance() {
        if (instance == null)
            instance = new JournalCache();

        return instance;
    }

    @Override public String getName() {
        return "JournalCache";
    }

    @Override public Class getKeyClass() {
        return MedlineTA.class;
    }

    @Override public Class getRecordClass() {
        return MedlineJournal.class;
    }

    @Override protected void insert(Collection<MedlineJournal> journals) {
        DbManager.instance().bulkCopy(JournalTable.TABLE_NAME, journals);
    }
}
