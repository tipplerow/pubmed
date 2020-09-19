
package pubmed.xml;

import org.jdom2.Element;

import jam.xml.JDOMElement;

import pubmed.mesh.MeshRecordKey;

/**
 * Decorates the {@code NameOfSubstance} element in a {@code PubMed}
 * XML document with additional parsing methods.
 */ 
public final class NameOfSubstanceElement extends JDOMElement {
    private final MeshRecordKey recordKey;

    private NameOfSubstanceElement(Element element) {
        super(element, TAG_NAME);
        this.recordKey = MeshRecordKey.instance(getRequiredAttribute("UI"));
    }

    /**
     * The tag name for the element.
     */
    public static final String TAG_NAME = "NameOfSubstance";

    /**
     * Extracts the {@code NameOfSubstance} element from its parent
     * chemical element.
     *
     * @param parent the parent chemical element.
     *
     * @return the decorated substance element.
     */
    public static NameOfSubstanceElement from(ChemicalElement parent) {
        return new NameOfSubstanceElement(parent.getRequiredChild(TAG_NAME));
    }

    /**
     * Returns the {@code MeSH} record key for the substance encoded
     * in this element.
     *
     * @return the {@code MeSH} record key for the substance encoded
     * in this element.
     */
    public MeshRecordKey getRecordKey() {
        return recordKey;
    }
}
