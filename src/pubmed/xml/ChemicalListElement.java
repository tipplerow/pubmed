
package pubmed.xml;

import java.util.Collections;
import java.util.List;

import org.jdom2.Element;

import jam.util.ListUtil;
import jam.xml.JDOMElement;

/**
 * Decorates the {@code ChemicalList} element in a {@code PubMed}
 * XML document with additional parsing methods.
 */ 
public final class ChemicalListElement extends JDOMElement {
    private List<ChemicalElement> chemicals;

    private ChemicalListElement(Element element) {
        super(element, TAG_NAME);
    }

    /**
     * The tag name for the element.
     */
    public static final String TAG_NAME = "ChemicalList";

    /**
     * Extracts the chemical list element from the parent citation.
     *
     * @param parent the parent citation.
     *
     * @return the decorated chemical list element.
     */
    public static ChemicalListElement from(MedlineCitationElement parent) {
        Element child = parent.getOptionalChild(TAG_NAME);

        if (child != null)
            return new ChemicalListElement(child);
        else
            return null;
    }

    /**
     * Returns the chemical elements in this list.
     *
     * @return the chemical elements in this list.
     */
    public List<ChemicalElement> getChemicals() {
        if (chemicals == null) {
            chemicals = ChemicalElement.from(this);
            chemicals = Collections.unmodifiableList(chemicals);
        }

        return chemicals;
    }
}
