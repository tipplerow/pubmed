
package pubmed.mesh;

import java.io.File;
import java.util.List;

import org.jdom2.Element;

import jam.app.JamEnv;
import jam.app.JamLogger;
import jam.util.StreamUtil;
import jam.xml.JDOMDocument;

/**
 * Parses {@code MeSH Descriptor} XML files.
 */
public final class MeshDescriptorXmlDocument {
    /**
     * Name of the environment variable pointing to the XML master
     * file.
     */
    public static final String XML_ENV = "MESH_DESC_XML";

    /**
     * Extracts the descriptors from the XML master file.
     *
     * @return a list containing all descriptors in the XML master file.
     *
     * @throws RuntimeException if any I/O or parsing errors occur.
     */
    public static List<MeshDescriptor> parseMaster() {
        return parse(resolveXMLMaster());
    }

    private static File resolveXMLMaster() {
        return new File(JamEnv.getRequired(XML_ENV));
    }

    /**
     * Extracts the descriptors from an XML file.
     *
     * @param xmlFile a descriptor set XML file.
     *
     * @return a list containing all descriptors in the XML file.
     *
     * @throws RuntimeException if any I/O or parsing errors occur.
     */
    public static List<MeshDescriptor> parse(String xmlFile) {
        return parse(new File(xmlFile));
    }

    /**
     * Extracts the descriptors from an XML file.
     *
     * @param xmlFile a descriptor set XML file.
     *
     * @return a list containing all descriptors in the XML file.
     *
     * @throws RuntimeException if any I/O or parsing errors occur.
     */
    public static List<MeshDescriptor> parse(File xmlFile) {
        JDOMDocument document = JDOMDocument.parse(xmlFile);

        JamLogger.info("Processing descriptor elements...");
        List<Element> descriptorElements = document.getRootElementChildren();

        return StreamUtil.apply(descriptorElements.stream(),
                                x -> MeshDescriptorXmlParser.parse(x));
    }
}
