package upm.oeg.loom.functions.similarities;

import info.debatty.java.stringsimilarity.LongestCommonSubsequence;
import info.debatty.java.stringsimilarity.MetricLCS;
import upm.oeg.loom.enums.SimilarityAlgorithm;

/**
 * @author wenqi
 */
public class LCSSimilarityFunction extends AbstractSimilarityFunction {
    private final LongestCommonSubsequence lcs;

    public LCSSimilarityFunction() {
        super(SimilarityAlgorithm.LCS);
        lcs = new LongestCommonSubsequence();
    }

    @Override
    public Double similarity(String element1, String element2) {
        int maxLength = Math.max(element1.length(), element2.length());
        return lcs.length(element1, element2) * 1.0 / maxLength;
    }
}
