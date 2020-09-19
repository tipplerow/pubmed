
package pubmed.mesh;

import java.io.File;
import java.util.List;

import org.jdom2.Element;

import jam.app.JamEnv;
import jam.app.JamLogger;
import jam.util.StreamUtil;
import jam.xml.JDOMDocument;

/**
 * Parses {@code MeSH PharmacologicalAction} XML files.
 */
public final class MeshPharmActionXmlDocument {
    /**
     * Name of the environment variable pointing to the XML master
     * file.
     */
    public static final String XML_ENV = "MESH_PA_XML";

    /**
     * Extracts the pharmacological actions from the XML master file.
     *
     * @return a list containing all pharmacological actions in the
     * XML master file.
     *
     * @throws RuntimeException if any I/O or parsing errors occur.
     */
    public static List<MeshPharmAction> parseMaster() {
        return parse(resolveXMLMaster());
    }

    private static File resolveXMLMaster() {
        return new File(JamEnv.getRequired(XML_ENV));
    }

    /**
     * Extracts the pharmacological actions from an XML file.
     *
     * @param xmlFile a pharmacological action set XML file.
     *
     * @return a list containing all pharmacological actions in the
     * XML file.
     *
     * @throws RuntimeException if any I/O or parsing errors occur.
     */
    public static List<MeshPharmAction> parse(String xmlFile) {
        return parse(new File(xmlFile));
    }

    /**
     * Extracts the pharmacological actions from an XML file.
     *
     * @param xmlFile a pharmacological action set XML file.
     *
     * @return a list containing all pharmacological actions in the
     * XML file.
     *
     * @throws RuntimeException if any I/O or parsing errors occur.
     */
    public static List<MeshPharmAction> parse(File xmlFile) {
        JDOMDocument document = JDOMDocument.parse(xmlFile);

        JamLogger.info("Processing pharmacological action elements...");
        List<Element> pharmActionElements = document.getRootElementChildren();

        return StreamUtil.apply(pharmActionElements.stream(),
                                x -> MeshPharmActionXmlParser.parse(x));
    }
}
