package upm.oeg.loom.examples;

import org.apache.jena.graph.Graph;
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
import upm.oeg.loom.utils.SparqlExecutor;

/**
 * https://jena.apache.org/documentation/query/library-function.html
 * https://jena.apache.org/documentation/query/writing_functions.html
 * https://stackoverflow.com/questions/153724/how-to-round-a-number-to-n-decimal-places-in-java
 *
 * @author Wenqi Jiang
 */
public class CustomAggregateFunctionExample {
  /**
   * Custom aggregates use accumulators. One accumulator is created for each group in a query
   * execution.
   */
  public static AccumulatorFactory factory = (agg, distinct) -> new StatsAccumulator(agg);

  public static int sum = 0;

  public static void main(String[] args) {
    // Register the aggregate function
    AggregateRegistry.register("http://example/sumdev", factory, NodeConst.nodeMinusOne);

    // Add data
    Graph g =
        SSE.parseGraph(
            "(graph "
                + "(:item1 :hasPrice 123) "
                + "(:item2 :hasPrice 234) "
                + "(:item3 :hasPrice 456) "
                + "(:item4 :hasPrice 567) "
                + "(:item5 :hasPrice 678) "
                + "(:item6 :hasPrice 345) "
                + "(:item7 :hasPrice 789)"
                + ")");

    Model m = ModelFactory.createModelForGraph(g);
    String sparql =
        "PREFIX : <http://example/> \n"
            + "SELECT (:sumdev(?price) AS ?sum) \n"
            + "WHERE { ?item :hasPrice ?price }";

    SparqlExecutor.printResultSet(sparql, m);
  }

  private static class StatsAccumulator implements Accumulator {
    private final AggCustom AGG_CUSTOM;

    StatsAccumulator(AggCustom agg) {
      AGG_CUSTOM = agg;
    }

    @Override
    public void accumulate(Binding binding, FunctionEnv functionEnv) {
      // Add values to summaryStatistics
      final ExprList exprList = AGG_CUSTOM.getExprList();
      final NodeValue value = exprList.get(0).eval(binding, functionEnv);
      sum += Integer.parseInt(value.toString());
    }

    @Override
    public NodeValue getValue() {
      // Get the standard deviation
      return NodeValue.makeInteger(sum);
    }
  }
}
