package upm.oeg.loom.functions.similarities;

import info.debatty.java.stringsimilarity.RatcliffObershelp;
import upm.oeg.loom.enums.SimilarityAlgorithm;

/**
 * @author Wenqi
 */
public class RatcliffObershelpSimilarityFunction extends AbstractSimilarityFunction {
    private final RatcliffObershelp ratcliffObershelp;

    public RatcliffObershelpSimilarityFunction() {
        super(SimilarityAlgorithm.RATCLIFF_OBERSHELP);
        ratcliffObershelp = new RatcliffObershelp();
    }

    @Override
    public Double similarity(String element1, String element2) {
        return ratcliffObershelp.similarity(element1,element2);
    }
}
