
package pubmed.filter;

import java.util.Collection;
import java.util.List;

import pubmed.article.PubmedArticle;
import pubmed.mesh.MeshRecordKey;

/**
 * Implements a filter that selects articles that contain one or more
 * {@code MeSH} descriptors or supplemental records in their chemical
 * list.
 */
public final class ChemicalListFilter extends ArticleFilter {
    private final Collection<MeshRecordKey> targets;

    private ChemicalListFilter(Collection<MeshRecordKey> targets) {
        this.targets = targets;
    }

    /**
     * Creates a filter that selects articles that contain one
     * or more {@code MeSH} descriptors or supplemental records
     * in their chemical list.
     *
     * @param targets the keys of the substances to match.
     *
     * @return a filter that selects articles that contain one or
     * more of the specified substances in their chemical list.
     */
    public static ChemicalListFilter create(MeshRecordKey... targets) {
        return create(List.of(targets));
    }

    /**
     * Creates a filter that selects articles that contain one
     * or more {@code MeSH} descriptors or supplemental records
     * in their chemical list.
     *
     * @param targets the keys of the substances to match.
     *
     * @return a filter that selects articles that contain one or
     * more of the specified substances in their chemical list.
     */
    public static ChemicalListFilter create(Collection<MeshRecordKey> targets) {
        return new ChemicalListFilter(targets);
    }

    @Override public int score(PubmedArticle article) {
        if (article.hasChemicalList())
            return super.score(article);
        else
            return 0;
    }

    @Override public boolean testArticle(PubmedArticle article) {
	for (MeshRecordKey target : targets)
	    if (article.hasChemical(target))
		return true;

        return false;
    }
}
