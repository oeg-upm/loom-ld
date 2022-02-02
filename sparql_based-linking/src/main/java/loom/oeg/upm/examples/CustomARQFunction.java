package loom.oeg.upm.examples;

import org.apache.commons.math3.stat.descriptive.SummaryStatistics;
import org.apache.jena.graph.Graph;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.sparql.engine.binding.Binding;
import org.apache.jena.sparql.expr.ExprList;
import org.apache.jena.sparql.expr.NodeValue;
import org.apache.jena.sparql.expr.aggregate.Accumulator;
import org.apache.jena.sparql.expr.aggregate.AccumulatorFactory;
import org.apache.jena.sparql.expr.aggregate.AggCustom;
import org.apache.jena.sparql.expr.aggregate.AggregateRegistry;
import org.apache.jena.sparql.function.FunctionEnv;
import org.apache.jena.sparql.graph.NodeConst;
import org.apache.jena.sparql.sse.SSE;

import java.text.DecimalFormat;

/**
 * https://jena.apache.org/documentation/query/library-function.html
 * https://jena.apache.org/documentation/query/writing_functions.html
 * https://stackoverflow.com/questions/153724/how-to-round-a-number-to-n-decimal-places-in-java
 *
 * @author Wenqi Jiang
 */
public class CustomARQFunction {
    /**
     * Custom aggregates use accumulators. One accumulator is created for each group in a query execution.
     */
    public static AccumulatorFactory factory = (agg, distinct) -> new StatsAccumulator(agg);

    public static void main(String[] args) {
        // Register the aggregate function
        AggregateRegistry.register("http://example/stddev", factory, NodeConst.nodeMinusOne);

        // Add data
        Graph g = SSE.parseGraph(
                "(graph " +
                        "(:item1 :hasPrice 123) " +
                        "(:item2 :hasPrice 234) " +
                        "(:item3 :hasPrice 456) " +
                        "(:item4 :hasPrice 567) " +
                        "(:item5 :hasPrice 678) " +
                        "(:item6 :hasPrice 345) " +
                        "(:item7 :hasPrice 789)" +
                        ")"
        );

        Model m = ModelFactory.createModelForGraph(g);
        String sparql = "PREFIX : <http://example/> " +
                "SELECT (:stddev(?price) AS ?stddev) " +
                "WHERE { ?item :hasPrice ?price }";

        // Execute query and print results
        Query q = QueryFactory.create(sparql);
        QueryExecution qexec = QueryExecutionFactory.create(q, m);
        ResultSet rs = qexec.execSelect();
        ResultSetFormatter.out(rs);
    }

    private static class StatsAccumulator implements Accumulator {
        private final AggCustom AGG_CUSTOM;
        private final SummaryStatistics SUMMARY_STATISTICS = new SummaryStatistics();
        private final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("##.####");

        StatsAccumulator(AggCustom agg) {
            AGG_CUSTOM = agg;
        }

        @Override
        public void accumulate(Binding binding, FunctionEnv functionEnv) {
            // Add values to summaryStatistics
            final ExprList exprList = AGG_CUSTOM.getExprList();
            final NodeValue value = exprList.get(0).eval(binding, functionEnv);
            SUMMARY_STATISTICS.addValue(value.getDouble());
        }

        @Override
        public NodeValue getValue() {
            // Get the standard deviation
            return NodeValue.makeNodeDouble(DECIMAL_FORMAT.format(SUMMARY_STATISTICS.getStandardDeviation()));
        }
    }
}
