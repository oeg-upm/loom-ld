package upm.oeg.loom.functions.similarities;

import info.debatty.java.stringsimilarity.Cosine;
import upm.oeg.loom.enums.SimilarityAlgorithm;

public class CosineSimilarityFunction extends AbstractSimilarityFunction {

  private final Cosine cosine;

  public CosineSimilarityFunction() {
    super(SimilarityAlgorithm.COSINE);
    cosine = new Cosine();
  }

  @Override
  public Double similarity(String element1, String element2) {
    return cosine.similarity(element1, element2);
  }
}
