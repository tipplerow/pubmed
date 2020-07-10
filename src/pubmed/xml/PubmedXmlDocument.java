
package pubmed.xml;

import java.io.File;
import java.util.List;

import pubmed.article.PMID;

/**
 * Parses {@code PubMed} XML bulk data files.
 */
public final class PubmedXmlDocument {
    private final File xmlFile;
    private final PubmedArticleSetElement pubmedArticleSetElement;

    private PubmedXmlDocument(File xmlFile) {
        this.xmlFile = xmlFile;
        this.pubmedArticleSetElement = PubmedArticleSetElement.from(xmlFile);
    }

    /**
     * Parses a {@code PubMed} XML bulk data file.
     *
     * @param xmlFile an article set XML file.
     *
     * @return a document object containing the parsed content.
     *
     * @throws RuntimeException if any I/O or parsing errors occur.
     */
    public static PubmedXmlDocument parse(String xmlFile) {
        return parse(new File(xmlFile));
    }

    /**
     * Parses a {@code PubMed} XML bulk data file.
     *
     * @param xmlFile an article set XML file.
     *
     * @return a document object containing the parsed content.
     *
     * @throws RuntimeException if any I/O or parsing errors occur.
     */
    public static PubmedXmlDocument parse(File xmlFile) {
        return new PubmedXmlDocument(xmlFile);
    }

    /**
     * Returns the {@code PubmedArticle} elements encoded in the
     * document.
     *
     * @return the {@code PubmedArticle} elements encoded in the
     * document.
     */
    public List<PubmedArticleElement> getPubmedArticleElements() {
        return pubmedArticleSetElement.getPubmedArticleElements();
    }

    /**
     * Returns the deleted citations encoded in the document.
     *
     * @return the deleted citations encoded in the document.
     */
    public List<PMID> getDeletedCitations() {
        return pubmedArticleSetElement.getDeleteCitationElement().getDeleted();
    }

    /**
     * Returns the XML file that was parsed.
     *
     * @return the XML file that was parsed.
     */
    public File getXMLFile() {
        return xmlFile;
    }
}
