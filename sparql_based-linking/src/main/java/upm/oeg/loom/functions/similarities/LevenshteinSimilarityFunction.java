package upm.oeg.loom.functions.similarities;

import info.debatty.java.stringsimilarity.NormalizedLevenshtein;
import upm.oeg.loom.enums.SimilarityAlgorithm;

/**
 * @author Wenqi
 */
public class LevenshteinSimilarityFunction extends AbstractSimilarityFunction {
    private final NormalizedLevenshtein levenshtein;

    public LevenshteinSimilarityFunction() {
        super(SimilarityAlgorithm.LEVENSHTEIN);
        levenshtein = new NormalizedLevenshtein();
    }

    @Override
    public Double similarity(String element1, String element2) {
        return levenshtein.similarity(element1, element2);
    }
}
