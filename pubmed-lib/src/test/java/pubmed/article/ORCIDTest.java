
package pubmed.article;

import org.junit.*;
import static org.junit.Assert.*;

public class ORCIDTest {
    @Test public void testNormalize() {
        assertEquals("0000-1111-2222-3333", ORCID.normalize("http://orcid.org/0000-1111-2222-3333"));
        assertEquals("0000-1111-2222-3333", ORCID.normalize("https://orcid.org/0000-1111-2222-3333"));
        assertEquals("0000-1111-2222-3333", ORCID.normalize("http://orcid.org/0000-1111-2222-3333/"));
        assertEquals("0000-1111-2222-3333", ORCID.normalize("https://orcid.org/0000-1111-2222-3333/"));
        assertEquals("0000-1111-2222-3333", ORCID.normalize("0000111122223333"));
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("pubmed.article.ORCIDTest");
    }
}
