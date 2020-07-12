
package pubmed.flat;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import jam.flat.FlatRecord;

import pubmed.xml.PubmedArticleElement;
import pubmed.xml.PubmedXmlDocument;

/**
 * Provides a base class to for flat files containing article joining
 * records (many-to-many mappings).
 */
public abstract class PubmedJoinFile<V extends FlatRecord> extends FlatFileBase {
    /**
     * Creates a new flat file for records derived from a given bulk
     * XML file.
     *
     * @param xmlFile the bulk XML file containing articles to be
     * processed.
     */
    protected PubmedJoinFile(File xmlFile) {
        super(xmlFile);
    }

    /**
     * Extracts the joining records from an XML article element.
     *
     * @param element the XML article element to process.
     *
     * @return the joining records extracted from the article (will be
     * empty if the article does not contain any relevant data items).
     */
    public abstract List<V> extractRecords(PubmedArticleElement element);

    @Override public List<V> extractRecords(PubmedXmlDocument document) {
        List<V> documentRecords = new ArrayList<V>();

        for (PubmedArticleElement element : document.getPubmedArticleElements())
            documentRecords.addAll(extractRecords(element));

        return documentRecords;
    }
}
