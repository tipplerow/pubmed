
package pubmed.mesh;

import java.io.File;
import java.util.List;

import org.jdom2.Element;

import jam.app.JamEnv;
import jam.app.JamLogger;
import jam.util.StreamUtil;
import jam.xml.JDOMDocument;

/**
 * Parses {@code MeSH SupplementalRecord} XML files.
 */
public final class MeshSupplementalXmlDocument {
    /**
     * Name of the environment variable pointing to the XML master
     * file.
     */
    public static final String XML_ENV = "MESH_SUPP_XML";

    /**
     * Extracts the supplemental records from the XML master file.
     *
     * @return a list containing all supplemental records in the XML master file.
     *
     * @throws RuntimeException if any I/O or parsing errors occur.
     */
    public static List<MeshSupplemental> parseMaster() {
        return parse(resolveXMLMaster());
    }

    private static File resolveXMLMaster() {
        return new File(JamEnv.getRequired(XML_ENV));
    }

    /**
     * Extracts the supplemental records from an XML file.
     *
     * @param xmlFile a supplemental record set XML file.
     *
     * @return a list containing all supplemental records in the XML
     * file.
     *
     * @throws RuntimeException if any I/O or parsing errors occur.
     */
    public static List<MeshSupplemental> parse(String xmlFile) {
        return parse(new File(xmlFile));
    }

    /**
     * Extracts the supplemental records from an XML file.
     *
     * @param xmlFile a supplemental record set XML file.
     *
     * @return a list containing all supplemental records in the XML
     * file.
     *
     * @throws RuntimeException if any I/O or parsing errors occur.
     */
    public static List<MeshSupplemental> parse(File xmlFile) {
        JDOMDocument document = JDOMDocument.parse(xmlFile);

        JamLogger.info("Processing supplemental record elements...");
        List<Element> supplementalElements = document.getRootElementChildren();

        return StreamUtil.apply(supplementalElements.stream(),
                                x -> MeshSupplementalXmlParser.parse(x));
    }
}
