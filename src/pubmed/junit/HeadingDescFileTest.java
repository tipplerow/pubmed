
package pubmed.junit;

import java.util.List;

import pubmed.article.PMID;
import pubmed.bulk.BulkFile;
import pubmed.bulk.HeadingDescFile;
import pubmed.flat.HeadingDescRecord;
import pubmed.flat.HeadingDescTable;
import pubmed.mesh.MeshDescriptorKey;

import org.junit.*;
import static org.junit.Assert.*;

public class HeadingDescFileTest {
    private static final BulkFile bulkFile = BulkFile.create("data/test/pubmed_sample.xml");
    private static final HeadingDescFile headingFile = HeadingDescFile.from(bulkFile);

    @Test public void testFile() {
        headingFile.processFile(true);
        headingFile.processFile(false);

        HeadingDescTable table = headingFile.load();

        assertEquals(36, table.count());
        assertEquals(22, table.countPrimary(PMID.instance(24451147)));
        assertEquals(12, table.countPrimary(PMID.instance(1)));
        assertEquals( 2, table.countPrimary(PMID.instance(31383582)));

        List<HeadingDescRecord> records = table.selectPrimary(PMID.instance(1));

        assertEquals(12, records.size());
        assertEquals(MeshDescriptorKey.instance("D000445"), records.get(0).getForeignKey());
        assertEquals(MeshDescriptorKey.instance("D011549"), records.get(11).getDescriptorKey());

        assertTrue(headingFile.delete());
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("pubmed.junit.HeadingDescFileTest");
    }
}
