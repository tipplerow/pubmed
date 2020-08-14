
package pubmed.subject;

import java.util.List;

import pubmed.mesh.MeshRecord;
import pubmed.mesh.MeshTreeNumber;
import pubmed.mesh.MeshTreeNumberList;
import pubmed.nlp.LemmaAnnotator;
import pubmed.nlp.LemmaList;

/**
 * Defines a subject that should match most cancer-related articles.
 */
public final class CancerSubject extends Subject {
    private CancerSubject() {
        super("CANCER_v2");
    }

    /**
     * The single cancer subject instance.
     */
    public static final Subject INSTANCE = new CancerSubject();

    /**
     * Terms used to identify cancer types and therapies, which may be
     * matched against article titles, abstracts, or keyword lists.
     */
    public static final List<String> KEYWORDS =
        List.of("adenocarcinoma",
                "anticancer",
                "anticarcinogenic",
                "antineoplastic",
                "antitumor",
                "antitumour",
                "antitumorigenic",
                "anti-carcinogenic",
                "anti-neoplastic",
                "anti-tumor",
                "anti-tumour",
                "anti-tumorigenic",
                "astrocytoma",
                "cancer",
                "carcinogenesis",
                "carcinogenic",
                "carcinogenicity",
                "carcinoma",
                "carcinosarcoma",
                "cholangiocarcinoma",
                "chondrosarcoma",
                "chordoma",
                "craniopharyingioma",
                "cytoma",
                "glioblastoma",
                "glioma",
                "hodgkin",
                "leukemia",
                "lymphoma",
                "macroglobulinemia",
                "malignancy",
                "malignant",
                "medulloblastoma",
                "melanoma",
                "meningioma",
                "mesothelioma",
                "myeloma",
                "neoplasia",
                "neoplasm",
                "neuroblastoma",
                "neuroma",
                "oligodendroglioma",
                "oncology",
                "osteosarcoma",
                "paraganglioma",
                "pheochromocytoma",
                "retinoblastoma",
                "rhabdomyosarcoma",
                "sarcoma",
                "schwannoma",
                "thymoma",
                "tumor",
                "tumour",
                "tumorigenesis",
                "tumorigenic",
                "tumorigenicity");

    /**
     * Root {@code MeSH} tree numbers for cancer.
     */
    public static final MeshTreeNumberList TREE_NUMBERS =
        MeshTreeNumberList.of(MeshTreeNumber.instance("C04.557"),
                              MeshTreeNumber.instance("C04.588"),
                              MeshTreeNumber.instance("C04.626"),
                              MeshTreeNumber.instance("C04.651"),
                              MeshTreeNumber.instance("C04.666"),
                              MeshTreeNumber.instance("C04.682"),
                              MeshTreeNumber.instance("C04.692"),
                              MeshTreeNumber.instance("C04.697"),
                              MeshTreeNumber.instance("C04.700"));
    
    @Override public MeshRecord getMeshRecord() {
        //
        // There is not a single MeSH record for cancer that excludes
        // cysts and hamartoma...
        //
        return null;
    }

    @Override public List<String> getKeywords() {
        return KEYWORDS;
    }

    @Override public List<LemmaList> getKeywordLemmas() {
        return LemmaAnnotator.contentWords(KEYWORDS);
    }

    @Override public MeshTreeNumberList getMeshTreeNumbers() {
        return TREE_NUMBERS;
    }

    @Override public String getTablePrefix() {
        return "cancer";
    }

    @Override public String getTableSuffix() {
        return null;
    }

    @Override public boolean isChemical() {
        return false;
    }

    @Override public boolean isDescriptor() {
        return false;
    }

    @Override public boolean isQualifier() {
        return false;
    }

    @Override public boolean isSupplemental() {
        return false;
    }
}
