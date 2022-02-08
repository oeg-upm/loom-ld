package upm.oeg.loom.functions.similarities;

import info.debatty.java.stringsimilarity.RatcliffObershelp;
import upm.oeg.loom.enums.SimilarityAlgorithm;

/**
 * @author Wenqi
 */
public class RatcliffObershelpSimilarity extends AbstractStringSimilarityFunction{
    private final RatcliffObershelp ratcliffObershelp;

    public RatcliffObershelpSimilarity() {
        super(SimilarityAlgorithm.RATCLIFF_OBERSHELP);
        ratcliffObershelp = new RatcliffObershelp();
    }

    @Override
    public Double compareStrings(String element1, String element2) {
        return ratcliffObershelp.similarity(element1,element2);
    }
}
