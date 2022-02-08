package upm.oeg.loom.linker.similarities;

import org.apache.jena.sparql.expr.NodeValue;
import org.apache.jena.sparql.function.FunctionBase3;

/**
 * inspired by Andrea Cimmino Arriaga
 * @author Wenqi Jiang
 *
 */
public abstract class AbstractStringSimilarityFunction extends FunctionBase3 implements StringSimilarity {

    @Override
    public NodeValue exec(NodeValue v1, NodeValue v2, NodeValue v3) {
        String element1 = v1.getString();
        String element2 = v2.getString();
        Double threshold = v3.getDouble();
        Double score = 0.0;

        try{
           score = compareStrings(element1, element2, threshold);
        } catch (Exception ex) {

        }

        return NodeValue.makeDouble(score);
    }

    @Override
    public Double compareStrings(String element1, String element2) {

        return null;
    }

    @Override
    public Double compareStrings(String element1, String element2, Double threshold) {
        return null;
    }
}
