
package pubmed.mesh;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jdom2.Element;

/**
 * Parses the {@code MeSH PharmacologicalAction} XML file.
 */
public final class MeshPharmActionXmlParser extends MeshXmlParser {
    private final Element pharmActionElement;
    
    private MeshPharmActionXmlParser(Element pharmActionElement) {
        this.pharmActionElement = pharmActionElement;
    }

    /**
     * Extracts the pharmacological action from an XML element.
     *
     * @param pharmActionElement an XML element encoding a 
     * {@code MeSH PharmacologicalAction}.
     *
     * @return the pharmacological action encoded in the given
     * element.
     *
     * @throws RuntimeException if any parsing errors occur.
     */
    public static MeshPharmAction parse(Element pharmActionElement) {
        MeshPharmActionXmlParser parser =
            new MeshPharmActionXmlParser(pharmActionElement);

        return parser.parse();
    }

    private MeshPharmAction parse() {
        assertTagName(pharmActionElement, "PharmacologicalAction");

        List<Element> children = pharmActionElement.getChildren();
        assertTagNames(children, "DescriptorReferredTo", "PharmacologicalActionSubstanceList");

        MeshDescriptorKey action = parseDescriptorReferredTo(children.get(0));
        Set<MeshRecordKey> actors = parseActors(children.get(1));
        
        return MeshPharmAction.create(action, actors);
    }

    private Set<MeshRecordKey> parseActors(Element substanceListElement) {
        assertTagName(substanceListElement, "PharmacologicalActionSubstanceList");

        List<Element> substanceElements =
            substanceListElement.getChildren();

        Set<MeshRecordKey> actors =
            new HashSet<MeshRecordKey>(substanceElements.size());

        for (Element substanceElement : substanceElements)
            actors.add(parseActor(substanceElement));

        return actors;
    }

    private MeshRecordKey parseActor(Element substanceElement) {
        assertTagName(substanceElement, "Substance");

        List<Element> children = substanceElement.getChildren();
        assertTagNames(children, "RecordUI", "RecordName");

        return MeshRecordKey.instance(children.get(0).getTextNormalize());
    }
}
