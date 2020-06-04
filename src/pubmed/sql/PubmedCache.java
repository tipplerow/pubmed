
package pubmed.sql;

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import jam.sql.SQLKeyTable;

/**
 * Maintains an in-memory cache and persistent database table of
 * {@code pubmed} records. Objects may be added to the cache and
 * the underlying table, but they may not be removed.
 *
 * @param <K> the runtime type of the record keys.
 *
 * @param <V> the runtime type of the record values.
 */
public abstract class PubmedCache<K, V> extends AbstractCollection<V> {
    private final Map<K, V> cache;
    private final SQLKeyTable<K, V> table;

    /**
     * Creates a new cache with a backing database table.
     *
     * @param table the backing database table.
     */
    protected PubmedCache(SQLKeyTable<K, V> table) {
        table.require();

        this.table = table;
        this.cache = table.load();
    }

    /**
     * Returns the persistent table supporting this cache.
     *
     * @return the persistent table supporting this cache.
     */
    protected SQLKeyTable<K, V> table() {
        return table;
    }

    /**
     * Returns the name of this cache (for message logging).
     *
     * @return the name of this cache (for message logging).
     */
    public abstract String getName();

    /**
     * Returns the runtime class for the key objects.
     *
     * @return the runtime class for the key objects.
     */
    public abstract Class getKeyClass();

    /**
     * Returns the runtime class for the record objects.
     *
     * @return the runtime class for the record objects.
     */
    public abstract Class getRecordClass();

    /**
     * Inserts a collection of records into the underlying database
     * table.  This base class will ensure that the records do not
     * already exist in the table.
     *
     * @param records the records to insert.
     */
    protected abstract void insert(Collection<V> records);

    /**
     * Identifies records contained in this cache.
     *
     * @param key the key of a record in question.
     *
     * @return {@code true} iff this cache contains a record with the
     * specified key.
     */
    public boolean containsKey(K key) {
        return cache.containsKey(key);
    }

    /**
     * Retrieves a record by key.
     *
     * @param key the key of the desired record.
     *
     * @return the record with the desired key ({@code null} if there
     * is no matching key).
     */
    public V fetch(K key) {
        return cache.get(key);
    }

    /**
     * Returns the key for a given record.
     *
     * @param record a record to be indexed.
     *
     * @return the key for the specified record.
     */
    public K getKey(V record) {
        return table.getKey(record);
    }

    @Override public boolean add(V record) {
        if (containsKey(getKey(record)))
            return false;

        updateTable(record);
        updateCache(record);

        return true;
    }

    private void updateTable(V record) {
        table.store(record);
    }

    private void updateCache(V record) {
        cache.put(table.getKey(record), record);
    }

    @Override public boolean addAll(Collection<? extends V> records) {
        //
        // We cannot add duplicate records to the database, so select
        // those not already present.  Also note that the collection
        // of records may contain duplicates (e.g., the authors or
        // journals from a collection of articles), so we must build
        // a _map_ of missing articles, not a list...
        //
        Map<K, V> missing = new HashMap<K, V>(records.size());

        for (V record : records) {
            K key = getKey(record);

            if (!containsKey(key))
                missing.put(key, record);
        }

        if (missing.isEmpty())
            return false;

        updateTable(missing.values());
        updateCache(missing.values());

        return true;
    }

    private void updateTable(Collection<V> records) {
        insert(records);
    }

    private void updateCache(Collection<V> records) {
        for (V record : records)
            updateCache(record);
    }

    @Override public void clear() {
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("unchecked")
    @Override public boolean contains(Object obj) {
        if (obj == null)
            return false;

        if (obj.getClass().equals(getKeyClass()))
            return containsKey((K) obj);

        if (obj.getClass().equals(getRecordClass()))
            return containsKey(getKey((V) obj));

        return false;
    }

    @Override public Iterator<V> iterator() {
        //
        // Do not support the iterator.remove() method...
        //
        return Collections.unmodifiableCollection(cache.values()).iterator();
    }

    @Override public boolean remove(Object obj) {
        throw new UnsupportedOperationException();
    }

    @Override public int size() {
        return cache.size();
    }
}
