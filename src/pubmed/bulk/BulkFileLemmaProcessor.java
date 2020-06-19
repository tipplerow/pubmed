
package pubmed.bulk;

import java.io.File;
import java.util.List;

import jam.app.JamLogger;
import jam.io.IOUtil;
import jam.lang.JamException;
import jam.report.LineBuilder;
import jam.util.StreamUtil;

import pubmed.article.PMID;
import pubmed.article.PubmedArticle;
import pubmed.article.PubmedXmlDocument;
import pubmed.nlp.LemmaAnnotator;
import pubmed.nlp.LemmaList;

/**
 * Identifies all {@code PubMed} bulk data files in a given directory,
 * parse them, lemmatizes the article titles and abstracts, and writes
 * the lemmas to new data files.
 */
public final class BulkFileLemmaProcessor extends BulkFileProcessor {
    private static BulkFileLemmaProcessor instance = null;

    private BulkFileLemmaProcessor() {
    }

    /**
     * Returns the single lemma processor.
     *
     * @return the single lemma processor.
     */
    public synchronized static BulkFileLemmaProcessor instance() {
        if (instance == null)
            instance = new BulkFileLemmaProcessor();

        return instance;
    }

    /**
     * Generates the lemmas from an article abstract and returns them
     * in a coded string to be written in the abstract lemma file.
     *
     * @param article an article to process.
     *
     * @return the coded abstract lemma string.
     */
    public static String lemmatizeAbstract(PubmedArticle article) {
        return lemmatize(article.getPMID(), article.getAbstract());
    }

    /**
     * Generates the lemmas from an article title and returns them in
     * a coded string to be written in the title lemma file.
     *
     * @param article an article to process.
     *
     * @return the coded title lemma string.
     */
    public static String lemmatizeTitle(PubmedArticle article) {
        return lemmatize(article.getPMID(), article.getTitle());
    }

    private static String lemmatize(PMID pmid, String text) {
        LineBuilder builder =
            new LineBuilder("|");

        builder.append(pmid.intValue());

        if (text != null && !text.isBlank()) {
            LemmaList lemmas = LemmaAnnotator.contentWords(text);

            for (String lemma : lemmas)
                builder.append(lemma);
        }

        return builder.toString();
    }

    @Override public void processFile(File bulkFile) {
        PubmedXmlDocument document = PubmedXmlDocument.parse(bulkFile);
        List<PubmedArticle> articles = document.viewLatest();

        JamLogger.info("Lemmatizing article titles...");

        List<String> titleLines =
            StreamUtil.apply(articles.parallelStream(),
                             article -> lemmatizeTitle(article));

        JamLogger.info("Lemmatizing article abstracts...");

        List<String> abstractLines =
            StreamUtil.apply(articles.parallelStream(),
                             article -> lemmatizeAbstract(article));

        JamLogger.info("Writing title lemmas...");
        IOUtil.writeLines(getTitleLemmaFile(bulkFile), false, titleLines);

        JamLogger.info("Writing abstract lemmas...");
        IOUtil.writeLines(getAbstractLemmaFile(bulkFile), false, abstractLines);
    }

    private static void usage() {
        System.err.println("Usage: pubmed.bulk.BulkFileLemmaProcessor DIR1 [DIR2 ...]");
        System.exit(1);
    }

    public static void main(String[] args) {
        if (args.length < 1)
            usage();

        for (String directory : args)
            instance().processDirectory(directory);
    }
}
