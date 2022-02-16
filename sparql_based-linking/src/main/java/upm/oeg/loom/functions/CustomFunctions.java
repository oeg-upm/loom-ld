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
  public static final FunctionRegistry FUNCTION_REGISTRY = FunctionRegistry.get();
  private static final String LINK = CustomARQConstants.LINK_PREFIX;

  public static void loadSimilarityFunctions() {
    add(LINK + SimilarityAlgorithm.COSINE, CosineSimilarityFunction.class);
    add(LINK + SimilarityAlgorithm.JACCARD, JaccardSimilarityFunction.class);
    add(LINK + SimilarityAlgorithm.JARO_WINKLER, JaroWinklerSimilarityFunction.class);
    add(LINK + SimilarityAlgorithm.LEVENSHTEIN, LevenshteinSimilarityFunction.class);
    add(LINK + SimilarityAlgorithm.RATCLIFF_OBERSHELP, RatcliffObershelpSimilarityFunction.class);
  }

  private static void add(String uri, Class<?> funcClass) {
    FUNCTION_REGISTRY.put(uri, funcClass);
  }
}
