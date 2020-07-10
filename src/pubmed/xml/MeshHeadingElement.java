
package pubmed.xml;

import java.util.List;

import org.jdom2.Element;

import jam.util.ListUtil;
import jam.xml.JDOMElement;

import pubmed.mesh.MeshDescriptorKey;
import pubmed.mesh.MeshHeading;
import pubmed.mesh.MeshQualifierKey;

/**
 * Decorates the {@code MeshHeading} element in a {@code PubMed}
 * XML document with additional parsing methods.
 */ 
public final class MeshHeadingElement extends JDOMElement {
    private final DescriptorNameElement descriptorNameElement;
    private final List<QualifierNameElement> qualifierNameElements;

    private MeshHeadingElement(Element element) {
        super(element, TAG_NAME);

        this.descriptorNameElement = DescriptorNameElement.from(this);
        this.qualifierNameElements = QualifierNameElement.from(this);
    }

    /**
     * The tag name for the element.
     */
    public static final String TAG_NAME = "MeshHeading";

    /**
     * Extracts the heading elements from the parent list.
     *
     * @param parent the parent heading list.
     *
     * @return the decorated heading elements.
     */
    public static List<MeshHeadingElement> from(MeshHeadingListElement parent) {
        return ListUtil.apply(parent.getChildren(TAG_NAME), child -> new MeshHeadingElement(child));
    }

    /**
     * Returns the descriptor name element for this heading.
     *
     * @return the descriptor element for this heading.
     */
    public DescriptorNameElement getDescriptorNameElement() {
        return descriptorNameElement;
    }

    /**
     * Returns the qualifier name elements for this heading.
     *
     * @return the qualifier elements for this heading.
     */
    public List<QualifierNameElement> getQualifierNameElements() {
        return qualifierNameElements;
    }

    /**
     * Returns the {@code MeSH} heading record encoded in this
     * element.
     *
     * @return the {@code MeSH} heading record encoded in this
     * element.
     */
    public MeshHeading getMeshHeading() {
        MeshDescriptorKey descriptorKey =
            descriptorNameElement.getDescriptorKey();

        List<MeshQualifierKey> qualifierKeys =
            ListUtil.apply(qualifierNameElements, element -> element.getQualifierKey());

        return MeshHeading.create(descriptorKey, qualifierKeys);
    }
}
