
package pubmed.xml;

import java.util.List;

import org.jdom2.Element;

import jam.util.ListUtil;
import jam.xml.JDOMElement;

import pubmed.mesh.MeshQualifierKey;

/**
 * Decorates the {@code QualifierName} element in a {@code PubMed}
 * XML document with additional parsing methods.
 */ 
public final class QualifierNameElement extends JDOMElement {
    private final String qualifierName;
    private final boolean isMajorTopic;
    private final MeshQualifierKey qualifierKey;

    private QualifierNameElement(Element element) {
        super(element, TAG_NAME);

        this.isMajorTopic = getBooleanAttribute("MajorTopicYN", false);
        this.qualifierKey = MeshQualifierKey.instance(getRequiredAttribute("UI"));
        this.qualifierName = element.getTextNormalize();
    }

    /**
     * The tag name for the element.
     */
    public static final String TAG_NAME = "QualifierName";

    /**
     * Extracts the {@code QualifierName} elements from their parent
     * heading element.
     *
     * @param parent the parent heading element.
     *
     * @return the decorated qualifier element.
     */
    public static List<QualifierNameElement> from(MeshHeadingElement parent) {
        return ListUtil.apply(parent.getChildren(TAG_NAME), child -> new QualifierNameElement(child));
    }

    /**
     * Returns the name of the qualifier encoded in this element.
     *
     * @return the name of the qualifier encoded in this element.
     */
    public String getQualifierName() {
        return qualifierName;
    }

    /**
     * Returns the {@code MeSH} key for the qualifier encoded in this
     * element.
     *
     * @return the {@code MeSH} key for the qualifier encoded in this
     * element.
     */
    public MeshQualifierKey getQualifierKey() {
        return qualifierKey;
    }

    /**
     * Identifies major topic qualifiers.
     *
     * @return {@code true} iff the qualifier is a major topic in the
     * heading.
     */
    public boolean isMajorTopic() {
        return isMajorTopic;
    }
}
