package upm.oeg.loom.functions.similarities;

import org.apache.jena.ext.com.google.common.collect.Lists;
import org.apache.jena.sparql.expr.NodeValue;
import org.apache.jena.sparql.function.FunctionBase2;
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
public abstract class AbstractStringSimilarityFunction extends FunctionBase2 implements StringSimilarity {

    private SimilarityAlgorithm algorithm;

    public AbstractStringSimilarityFunction(SimilarityAlgorithm algorithm) {
        this.algorithm = algorithm;
    }


    @Override
    public NodeValue exec(NodeValue v1, NodeValue v2) {
        String element1 = v1.getString();
        String element2 = v2.getString();
        Double score = compareStrings(element1, element2);
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


    public SimilarityAlgorithm getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(SimilarityAlgorithm algorithm) {
        this.algorithm = algorithm;
    }
}
