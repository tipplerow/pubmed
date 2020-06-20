
package pubmed.bulk;

import java.io.File;
import java.util.List;
import java.util.function.Function;

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
        File titleLemmaFile = getTitleLemmaFile(bulkFile);
        File abstractLemmaFile = getAbstractLemmaFile(bulkFile);

        if (titleLemmaFile.canRead() && abstractLemmaFile.canRead()) {
            JamLogger.info("Bulk file has been processed; skipping...");
            return;
        }

        PubmedXmlDocument document = PubmedXmlDocument.parse(bulkFile);
        List<PubmedArticle> articles = document.viewLatest();

        if (!titleLemmaFile.canRead()) {
            JamLogger.info("Lemmatizing article titles...");
            processLemmas(articles, titleLemmaFile, article -> lemmatizeTitle(article));
        }

        if (!abstractLemmaFile.canRead()) {
            JamLogger.info("Lemmatizing article abstracts...");
            processLemmas(articles, abstractLemmaFile, article -> lemmatizeAbstract(article));
        }
    }

    private void processLemmas(List<PubmedArticle> articles, File lemmaFile,
                               Function<PubmedArticle, String> lemmaFunc) {
        List<String> lemmaLines =
            StreamUtil.apply(articles.parallelStream(), lemmaFunc);

        JamLogger.info("Writing lemma file [%s]...", lemmaFile);
        IOUtil.writeLines(lemmaFile, false, lemmaLines);
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
