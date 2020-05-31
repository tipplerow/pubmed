
package pubmed.mesh;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multiset;

import jam.lang.JamException;
import jam.util.MapUtil;

/**
 * Represents a pharmacological action defined by the {@code MeSH}
 * database.
 */
public final class MeshPharmAction {
    private final MeshDescriptorKey action;
    private final Set<MeshRecordKey> actors;

    private static boolean loaded = false;

    private static final Map<MeshDescriptorKey, MeshPharmAction> keyIndex =
        new HashMap<MeshDescriptorKey, MeshPharmAction>();

    private static final Multimap<MeshRecordKey, MeshPharmAction> substanceActions = HashMultimap.create();

    private MeshPharmAction(MeshDescriptorKey action, Set<MeshRecordKey> actors) {
        this.action = action;
        this.actors = actors;

        indexByKey();
        addActions();
    }

    private void indexByKey() {
        MapUtil.putUnique(keyIndex, action, this);
    }

    private void addActions() {
        for (MeshRecordKey substance : actors)
            substanceActions.put(substance, this);
    }

    /**
     * Creates a new pharmacological action and indexes it and its
     * actors.
     *
     * @param action the key of the action descriptor.
     *
     * @param actors keys of the substances that perform the action.
     *
     * @return the pharmacological action record.
     */
    public static MeshPharmAction create(MeshDescriptorKey action, Set<MeshRecordKey> actors) {
        return new MeshPharmAction(action, actors);
    }

    /**
     * Identifies previously created pharmacological actions.
     *
     * @param key the unique identifier for the action descriptor.
     *
     * @return {@code true} iff a pharmacological action with the
     * given key was previously created and stored in the registry.
     */
    public static boolean exists(MeshDescriptorKey key) {
        return keyIndex.containsKey(key);
    }

    /**
     * Returns the previously created pharmacological action with a
     * given key.
     *
     * @param key the unique identifier for the action descriptor.
     *
     * @return the desired pharmacological action record.
     *
     * @throws RuntimeException unless the requested action exists.
     */
    public static MeshPharmAction instance(MeshDescriptorKey key) {
        MeshPharmAction instance = keyIndex.get(key);

        if (instance != null)
            return instance;
        else
            throw JamException.runtime("Missing pharmacological action: [%s].", key);
    }

    /**
     * Ensures that all pharmacological action records have been
     * loaded, stored in the registry, and indexed by descriptor key.
     *
     * <p>When this method is called for the first time, the master
     * XML file is parsed and all records are loaded.  Subsequent
     * calls have no effect, so there is no performance penalty for
     * calling this method multiple times in the same application.
     */
    public static synchronized void load() {
        if (!loaded) {
            MeshPharmActionXmlDocument.parseMaster();
            loaded = true;
        }
    }

    /**
     * Returns a read-only view of all known pharmacological actions.
     *
     * @return a read-only view of all known pharmacological actions.
     */
    public static Collection<MeshPharmAction> allActions() {
        return Collections.unmodifiableCollection(keyIndex.values());
    }

    /**
     * Returns a read-only view of all known pharmacological actors.
     *
     * @return a read-only view of all known pharmacological actors.
     */
    public static Collection<MeshRecordKey> allActors() {
        return Collections.unmodifiableCollection(substanceActions.keySet());
    }

    /**
     * Finds other substances that share one or more pharmacological
     * actions with a given actor.
     *
     * @param actor the key of an acting substance.
     *
     * @return a {@code Multiset} counting the number of actions
     * shared by common actors.
     */
    public static Multiset<MeshRecordKey> findCoActors(MeshRecordKey actor) {
        Multiset<MeshRecordKey> coActors = HashMultiset.create();

        for (MeshPharmAction action : viewActions(actor))
            coActors.addAll(action.viewActors());

        coActors.setCount(actor, 0);
        return coActors;
    }

    /**
     * Returns the keys from a collection of actions in a new set.
     *
     * @param actions the actions of interest.
     *
     * @return a new set containing the keys of the specified actions.
     */
    public static Set<MeshDescriptorKey> keySet(Collection<MeshPharmAction> actions) {
        Set<MeshDescriptorKey> keys =
            new HashSet<MeshDescriptorKey>(actions.size());

        for (MeshPharmAction action : actions)
            keys.add(action.getKey());

        return keys;
    }

    /**
     * Returns a read-only view of the pharmacological actions
     * performed by a given substance.
     *
     * @param actor the key of the acting substance.
     *
     * @return a read-only view of the pharmacological actions
     * performed by the given substance.
     */
    public static Collection<MeshPharmAction> viewActions(MeshRecordKey actor) {
        return Collections.unmodifiableCollection(substanceActions.get(actor));
    }

    /**
     * Returns the unique identifier for this pharmacological action.
     *
     * @return the unique identifier for this pharmacological action.
     */
    public MeshDescriptorKey getKey() {
        return action;
    }

    /**
     * Returns a read-only view of the substances that perform this
     * pharmacological action.
     *
     * @return a read-only view of the substances that perform this
     * pharmacological action.
     */
    public Set<MeshRecordKey> viewActors() {
        return Collections.unmodifiableSet(actors);
    }
}
