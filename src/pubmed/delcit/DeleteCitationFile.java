
package pubmed.delcit;

import java.util.Set;

import jam.app.JamEnv;
import jam.app.JamProperties;
import jam.io.FileUtil;
import jam.io.TOCFile;
import jam.util.StreamUtil;

import pubmed.article.PMID;
import pubmed.bulk.BulkFile;
import pubmed.xml.PubmedXmlDocument;

/**
 * Maintains the global file of deleted citations.
 */
public final class DeleteCitationFile {
    private final TOCFile tocFile;
    private static final DeleteCitationFile instance = new DeleteCitationFile();

    private DeleteCitationFile() {
        this.tocFile = TOCFile.instance(resolveFileName());
        System.out.println(resolveFileName());
        System.out.println(tocFile.getCanonicalFile());
    }

    private static String resolveFileName() {
        if (JamProperties.isSet(DELCIT_FILENAME_PROPERTY))
            return JamProperties.getRequired(DELCIT_FILENAME_PROPERTY);

        String pubmedLocalDir = JamEnv.getRequired(BulkFile.LOCAL_DIRNAME_ENV);
        String delcitFileName = FileUtil.join(pubmedLocalDir, DELCIT_FILENAME_DEFAULT);

        return delcitFileName;
    }

    /**
     * The relative path name (under the PubMed parent) of the deleted
     * citation file.
     */
    public static final String DELCIT_FILENAME_DEFAULT = "delete_citation.txt";

    /**
     * Name of the system property that specifies the full path to the
     * deleted citation file. If this property is unset, the file name
     * will resolve to $PUBMED_LOCAL_DIR/delcit.
     */
    public static final String DELCIT_FILENAME_PROPERTY = "pubmed.delcit.fileName";

    /**
     * Returns the global file of deleted citations.
     *
     * @return the global file of deleted citations.
     */
    public static DeleteCitationFile instance() {
        return instance;
    }

    /**
     * Adds a deleted citation.
     *
     * @param pmid the identifier of the deleted article.
     */
    public void add(PMID pmid) {
        tocFile.add(format(pmid));
    }

    private static String format(PMID pmid) {
        return Integer.toString(pmid.intValue());
    }

    /**
     * Adds all deleted citations contained in an XML document.
     *
     * @param document the XML document to process.
     */
    public void add(PubmedXmlDocument document) {
        for (PMID pmid : document.getDeletedCitations())
            add(pmid);
    }

    /**
     * Adds all deleted citations contained in a bulk XML file.
     *
     * @param bulkFile the bulk XML file to process.
     */
    public void add(BulkFile bulkFile) {
        add(bulkFile.getDocument());
    }

    /**
     * Identifies deleted citations.
     *
     * @param pmid the article identifier to examine.
     *
     * @return {@code true} iff the article with the specified
     * identifer has been deleted from the corpus.
     */
    public boolean contains(PMID pmid) {
        return tocFile.contains(format(pmid));
    }

    /**
     * Deletes the underlying physical file.
     *
     * @return {@code true} iff the file was deleted successfully.
     */
    public boolean delete() {
        return tocFile.delete();
    }

    /**
     * Returns a read-only view of the deleted citations.
     *
     * @return a read-only view of the deleted citations.
     */
    public Set<PMID> viewDeleted() {
        return StreamUtil.toHashSet(tocFile.viewItems().parallelStream().map(item -> PMID.instance(item)));
    }
}
