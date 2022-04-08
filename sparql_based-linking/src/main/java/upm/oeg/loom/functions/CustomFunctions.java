package upm.oeg.loom.functions;

import org.apache.jena.sparql.function.FunctionRegistry;
import upm.oeg.loom.enums.CustomARQConstants;
import upm.oeg.loom.enums.GeometryRelation;
import upm.oeg.loom.enums.SimilarityAlgorithm;
import upm.oeg.loom.functions.geometries.*;
import upm.oeg.loom.functions.similarities.*;

/**
 * Standard functions library
 *
 * @author Wenqi
 */
public class CustomFunctions {
  private static final String LINK = CustomARQConstants.LINK_PREFIX;
  private static final String GEOMETRY_PREFIX = CustomARQConstants.GEOMETRY_PREFIX;

  public static void loadSimilarityFunctions() {
    put(LINK + SimilarityAlgorithm.COSINE, CosineSimilarityFunction.class);
    put(LINK + SimilarityAlgorithm.JACCARD, JaccardSimilarityFunction.class);
    put(LINK + SimilarityAlgorithm.JARO_WINKLER, JaroWinklerSimilarityFunction.class);
    put(LINK + SimilarityAlgorithm.LEVENSHTEIN, LevenshteinSimilarityFunction.class);
    put(LINK + SimilarityAlgorithm.RATCLIFF_OBERSHELP, RatcliffObershelpSimilarityFunction.class);
  }

  public static void loadGeometryFunctions() {
    put(GEOMETRY_PREFIX + GeometryRelation.CONTAINS, GeometryContainsFunction.class);
    put(GEOMETRY_PREFIX + GeometryRelation.COVERED_BY, GeometryCoveredByFunction.class);
    put(GEOMETRY_PREFIX + GeometryRelation.COVERS, GeometryCoversFunction.class);
    put(GEOMETRY_PREFIX + GeometryRelation.DISJOINT, GeometryDisjointFunction.class);
    put(GEOMETRY_PREFIX + GeometryRelation.EQUALS, GeometryEqualsFunction.class);
    put(GEOMETRY_PREFIX + GeometryRelation.INTERSECTS, GeometryIntersectsFunction.class);
    put(GEOMETRY_PREFIX + GeometryRelation.OVERLAPS, GeometryOverlapsFunction.class);
    put(GEOMETRY_PREFIX + GeometryRelation.TOUCHES, GeometryTouchesFunction.class);
    put(GEOMETRY_PREFIX + GeometryRelation.WITHIN, GeometryWithinFunction.class);
  }

  private static void put(String uri, Class<?> funcClass) {
    FunctionRegistry.get().put(uri, funcClass);
  }
}
