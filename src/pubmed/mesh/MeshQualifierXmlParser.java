
package pubmed.mesh;

import java.util.List;

import org.jdom2.Element;

/**
 * Parses {@code MeSH Qualifier} XML files.
 */
public final class MeshQualifierXmlParser extends MeshXmlParser {
    private final Element qualifierElement;
    
    private MeshQualifierXmlParser(Element qualifierElement) {
        this.qualifierElement = qualifierElement;
    }

    /**
     * Extracts the qualifier from an XML element.
     *
     * @param qualifierElement an XML element encoding a 
     * {@code MeSH} qualifier.
     *
     * @return the qualifier encoded in the given element.
     *
     * @throws RuntimeException if any parsing errors occur.
     */
    public static MeshQualifier parse(Element qualifierElement) {
        MeshQualifierXmlParser parser =
            new MeshQualifierXmlParser(qualifierElement);

        return parser.parse();
    }

    private MeshQualifier parse() {
        assertTagName(qualifierElement, "QualifierRecord");

        MeshQualifierKey   key     = parseKey();
        MeshQualifierName  name    = parseName();
        String             note    = parseNote();
        MeshTermList       terms   = parseTerms();
        MeshTreeNumberList numbers = parseTreeNumbers();

        return MeshQualifier.create(key, name, note, terms, numbers);
    }

    private MeshQualifierKey parseKey() {
        return parseQualifierUI(getRequiredChild(qualifierElement, "QualifierUI"));
    }

    private MeshQualifierName parseName() {
        return parseQualifierName(getRequiredChild(qualifierElement, "QualifierName"));
    }
    
    private String parseNote() {
        return parseScopeNote(getRequiredChild(qualifierElement, "ConceptList"));
    }
    
    private MeshTermList parseTerms() {
        return parseConceptList(getRequiredChild(qualifierElement, "ConceptList"));
    }

    private MeshTreeNumberList parseTreeNumbers() {
        Element numberListElement = qualifierElement.getChild("TreeNumberList");

        if (numberListElement != null)
            return parseTreeNumberList(numberListElement);
        else
            return MeshTreeNumberList.EMPTY;
    }
}
