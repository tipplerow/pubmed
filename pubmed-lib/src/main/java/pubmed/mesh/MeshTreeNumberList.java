
package pubmed.mesh;

import java.util.AbstractList;
import java.util.Collection;
import java.util.List;

/**
 * An immutable list of {@code MeSH} tree numbers.
 */
public final class MeshTreeNumberList extends AbstractList<MeshTreeNumber> {
    private final List<MeshTreeNumber> numbers;

    private MeshTreeNumberList(List<MeshTreeNumber> numbers) {
        this.numbers = numbers;
    }

    /**
     * The single empty number list.
     */
    public static final MeshTreeNumberList EMPTY = new MeshTreeNumberList(List.of());

    /**
     * Creates a new immutable tree number list.
     *
     * @param numbers the underlying tree numbers.
     *
     * @return an immutable number list with the specified numbers.
     */
    public static MeshTreeNumberList copyOf(Collection<MeshTreeNumber> numbers) {
        return new MeshTreeNumberList(List.copyOf(numbers));
    }

    /**
     * Wraps an array of tree numbers in an immutable list.
     *
     * @param numbers the underlying tree numbers.
     *
     * @return an immutable number list with the specified numbers.
     */
    public static MeshTreeNumberList of(MeshTreeNumber... numbers) {
        return new MeshTreeNumberList(List.of(numbers));
    }

    @Override public MeshTreeNumber get(int index) {
        return numbers.get(index);
    }

    @Override public int size() {
        return numbers.size();
    }
}
