
package pubmed.article;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jdom2.Content;
import org.jdom2.Element;
import org.jdom2.output.Format;

import jam.app.JamLogger;
import jam.lang.JamException;
import jam.util.ListUtil;
import jam.util.StreamUtil;
import jam.xml.JDOMDocument;

/**
 * Parses {@code PubMed} XML files.
 */
public final class PubmedXmlDocument {
    private final File xmlFile;

    private JDOMDocument document;

    private List<PMID> deleted;
    private List<PubmedArticle> latest;   // Only the latest version of each article
    private List<PubmedArticle> articles; // All articles in the XML document

    private PubmedXmlDocument(File xmlFile) {
        this.xmlFile = xmlFile;
    }

    /**
     * Extracts all articles from an XML file.
     *
     * @param xmlFile an article set XML file.
     *
     * @return a document object containing all articles in the XML
     * file.
     *
     * @throws RuntimeException if any I/O or parsing errors occur.
     */
    public static PubmedXmlDocument parse(String xmlFile) {
        return parse(new File(xmlFile));
    }

    /**
     * Extracts all articles from an XML file.
     *
     * @param xmlFile an article set XML file.
     *
     * @return a document object containing all articles in the XML
     * file.
     *
     * @throws RuntimeException if any I/O or parsing errors occur.
     */
    public static PubmedXmlDocument parse(File xmlFile) {
        PubmedXmlDocument pubmedDocument =
            new PubmedXmlDocument(xmlFile);

        pubmedDocument.parse();
        return pubmedDocument;
    }

    /**
     * Returns the number of unique articles in this document (earlier
     * versions of the same article are excluded).
     *
     * @return the number of unique articles in this document.
     */
    public int countLatest() {
        return latest.size();
    }

    /**
     * Returns the number of deleted citations in this document.
     *
     * @return the number of deleted citations in this document.
     */
    public int countDeleted() {
        return deleted.size();
    }

    /**
     * Returns a list of the original articles in this document.
     *
     * @return a list containing the articles with {@code PMID}
     * version 1.
     */
    public List<PubmedArticle> getOriginals() {
        return ListUtil.filter(latest, article -> !article.isRevision());
    }

    /**
     * Returns a list of the revised articles in this document.
     *
     * @return a list containing the articles with {@code PMID}
     * version greater than 1.
     */
    public List<PubmedArticle> getRevisions() {
        return ListUtil.filter(latest, article -> article.isRevision());
    }

    /**
     * Returns the XML file that was parsed.
     *
     * @return the XML file that was parsed.
     */
    public File getXMLFile() {
        return xmlFile;
    }

    /**
     * Writes an XML file containing the articles currently in this
     * document (a subset of the original articles, if this document
     * has been filtered).
     *
     * @param outputFile the output file to write.
     */
    public void unparse(String outputFile) {
        unparse(new File(outputFile));
    }

    /**
     * Writes an XML file containing the articles currently in this
     * document (a subset of the original articles, if this document
     * has been filtered).
     *
     * @param outputFile the output file to write.
     */
    public void unparse(File outputFile) {
        Format format = Format.getPrettyFormat();
        format.setIndent(" ");

        document.unparse(outputFile, format);
    }

    /**
     * Returns a read-only view of all articles in this document (even
     * those with multiple revisions).
     *
     * @return a read-only view of all articles in this document (even
     * those with multiple revisions).
     */
    public List<PubmedArticle> viewArticles() {
        return articles;
    }

    /**
     * Returns a read-only view of the deleted citations.
     *
     * @return a read-only view of the deleted citations.
     */
    public List<PMID> viewDeleted() {
        return Collections.unmodifiableList(deleted);
    }

    /**
     * Returns a read-only view of the latest versions of each article
     * in this document.
     *
     * @return a read-only view of the latest versions of each article
     * in this document.
     */
    public List<PubmedArticle> viewLatest() {
        return latest;
    }

    private void parse() {
        document = JDOMDocument.parse(xmlFile);

        List<Element> rootElementChildren =
            document.getRootElementChildren();

        JamLogger.info("Processing [%d] article elements...", rootElementChildren.size());

        deleted = new ArrayList<PMID>();
        articles = new ArrayList<PubmedArticle>(rootElementChildren.size());

        for (Element element : rootElementChildren)
            processElement(element);

        latest = PubmedArticle.latest(articles);
    }

    private void processElement(Element element) {
        String tagName = element.getName();

        switch (tagName) {
        case "PubmedArticle":
            processArticle(element);
            break;

        case "DeleteCitation":
            processDeleteCitation(element);
            break;

        default:
            JamLogger.info("Skipping element [%s]...", tagName);
            break;
        }
    }

    private void processArticle(Element element) {
        try {
            articles.add(PubmedArticleXmlParser.parse(element));
        }
        catch (Exception ex) {
            JamLogger.warn("Article parsing exception: [%s].", ex.getMessage());
        }
    }

    private void processDeleteCitation(Element element) {
        deleted = PubmedDeleteCitationXmlParser.parse(element);
    }
}
