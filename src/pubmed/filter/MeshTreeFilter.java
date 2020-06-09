
package pubmed.filter;

import java.util.Collection;
import java.util.List;

import pubmed.article.PubmedArticle;
import pubmed.mesh.MeshDescriptor;
import pubmed.mesh.MeshHeading;
import pubmed.mesh.MeshTreeNumber;
import pubmed.subject.Subject;

/**
 * Matches articles that contain one or more {@code MeSH} headings
 * one branches of the {@code MeSH} tree structure defined by a set
 * of target nodes.
 */
public final class MeshTreeFilter extends ArticleFilter {
    private final Collection<MeshTreeNumber> targets;

    private MeshTreeFilter(Collection<MeshTreeNumber> targets) {
        this.targets = targets;
    }

    /**
     * Creates a filter that matches a subject with articles that
     * contain a {@code MeSH} heading whose descriptor has a tree
     * number equal to or more specific than a tree number for the
     * subject.
     *
     * @param subject the subject to match.
     *
     * @return a filter that selects articles matching the given
     * subject.
     */
    public static ArticleFilter create(Subject subject) {
        if (subject.isDescriptor())
            return create(subject.getDescriptor().getNumberList());
        else
            return ArticleFilter.none();
    }

    /**
     * Creates a new {@code MeSH} tree filter for a fixed set of
     * target nodes.
     *
     * @param targets the {@code MeSH} tree nodes to include in the
     * filter.
     *
     * @return the filter for the specified target nodes.
     */ 
    public static MeshTreeFilter create(MeshTreeNumber... targets) {
        return create(List.of(targets));
    }

    /**
     * Creates a new {@code MeSH} tree filter for a fixed set of
     * target nodes.
     *
     * @param targets the {@code MeSH} tree nodes to include in the
     * filter.
     *
     * @return the filter for the specified target nodes.
     */ 
    public static MeshTreeFilter create(Collection<MeshTreeNumber> targets) {
        return new MeshTreeFilter(targets);
    }

    @Override public int score(PubmedArticle article) {
        if (article.hasHeadingList())
            return super.score(article);
        else
            return 0;
    }

    @Override public boolean testArticle(PubmedArticle article) {
        for (MeshHeading heading : article.viewHeadingList())
            if (testHeading(heading))
                return true;

        return false;
    }

    private boolean testHeading(MeshHeading heading) {
        return testDescriptor(heading.getDescriptor());
    }

    private boolean testDescriptor(MeshDescriptor descriptor) {
        for (MeshTreeNumber descriptorNumber : descriptor.getNumberList())
            if (testDescriptorNumber(descriptorNumber))
                return true;
        
        return false;
    }

    private boolean testDescriptorNumber(MeshTreeNumber descriptorNumber) {
        for (MeshTreeNumber target : targets)
            if (descriptorNumber.onSubTree(target))
                return true;

        return false;
    }
}
