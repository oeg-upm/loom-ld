package loom.oeg.upm.examples;

import loom.oeg.upm.utils.StringUtil;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.sparql.expr.NodeValue;
import org.apache.jena.sparql.function.FunctionBase2;
import org.apache.jena.sparql.function.FunctionRegistry;
import org.apache.jena.sparql.syntax.Element;
import org.apache.jena.sparql.syntax.ElementFilter;
import org.apache.jena.sparql.syntax.ElementGroup;

/**
 * @author Wenqi
 */
public class CustomSimilarityFunction {
    public static void main(String[] args) {
        FunctionRegistry.get().put("http://example.org/functions#sm", SimilarityPercentage.class);
        String queryStr = "";

        Query query = QueryFactory.create(queryStr);
        Model init = ModelFactory.createDefaultModel();
        QueryExecution queryExecution = QueryExecutionFactory.create(query, init);
        Model model = queryExecution.execConstruct();
        model.write(System.out, "TURTLE");

        ElementGroup queryPattern = (ElementGroup) query.getQueryPattern();
        for (Element element : queryPattern.getElements()) {
            if (element instanceof ElementFilter) {
                ElementFilter filter = (ElementFilter) element;
                System.out.println(filter.getExpr());
            }
        }
    }

    private static class SimilarityPercentage extends FunctionBase2 {
        @Override
        public NodeValue exec(NodeValue v1, NodeValue v2) {
            return NodeValue.makeDouble(StringUtil.similarity(v1.toString(), v2.toString()));
        }
    }
}
