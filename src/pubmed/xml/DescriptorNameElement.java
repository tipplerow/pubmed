
package pubmed.xml;

import org.jdom2.Element;

import jam.xml.JDOMElement;

import pubmed.mesh.MeshDescriptorKey;

/**
 * Decorates the {@code DescriptorName} element in a {@code PubMed}
 * XML document with additional parsing methods.
 */ 
public final class DescriptorNameElement extends JDOMElement {
    private final String descriptorName;
    private final boolean isMajorTopic;
    private final MeshDescriptorKey descriptorKey;

    private DescriptorNameElement(Element element) {
        super(element, TAG_NAME);

        this.isMajorTopic = getBooleanAttribute("MajorTopicYN", false);
        this.descriptorKey = MeshDescriptorKey.instance(getRequiredAttribute("UI"));
        this.descriptorName = element.getTextNormalize();
    }

    /**
     * The tag name for the element.
     */
    public static final String TAG_NAME = "DescriptorName";

    /**
     * Extracts the {@code DescriptorName} element from its parent
     * heading element.
     *
     * @param parent the parent heading element.
     *
     * @return the decorated descriptor element.
     */
    public static DescriptorNameElement from(MeshHeadingElement parent) {
        return new DescriptorNameElement(parent.getRequiredChild(TAG_NAME));
    }

    /**
     * Returns the name of the descriptor encoded in this element.
     *
     * @return the name of the descriptor encoded in this element.
     */
    public String getDescriptorName() {
        return descriptorName;
    }

    /**
     * Returns the {@code MeSH} key for the descriptor encoded in this
     * element.
     *
     * @return the {@code MeSH} key for the descriptor encoded in this
     * element.
     */
    public MeshDescriptorKey getDescriptorKey() {
        return descriptorKey;
    }

    /**
     * Identifies major topic qualifiers.
     *
     * @return {@code true} iff the descriptor is a major topic in the
     * heading list.
     */
    public boolean isMajorTopic() {
        return isMajorTopic;
    }
}
