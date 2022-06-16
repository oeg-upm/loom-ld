package upm.oeg.loom.functions.similarities;

import info.debatty.java.stringsimilarity.MetricLCS;
import upm.oeg.loom.enums.SimilarityAlgorithm;

/**
 * MetricLCS
 * @author wenqi
 */
public class LCSSimilarityFunction extends AbstractSimilarityFunction {
    private final MetricLCS lcs;

    public LCSSimilarityFunction() {
        super(SimilarityAlgorithm.JARO_WINKLER);
        lcs = new MetricLCS();
    }

    @Override
    public Double similarity(String element1, String element2) {
        return lcs.distance(element1, element2);
    }
}
