package upm.oeg.loom.examples;

import info.debatty.java.stringsimilarity.Cosine;
import org.apache.jena.sparql.expr.NodeValue;
import org.apache.jena.sparql.function.FunctionBase2;
import org.apache.jena.sparql.function.FunctionRegistry;
import upm.oeg.loom.utils.SparqlExecutor;

/**
 * This example is for testing how to register a custom function, which has two parameters
 *
 * @author Wenqi
 */
public class CustomFunctionExample {

  public static void main(String[] args) {
    FunctionRegistry ref = FunctionRegistry.get();
    ref.put("http://oeg.upm.es/loom-ld/functions/linking#", CustomFunction.class);
    String queryString =
        "PREFIX linking:    <http://oeg.upm.es/loom-ld/functions/linking#>"
            + "SELECT * WHERE { "
            + "    SERVICE <http://dbpedia-live.openlinksw.com/sparql?timeout=2000> { "
            + "        SELECT DISTINCT ?company where {?company a <http://dbpedia.org/ontology/Company>} LIMIT 20"
            + "    }"
            + "    SERVICE <http://dbpedia-live.openlinksw.com/sparql?timeout=2000> { "
            + "        SELECT DISTINCT ?company1 where {?company1 a <http://dbpedia.org/ontology/Company>} LIMIT 20"
            + "    }"
            + "BIND(linking:(?company, ?company1) AS ?grade)"
            + "}";

    SparqlExecutor.printResultSet(queryString);
  }

  public static class CustomFunction extends FunctionBase2 {
    Cosine cosine = new Cosine();

    @Override
    public NodeValue exec(NodeValue v1, NodeValue v2) {
      return NodeValue.makeString(String.valueOf(cosine.similarity(v1.asString(), v2.asString())));
    }
  }
}
