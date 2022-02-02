package loom.oeg.upm.utils;

import info.debatty.java.stringsimilarity.JaroWinkler;

/**
 * @author Wenqi Jiang
 */
public class StringUtil {
    private static final JaroWinkler STRING_SIMILARITY_TOOL = new JaroWinkler();


    /**
     *
     * @param text1
     * @param text2
     * @return The similarity between two texts
     */
    public static double similarity(String text1, String text2) {
        return STRING_SIMILARITY_TOOL.similarity(text1, text2);
    }
}