package upm.oeg.loom.functions.similarities;

import org.apache.jena.sparql.expr.NodeValue;
import org.apache.jena.sparql.function.FunctionBase2;
import upm.oeg.loom.enums.SimilarityAlgorithm;

/**
 * inspired by Andrea Cimmino Arriaga
 *
 * @author Wenqi Jiang
 */
public abstract class AbstractSimilarityFunction extends FunctionBase2 implements Similarity {

  private SimilarityAlgorithm algorithm;

  public AbstractSimilarityFunction(SimilarityAlgorithm algorithm) {
    this.algorithm = algorithm;
  }

  @Override
  public NodeValue exec(NodeValue v1, NodeValue v2) {
    String element1 = v1.asString();
    String element2 = v2.asString();
    Double score = similarity(element1, element2);
    return NodeValue.makeDouble(score);
  }

  public SimilarityAlgorithm getAlgorithm() {
    return algorithm;
  }

  public void setAlgorithm(SimilarityAlgorithm algorithm) {
    this.algorithm = algorithm;
  }
}
