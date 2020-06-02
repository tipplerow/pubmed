
package pubmed.filter;

import java.util.Collection;
import java.util.List;

import jam.app.JamLogger;

import pubmed.article.PubmedArticle;
import pubmed.mesh.MeshDescriptor;
import pubmed.mesh.MeshHeading;
import pubmed.mesh.MeshTreeNumber;

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

    static {
        JamLogger.info("MeshTreeFilter: Loading MeSH descriptors...");
        MeshDescriptor.load();
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
