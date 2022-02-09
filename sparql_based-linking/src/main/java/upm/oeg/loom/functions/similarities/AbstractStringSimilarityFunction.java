package upm.oeg.loom.functions.similarities;

import org.apache.jena.ext.com.google.common.collect.Lists;
import org.apache.jena.sparql.expr.NodeValue;
import org.apache.jena.sparql.function.FunctionBase3;
import upm.oeg.loom.enums.SimilarityAlgorithm;

import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * inspired by Andrea Cimmino Arriaga
 *
 * @author Wenqi Jiang
 */
public abstract class AbstractStringSimilarityFunction extends FunctionBase3 implements StringSimilarity {

    private SimilarityAlgorithm algorithm;

    public AbstractStringSimilarityFunction(SimilarityAlgorithm algorithm) {
        this.algorithm = algorithm;
    }


    @Override
    public NodeValue exec(NodeValue v1, NodeValue v2, NodeValue v3) {
        String element1 = v1.getString();
        String element2 = v2.getString();
        // TODO tokenizing elements
        Double threshold = v3.getDouble();
        Double score = compareStrings(element1, element2, threshold);
        score = normalizing(score, threshold);
        return NodeValue.makeDouble(score);
    }

    /**
     * normalizing similarity score
     *
     * @param score     similarity score
     * @param threshold threshold
     * @return normalized score
     */
    private Double normalizing(Double score, Double threshold) {
        if (score == 1.0) {
            score = 1.0;
        } else if (score.equals(threshold)) {
            score = 0.01;
        } else if (score > threshold) {
            score = (score - threshold) / (1 - threshold);
        } else {
            score = (score / threshold) - 1;
        }
        return score;
    }

    @Override
    public Double compareStrings(List<String> strings1, List<String> strings2) {
        List<List<String>> stringPairs = Lists.cartesianProduct(strings1, strings2);
        return stringPairs.stream()
                .map(strings -> compareStrings(strings.get(0), strings.get(1)))
                .max(Comparator.comparing(Double::doubleValue))
                .orElseThrow(NoSuchElementException::new);
    }

    @Override
    public Double compareStrings(String element1, String element2, Double threshold) {
        return null;
    }

    public SimilarityAlgorithm getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(SimilarityAlgorithm algorithm) {
        this.algorithm = algorithm;
    }
}
