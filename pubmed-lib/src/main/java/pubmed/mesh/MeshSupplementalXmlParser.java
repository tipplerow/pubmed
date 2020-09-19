
package pubmed.mesh;

import java.util.List;

import org.jdom2.Element;

/**
 * Parsers {@code MeSH SupplementalRecord} XML files.
 */
public final class MeshSupplementalXmlParser extends MeshXmlParser {
    private final Element supplementalElement;
    
    private MeshSupplementalXmlParser(Element supplementalElement) {
        this.supplementalElement = supplementalElement;
    }

    /**
     * Extracts the supplemental record from an XML element.
     *
     * @param supplementalElement an XML element encoding a 
     * {@code MeSH} supplemental record.
     *
     * @return the supplemental record encoded in the given element.
     *
     * @throws RuntimeException if any parsing errors occur.
     */
    public static MeshSupplemental parse(Element supplementalElement) {
        MeshSupplementalXmlParser parser =
            new MeshSupplementalXmlParser(supplementalElement);

        return parser.parse();
    }

    private MeshSupplemental parse() {
        assertTagName(supplementalElement, "SupplementalRecord");

        MeshSupplementalKey  key   = parseKey();
        MeshSupplementalName name  = parseName();
        String               note  = parseNote();
        MeshTermList         terms = parseTerms();

        return MeshSupplemental.create(key, name, note, terms);
    }

    private MeshSupplementalKey parseKey() {
        return parseSupplementalUI(getRequiredChild(supplementalElement, "SupplementalRecordUI"));
    }

    private MeshSupplementalName parseName() {
        return parseSupplementalName(getRequiredChild(supplementalElement, "SupplementalRecordName"));
    }

    private String parseNote() {
        Element noteElement = supplementalElement.getChild("Note");

        if (noteElement != null)
            return noteElement.getTextNormalize();
        else
            return null;
    }
    
    private MeshTermList parseTerms() {
        return parseConceptList(getRequiredChild(supplementalElement, "ConceptList"));
    }
}
