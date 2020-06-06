
package pubmed.subject;

import java.util.List;

import pubmed.mesh.MeshRecordKey;
import pubmed.mesh.MeshRecordName;
import pubmed.mesh.MeshRecordType;

/**
 * Defines <em>cancer</em> as a subject covered in articles.
 */
public final class CancerSubject implements Subject {
    private CancerSubject() {
    }

    /**
     * The single {@code CancerSubject} instance.
     */
    public static CancerSubject INSTANCE = new CancerSubject();
        
    @Override public String getName() {
        return "Cancer";
    }

    @Override public List<String> getKeywords() {
        return List.of("adenocarcinoma",
                       "anticarcinogenic",
                       "antineoplastic",
                       "antitumor",
                       "antitumorigenic",
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
                       "insulinoma",
                       "leukemia",
                       "lymphoma",
                       "macroglobulinemia",
                       "malignancy",
                       "malignant",
                       "medulloblastoma",
                       "melanoma",
                       "meningioma",
                       "mesothelioma",
                       "myelofibrosis",
                       "myeloma",
                       "myeloproliferative",
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
                       "tumorigenesis",
                       "tumorigenic",
                       "tumorigenicity",
                       "tumour");
    }

    @Override public MeshRecordKey getMeshKey() {
        return MeshRecordKey.instance("D009369");
    }

    @Override public MeshRecordName getMeshName() {
        return MeshRecordName.instance("Neoplasms", MeshRecordType.DESCRIPTOR);
    }

    @Override public MeshRecordType getMeshType() {
        return MeshRecordType.DESCRIPTOR;
    }

    @Override public boolean isChemical() {
        return false;
    }
}
