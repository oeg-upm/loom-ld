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
    private static final String TEXT_PREFIX = CustomARQConstants.TEXT_PREFIX;
    private static final String GEOMETRY_PREFIX = CustomARQConstants.GEOMETRY_PREFIX;
    private static final FunctionRegistry FUNCTION_REGISTRY = FunctionRegistry.get();

    private CustomFunctions() {
    }

    public static void loadTextFunctions() {
        FUNCTION_REGISTRY.put(TEXT_PREFIX + SimilarityAlgorithm.COSINE, CosineSimilarityFunction.class);
        FUNCTION_REGISTRY.put(TEXT_PREFIX + SimilarityAlgorithm.JACCARD, JaccardSimilarityFunction.class);
        FUNCTION_REGISTRY.put(TEXT_PREFIX + SimilarityAlgorithm.JARO_WINKLER, JaroWinklerSimilarityFunction.class);
        FUNCTION_REGISTRY.put(TEXT_PREFIX + SimilarityAlgorithm.LEVENSHTEIN, LevenshteinSimilarityFunction.class);
        FUNCTION_REGISTRY.put(TEXT_PREFIX + SimilarityAlgorithm.LCS, LCSSimilarityFunction.class);
        FUNCTION_REGISTRY.put(TEXT_PREFIX + SimilarityAlgorithm.RATCLIFF_OBERSHELP, RatcliffObershelpSimilarityFunction.class);
    }

    public static void loadGeometryFunctions() {
        FUNCTION_REGISTRY.put(GEOMETRY_PREFIX + GeometryRelation.CONTAINS, GeometryContainsFunction.class);
        FUNCTION_REGISTRY.put(GEOMETRY_PREFIX + GeometryRelation.COVERED_BY, GeometryCoveredByFunction.class);
        FUNCTION_REGISTRY.put(GEOMETRY_PREFIX + GeometryRelation.COVERS, GeometryCoversFunction.class);
        FUNCTION_REGISTRY.put(GEOMETRY_PREFIX + GeometryRelation.DISJOINT, GeometryDisjointFunction.class);
        FUNCTION_REGISTRY.put(GEOMETRY_PREFIX + GeometryRelation.EQUALS, GeometryEqualsFunction.class);
        FUNCTION_REGISTRY.put(GEOMETRY_PREFIX + GeometryRelation.INTERSECTS, GeometryIntersectsFunction.class);
        FUNCTION_REGISTRY.put(GEOMETRY_PREFIX + GeometryRelation.OVERLAPS, GeometryOverlapsFunction.class);
        FUNCTION_REGISTRY.put(GEOMETRY_PREFIX + GeometryRelation.TOUCHES, GeometryTouchesFunction.class);
        FUNCTION_REGISTRY.put(GEOMETRY_PREFIX + GeometryRelation.CROSSES, GeometryCrossesFunction.class);
        FUNCTION_REGISTRY.put(GEOMETRY_PREFIX + GeometryRelation.WITHIN, GeometryWithinFunction.class);
    }


}
