package upm.oeg.loom.functions.similarities;

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




}
