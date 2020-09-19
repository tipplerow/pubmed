
package pubmed.mesh;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.jdom2.Element;

import jam.lang.JamException;
import jam.xml.JDOMParser;

/**
 * Provides a base class for parsing XML files that define {@code MeSH}
 * objects.
 */
public class MeshXmlParser extends JDOMParser {
    /**
     * Extracts all terms from an XML concept list.
     *
     * @param conceptListElement an XML concept list.
     *
     * @return all terms contained in the concept list.
     *
     * @throws RuntimeException unless the element is a well-formed
     * concept list element.
     */
    public MeshTermList parseConceptList(Element conceptListElement) {
        assertTagName(conceptListElement, "ConceptList");

        List<String> terms = new ArrayList<String>();
        List<Element> conceptElements = conceptListElement.getChildren();

        for (Element conceptElement : conceptElements)
            terms.addAll(parseConceptTerms(conceptElement));

        return MeshTermList.create(terms);
    }

    private List<String> parseConceptTerms(Element conceptElement) {
        assertTagName(conceptElement, "Concept");

        Element termListElement =
            getRequiredChild(conceptElement, "TermList");

        return parseTermList(termListElement);
    }

    private List<String> parseTermList(Element termListElement) {
        assertTagName(termListElement, "TermList");

        List<Element> termElements = termListElement.getChildren();
        List<String> terms = new ArrayList<String>(termElements.size());

        for (Element termElement : termElements)
            terms.add(parseTerm(termElement));

        return terms;
    }

    private String parseTerm(Element termElement) {
        assertTagName(termElement, "Term");

        List<Element> children =
            termElement.getChildren();

        return parseString(children.get(1));
    }

    /**
     * Extracts the descriptor name from its XML element.
     *
     * @param element a descriptor name element.
     *
     * @return the descriptor name defined by the XML element.
     *
     * @throws RuntimeException unless the element is a descriptor
     * name element.
     */
    public MeshDescriptorName parseDescriptorName(Element element) {
        assertTagName(element, "DescriptorName");
        return MeshDescriptorName.instance(parseName(element));
    }

    private String parseName(Element nameElement) {
        List<Element> children = nameElement.getChildren();

        if (children.size() == 1)
            return parseString(children.get(0));
        else
            throw JamException.runtime("Non-unique name element: [%s].", nameElement.getName());
    }

    /**
     * Extracts the unique descriptor identifier from its XML element.
     *
     * @param element a descriptor identifier element.
     *
     * @return the unique descriptor defined by the XML element.
     *
     * @throws RuntimeException unless the element is a descriptor
     * identifier element.
     */
    public MeshDescriptorKey parseDescriptorUI(Element element) {
        assertTagName(element, "DescriptorUI");
        return MeshDescriptorKey.instance(element.getTextNormalize());
    }

    /**
     * Extracts the unique identifier in a descriptor reference.
     *
     * @param element a descriptor reference element.
     *
     * @return the unique descriptor referred to in the XML element.
     *
     * @throws RuntimeException unless the element is a descriptor
     * reference element.
     */
    public MeshDescriptorKey parseDescriptorReferredTo(Element element) {
        assertTagName(element, "DescriptorReferredTo");

        List<Element> children = element.getChildren();
        assertTagNames(children, "DescriptorUI", "DescriptorName");

        String uiText = children.get(0).getTextNormalize();

        // Some UIs have a leading asterisk; remove it.
        if (uiText.startsWith("*"))
            uiText = uiText.substring(1);

        return MeshDescriptorKey.instance(uiText);
    }

    /**
     * Extracts the qualifier name from its XML element.
     *
     * @param element a qualifier name element.
     *
     * @return the qualifier name defined by the XML element.
     *
     * @throws RuntimeException unless the element is a qualifier
     * name element.
     */
    public MeshQualifierName parseQualifierName(Element element) {
        assertTagName(element, "QualifierName");
        return MeshQualifierName.instance(parseName(element));
    }

    /**
     * Extracts the unique qualifier identifier from its XML element.
     *
     * @param element a qualifier identifier element.
     *
     * @return the unique qualifier defined by the XML element.
     *
     * @throws RuntimeException unless the element is a qualifier
     * identifier element.
     */
    public MeshQualifierKey parseQualifierUI(Element element) {
        assertTagName(element, "QualifierUI");
        return MeshQualifierKey.instance(element.getTextNormalize());
    }

    /**
     * Extracts the unique identifier in a qualifier reference.
     *
     * @param element a qualifier reference element.
     *
     * @return the unique qualifier referred to in the XML element.
     *
     * @throws RuntimeException unless the element is a qualifier
     * reference element.
     */
    public MeshQualifierKey parseQualifierReferredTo(Element element) {
        assertTagName(element, "QualifierReferredTo");

        List<Element> children = element.getChildren();
        assertTagNames(children, "QualifierUI", "QualifierName");

        return MeshQualifierKey.instance(children.get(0).getTextNormalize());
    }

    /**
     * Extracts the scope note from an XML concept list.
     *
     * @param conceptListElement an XML concept list.
     *
     * @return the first scope note contained in the concept list (or
     * {@code null} if there is none).
     *
     * @throws RuntimeException unless the element is a well-formed
     * concept list element.
     */
    public String parseScopeNote(Element conceptListElement) {
        assertTagName(conceptListElement, "ConceptList");

        for (Element conceptElement : conceptListElement.getChildren())
            for (Element conceptChild : conceptElement.getChildren())
                if (conceptChild.getName().equals("ScopeNote"))
                    return conceptChild.getTextNormalize();

        return null;
    }

    /**
     * Extracts the text from a {@code String} element.
     *
     * @param element a {@code String} element.
     *
     * @return the text contained in the given element.
     *
     * @throws RuntimeException unless the element is a
     * {@code String} element.
     */
    public String parseString(Element element) {
        assertTagName(element, "String");
        return element.getTextNormalize();
    }

    /**
     * Extracts the qualifier name from its XML element.
     *
     * @param element a qualifier name element.
     *
     * @return the qualifier name defined by the XML element.
     *
     * @throws RuntimeException unless the element is a qualifier
     * name element.
     */
    public MeshSupplementalName parseSupplementalName(Element element) {
        assertTagName(element, "SupplementalRecordName");
        return MeshSupplementalName.instance(parseName(element));
    }

    /**
     * Extracts the unique qualifier identifier from its XML element.
     *
     * @param element a qualifier identifier element.
     *
     * @return the unique qualifier defined by the XML element.
     *
     * @throws RuntimeException unless the element is a qualifier
     * identifier element.
     */
    public MeshSupplementalKey parseSupplementalUI(Element element) {
        assertTagName(element, "SupplementalRecordUI");
        return MeshSupplementalKey.instance(element.getTextNormalize());
    }

    /**
     * Extracts a {@code MeSH} tree number from its XML element.
     *
     * @param element a tree number element.
     *
     * @return the {@code MeSH} tree number encoded in the element.
     */
    public MeshTreeNumber parseTreeNumber(Element element) {
        assertTagName(element, "TreeNumber");
        return MeshTreeNumber.instance(element.getTextNormalize());
    }

    /**
     * Extracts a list of {@code MeSH} tree numbers from its XML
     * element.
     *
     * @param element a tree number list element.
     *
     * @return the list of {@code MeSH} tree numbers encoded in the
     * element.
     */
    public MeshTreeNumberList parseTreeNumberList(Element element) {
        assertTagName(element, "TreeNumberList");

        // Almost all number lists are small (one or two elements), so
        // it may be more efficient in time and space to allocate the
        // array directly and then wrap it in a MeshTreeNumberList...
        List<Element> elementList = element.getChildren();
        MeshTreeNumber[] treeNumbers = new MeshTreeNumber[elementList.size()];

        for (int index = 0; index < elementList.size(); ++index)
            treeNumbers[index] = parseTreeNumber(elementList.get(index));

        return MeshTreeNumberList.of(treeNumbers);
    }
}
