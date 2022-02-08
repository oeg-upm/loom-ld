package upm.oeg.loom.functions.similarities;

import info.debatty.java.stringsimilarity.Levenshtein;
import info.debatty.java.stringsimilarity.NormalizedLevenshtein;
import upm.oeg.loom.enums.SimilarityAlgorithm;

/**
 * @author Wenqi
 */
public class LevenshteinSimilarity extends AbstractStringSimilarityFunction {
    private final NormalizedLevenshtein levenshtein;

    public LevenshteinSimilarity() {
        super(SimilarityAlgorithm.LEVENSHTEIN);
        levenshtein = new NormalizedLevenshtein();
    }

    @Override
    public Double compareStrings(String element1, String element2) {
        return levenshtein.similarity(element1, element2);
    }
}
