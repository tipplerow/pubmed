
package pubmed.mesh;

import java.util.List;

import org.jdom2.Element;

/**
 * Extracts {@code MeSH Descriptor} records from their XML elements.
 */
public final class MeshDescriptorXmlParser extends MeshXmlParser {
    private final Element descriptorElement;

    private MeshDescriptorXmlParser(Element descriptorElement) {
        this.descriptorElement = descriptorElement;
    }

    /**
     * Extracts the descriptor from an XML element.
     *
     * @param descriptorElement an XML element encoding a 
     * {@code MeSH} descriptor.
     *
     * @return the descriptor encoded in the given element.
     *
     * @throws RuntimeException if any parsing errors occur.
     */
    public static MeshDescriptor parse(Element descriptorElement) {
        MeshDescriptorXmlParser parser =
            new MeshDescriptorXmlParser(descriptorElement);

        return parser.parse();
    }

    private MeshDescriptor parse() {
        assertTagName(descriptorElement, "DescriptorRecord");

        MeshDescriptorKey  key     = parseKey();
        MeshDescriptorName name    = parseName();
        String             note    = parseNote();
        MeshTermList       terms   = parseTerms();
        MeshTreeNumberList numbers = parseTreeNumbers();

        return MeshDescriptor.create(key, name, note, terms, numbers);
    }

    private MeshDescriptorKey parseKey() {
        return parseDescriptorUI(getRequiredChild(descriptorElement, "DescriptorUI"));
    }

    private MeshDescriptorName parseName() {
        return parseDescriptorName(getRequiredChild(descriptorElement, "DescriptorName"));
    }

    private String parseNote() {
        return parseScopeNote(getRequiredChild(descriptorElement, "ConceptList"));
    }
    
    private MeshTermList parseTerms() {
        return parseConceptList(getRequiredChild(descriptorElement, "ConceptList"));
    }

    private MeshTreeNumberList parseTreeNumbers() {
        Element numberListElement = descriptorElement.getChild("TreeNumberList");

        if (numberListElement != null)
            return parseTreeNumberList(numberListElement);
        else
            return MeshTreeNumberList.EMPTY;
    }
}
