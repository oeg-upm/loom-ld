package upm.oeg.loom.functions.similarities;

/**
 * inspired by Andrea Cimmino Arriaga
 *
 * @author Wenqi Jiang
 */
public interface Similarity {
  /**
   * Get two strings' similarity
   *
   * @param element1 string 1
   * @param element2 string 2
   * @return Degree of similarity(0-1)
   */
  Double similarity(String element1, String element2);
}
