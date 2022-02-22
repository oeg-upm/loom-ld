package upm.oeg.loom.functions.similarities;

import info.debatty.java.stringsimilarity.Jaccard;
import upm.oeg.loom.enums.SimilarityAlgorithm;

/** @author Wenqi */
public class JaccardSimilarityFunction extends AbstractSimilarityFunction {
  private final Jaccard jaccard;

  public JaccardSimilarityFunction() {
    super(SimilarityAlgorithm.JACCARD);
    jaccard = new Jaccard();
  }

  @Override
  public Double similarity(String element1, String element2) {
    return jaccard.similarity(element1, element2);
  }
}
