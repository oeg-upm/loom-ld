package upm.oeg.loom.utils;

import opennlp.tools.tokenize.WhitespaceTokenizer;

import java.util.List;

/**
 * https://www.baeldung.com/apache-open-nlp
 * @author Wenqi
 */
public class TextUtil {

    /**
     * split text into string list by whitespace
     * @param text text
     * @return String list
     */
    public static List<String> tokenizeByWhitespace(String text) {
        return List.of(WhitespaceTokenizer.INSTANCE.tokenize(text));
    }


}
