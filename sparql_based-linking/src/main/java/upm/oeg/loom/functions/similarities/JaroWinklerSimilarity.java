package upm.oeg.loom.functions.similarities;

import info.debatty.java.stringsimilarity.JaroWinkler;
import upm.oeg.loom.enums.SimilarityAlgorithm;

/**
 * @author Wenqi
 */
public class JaroWinklerSimilarity extends AbstractStringSimilarityFunction{
    private final JaroWinkler jaroWinkler;

    public JaroWinklerSimilarity() {
        super(SimilarityAlgorithm.JARO_WINKLER);
        jaroWinkler = new JaroWinkler();
    }

    @Override
    public Double compareStrings(String element1, String element2) {
        return jaroWinkler.similarity(element1, element2);
    }
}
