
package pubmed.mesh;

import java.io.File;
import java.util.List;

import org.jdom2.Element;

import jam.app.JamEnv;
import jam.app.JamLogger;
import jam.util.StreamUtil;
import jam.xml.JDOMDocument;

/**
 * Parses {@code MeSH Qualifier} XML files.
 */
public final class MeshQualifierXmlDocument {
    /**
     * Name of the environment variable pointing to the XML master
     * file.
     */
    public static final String XML_ENV = "MESH_QUAL_XML";

    /**
     * Extracts the qualifiers from the XML master file.
     *
     * @return a list containing all qualifiers in the XML master file.
     *
     * @throws RuntimeException if any I/O or parsing errors occur.
     */
    public static List<MeshQualifier> parseMaster() {
        return parse(resolveXMLMaster());
    }

    private static File resolveXMLMaster() {
        return new File(JamEnv.getRequired(XML_ENV));
    }

    /**
     * Extracts the qualifiers from an XML file.
     *
     * @param xmlFile a qualifier set XML file.
     *
     * @return a list containing all qualifiers in the XML file.
     *
     * @throws RuntimeException if any I/O or parsing errors occur.
     */
    public static List<MeshQualifier> parse(String xmlFile) {
        return parse(new File(xmlFile));
    }

    /**
     * Extracts the qualifiers from an XML file.
     *
     * @param xmlFile a qualifier set XML file.
     *
     * @return a list containing all qualifiers in the XML file.
     *
     * @throws RuntimeException if any I/O or parsing errors occur.
     */
    public static List<MeshQualifier> parse(File xmlFile) {
        JDOMDocument document = JDOMDocument.parse(xmlFile);

        JamLogger.info("Processing qualifier elements...");
        List<Element> qualifierElements = document.getRootElementChildren();

        return StreamUtil.apply(qualifierElements.stream(),
                                x -> MeshQualifierXmlParser.parse(x));
    }
}
