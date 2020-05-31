
package pubmed.mesh;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import jam.lang.JamException;

/**
 * Represents a substance that shares one or more pharmacological
 * actions with another substance.
 */
public final class MeshPharmCoActor {
    private final MeshRecordKey actor;
    private final MeshRecordKey coActor;
    private final Set<MeshDescriptorKey> actions;

    private MeshPharmCoActor(MeshRecordKey actor,
                             MeshRecordKey coActor,
                             Set<MeshDescriptorKey> actions) {
        validateActions(actions);

        this.actor = actor;
        this.coActor = coActor;
        this.actions = actions;
    }

    private static void validateActions(Set<MeshDescriptorKey> actions) {
        if (actions.isEmpty())
            throw JamException.runtime("No shared actions.");
    }

    /**
     * Orders co-actor relationships in (1) ascending order by actor,
     * (2) decreasing order by the number of shared actions, and (3)
     * ascending order by co-actor.
     */
    public static final Comparator<MeshPharmCoActor> DESCENDING_COUNT_COMPARATOR =
        new Comparator<MeshPharmCoActor>() {
            @Override public int compare(MeshPharmCoActor co1, MeshPharmCoActor co2) {
                int cmp = co1.actor.getKey().compareTo(co2.actor.getKey());

                if (cmp != 0)
                    return cmp;

                // Descending order...
                cmp = Integer.compare(co2.countActions(), co1.countActions());

                if (cmp != 0)
                    return cmp; 

                return co1.coActor.getKey().compareTo(co2.coActor.getKey());
            }
        };

    /**
     * Creates a new co-actor relationship.
     *
     * @param actor the key of the primary substance.
     *
     * @param coActor the key of the co-acting substance.
     *
     * @param actions the pharmacological actions shared by the
     * actor and co-actor.
     *
     * @return the new co-actor relationship.
     */
    public static MeshPharmCoActor create(MeshRecordKey actor,
                                          MeshRecordKey coActor,
                                          Set<MeshDescriptorKey> actions) {
        return new MeshPharmCoActor(actor, coActor, actions);
    }

    /**
     * Finds other substances that share one or more pharmacological
     * actions with a given actor.
     *
     * @param actorKey the key of an acting substance.
     *
     * @return a list of co-actor relationships for the primary actor.
     */
    public static List<MeshPharmCoActor> find(MeshRecordKey actorKey) {
        Multimap<MeshRecordKey, MeshDescriptorKey> actionMap = HashMultimap.create();

        for (MeshPharmAction action : MeshPharmAction.viewActions(actorKey))
            for (MeshRecordKey coActorKey : action.viewActors())
                if (!coActorKey.equals(actorKey))
                    actionMap.put(coActorKey, action.getKey());

        List<MeshPharmCoActor> coActorList =
            new ArrayList<MeshPharmCoActor>();

        for (MeshRecordKey coActorKey : actionMap.keySet()) {
            Set<MeshDescriptorKey> actions =
                new HashSet<MeshDescriptorKey>(actionMap.get(coActorKey));

            coActorList.add(MeshPharmCoActor.create(actorKey, coActorKey, actions));
        }

        return coActorList;
    }

    /**
     * Returns a read-only view of the pharmacological actions shared
     * by the actor and co-actor.
     *
     * @return a read-only view of the pharmacological actions shared
     * by the actor and co-actor.
     */
    public int countActions() {
        return actions.size();
    }

    /**
     * Returns the key of the primary substance.
     *
     * @return the key of the primary substance.
     */
    public MeshRecordKey getActor() {
        return actor;
    }

    /**
     * Returns the key of the co-acting substance.
     *
     * @return the key of the co-acting substance.
     */
    public MeshRecordKey getCoActor() {
        return coActor;
    }

    /**
     * Returns a read-only view of the pharmacological actions shared
     * by the actor and co-actor.
     *
     * @return a read-only view of the pharmacological actions shared
     * by the actor and co-actor.
     */
    public Set<MeshDescriptorKey> viewActions() {
        return Collections.unmodifiableSet(actions);
    }
}
