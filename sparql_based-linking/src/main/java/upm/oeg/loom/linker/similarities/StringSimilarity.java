package upm.oeg.loom.linker.similarities;

import java.util.List;

/**
 *
 * inspired by Andrea Cimmino Arriaga
 * @author Wenqi Jiang
 */
public interface StringSimilarity {
    /**
     * Get two strings' similarity
     * @param element1 string 1
     * @param element2 string 2
     * @return Degree of similarity(0-1)
     */
    public Double compareStrings(String element1, String element2);


    /**
     * @param strings1
     * @param strings2
     * @return
     */
    public Double compareStrings(List<String> strings1, List<String> strings2);

    /**
     * @param element1
     * @param element2
     * @param threshold
     * @return
     */
    public Double compareStrings(String element1, String element2, Double threshold);


}
