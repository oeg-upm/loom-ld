package upm.oeg.loom.functions;

import org.apache.jena.sparql.function.FunctionRegistry;
import upm.oeg.loom.enums.CustomARQConstants;
import upm.oeg.loom.enums.SimilarityAlgorithm;
import upm.oeg.loom.functions.similarities.*;

/**
 * Standard functions library
 *
 * @author Wenqi
 */
public class CustomFunctions {
  private static final String LINK = CustomARQConstants.LINK_PREFIX;

  public static void loadSimilarityFunctions() {
    put(LINK + SimilarityAlgorithm.COSINE, CosineSimilarityFunction.class);
    put(LINK + SimilarityAlgorithm.JACCARD, JaccardSimilarityFunction.class);
    put(LINK + SimilarityAlgorithm.JARO_WINKLER, JaroWinklerSimilarityFunction.class);
    put(LINK + SimilarityAlgorithm.LEVENSHTEIN, LevenshteinSimilarityFunction.class);
    put(LINK + SimilarityAlgorithm.RATCLIFF_OBERSHELP, RatcliffObershelpSimilarityFunction.class);
  }

  private static void put(String uri, Class<?> funcClass) {
    FunctionRegistry.get().put(uri, funcClass);
  }
}
