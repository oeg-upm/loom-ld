package upm.oeg.loom.functions.similarities;

import info.debatty.java.stringsimilarity.Jaccard;
import upm.oeg.loom.enums.SimilarityAlgorithm;

/**
 * @author Wenqi
 */
public class JaccardSimilarity extends AbstractStringSimilarityFunction {
    private final Jaccard jaccard;

    public JaccardSimilarity() {
        super(SimilarityAlgorithm.JACCARD);
        jaccard = new Jaccard();
    }

    @Override
    public Double compareStrings(String element1, String element2) {
        return jaccard.similarity(element1, element2);
    }
}
