
package pubmed.filter;

import java.util.Collection;
import java.util.List;

import pubmed.article.PubmedArticle;
import pubmed.mesh.MeshDescriptorKey;
import pubmed.subject.Subject;

/**
 * Implements a filter that selects articles that contain one or more
 * {@code MeSH} descriptors in their heading list.
 */
public final class MeshHeadingFilter extends ArticleFilter {
    private final Collection<MeshDescriptorKey> targets;

    private MeshHeadingFilter(Collection<MeshDescriptorKey> targets) {
        this.targets = targets;
    }

    /**
     * Creates a filter that selects articles containing a subject
     * descriptor record in their heading list.
     *
     * @param subject the subject to match.
     *
     * @return a filter that selects articles matching the given
     * subject.
     */
    public static ArticleFilter create(Subject subject) {
        if (subject.isDescriptor())
            return create((MeshDescriptorKey) subject.getMeshRecord().getKey());
        else
            return ArticleFilter.none();
    }

    /**
     * Creates a filter that selects articles that contain one
     * or more {@code MeSH} descriptors in their heading list.
     *
     * @param targets the keys of the descriptors to match.
     *
     * @return a filter that selects articles that contain one or
     * more of the specified descriptors in their heading list.
     */
    public static MeshHeadingFilter create(MeshDescriptorKey... targets) {
        return create(List.of(targets));
    }

    /**
     * Creates a filter that selects articles that contain one
     * or more {@code MeSH} descriptors or supplemental records
     * in their heading list.
     *
     * @param targets the keys of the descriptors to match.
     *
     * @return a filter that selects articles that contain one or
     * more of the specified descriptors in their heading list.
     */
    public static MeshHeadingFilter create(Collection<MeshDescriptorKey> targets) {
        return new MeshHeadingFilter(targets);
    }

    @Override public int score(PubmedArticle article) {
        if (article.hasHeadingList())
            return super.score(article);
        else
            return 0;
    }

    @Override public boolean testArticle(PubmedArticle article) {
	for (MeshDescriptorKey target : targets)
	    if (article.hasHeading(target))
		return true;

        return false;
    }
}
