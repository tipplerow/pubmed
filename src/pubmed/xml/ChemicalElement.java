
package pubmed.xml;

import java.util.List;

import org.jdom2.Element;

import jam.util.ListUtil;
import jam.xml.JDOMElement;

import pubmed.mesh.MeshRecordKey;

/**
 * Decorates the {@code Chemical} element in a {@code PubMed}
 * XML document with additional parsing methods.
 */ 
public final class ChemicalElement extends JDOMElement {
    private final NameOfSubstanceElement substanceElement;

    private ChemicalElement(Element element) {
        super(element, TAG_NAME);
        this.substanceElement = NameOfSubstanceElement.from(this);
    }

    /**
     * The tag name for the element.
     */
    public static final String TAG_NAME = "Chemical";

    /**
     * Extracts the chemical element from the parent list.
     *
     * @param parent the parent chemical list.
     *
     * @return the decorated chemical element.
     */
    public static List<ChemicalElement> from(ChemicalListElement parent) {
        return ListUtil.apply(parent.getChildren(TAG_NAME), child -> new ChemicalElement(child));
    }

    /**
     * Returns the substance element for this chemical.
     *
     * @return the substance element for this chemical.
     */
    public NameOfSubstanceElement getNameOfSubstanceElement() {
        return substanceElement;
    }

    /**
     * Returns the {@code MeSH} record key for this chemical.
     *
     * @return the {@code MeSH} record key for this chemical.
     */
    public MeshRecordKey getRecordKey() {
        return substanceElement.getRecordKey();
    }
}
