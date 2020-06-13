
package pubmed.junit;

import java.util.List;
import java.util.Map;

import pubmed.article.PMID;
import pubmed.article.PubmedArticle;
import pubmed.article.PubmedXmlDocument;

import pubmed.sql.DbEnv;
import pubmed.sql.SubjectScoreRecord;
import pubmed.sql.SubjectScoreTable;
import pubmed.subject.MeshSubject;
import pubmed.subject.Subject;

import org.junit.*;
import static org.junit.Assert.*;

public class SubjectScoreTableTest {
    private static final String SAMPLE_XML = "data/test/pubmed_sample.xml";

    private static final PubmedXmlDocument document = PubmedXmlDocument.parse(SAMPLE_XML);
    private static final List<PubmedArticle> articles = document.viewLatest();

    private static final Subject atorvastatin = MeshSubject.create("D000069059");

    static {
        DbEnv.TEST.use();
    }

    @Test public void testInsertArticles() {
        SubjectScoreTable.dropTable(atorvastatin);
        SubjectScoreTable table = SubjectScoreTable.instance(atorvastatin);

        table.insertArticles(articles);

        Map<PMID, SubjectScoreRecord> records = table.load();
        assertEquals(1, records.size());

        SubjectScoreRecord record = records.get(PMID.instance(24451147));

        assertEquals(1, record.getTitleScore());
        assertEquals(3, record.getAbstractScore());
        assertEquals(1, record.getMeshTreeScore());
        assertEquals(1, record.getHeadingListScore());
        assertEquals(0, record.getKeywordListScore());
        assertEquals(1, record.getChemicalListScore());
    }

    @Test public void testTableCode() {
        assertEquals("2dg", SubjectScoreTable.getTableCode("2-DG"));
        assertEquals("colon_cancer", SubjectScoreTable.getTableCode("Colon cancer"));
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("pubmed.junit.SubjectScoreTableTest");
    }
}
