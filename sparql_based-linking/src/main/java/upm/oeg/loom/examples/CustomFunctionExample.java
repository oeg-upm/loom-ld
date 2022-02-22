package upm.oeg.loom.examples;

import org.apache.jena.query.*;
import org.apache.jena.rdf.model.ModelFactory;
import upm.oeg.loom.functions.CustomFunctions;

/** @author Wenqi */
public class CustomFunctionExample {
  public static void main(String[] args) {
    CustomFunctions.loadSimilarityFunctions();
    String queryString =
        "PREFIX loom:    <http://oeg.upm.es/loom-ld/functions/link#>"
            + "SELECT * WHERE { "
            + "    SERVICE <http://dbpedia-live.openlinksw.com/sparql?timeout=2000> { "
            + "        SELECT DISTINCT ?company where {?company a <http://dbpedia.org/ontology/Company>} LIMIT 20"
            + "    }"
            + "    SERVICE <http://dbpedia-live.openlinksw.com/sparql?timeout=2000> { "
            + "        SELECT DISTINCT ?company1 where {?company1 a <http://dbpedia.org/ontology/Company>} LIMIT 20"
            + "    }"
            + "BIND(loom:levenshtein(?company, ?company1) AS ?grade)"
            + "}";

    Query query = QueryFactory.create(queryString);
    try (QueryExecution qexec =
        QueryExecutionFactory.create(query, ModelFactory.createDefaultModel())) {
      ResultSet rs = qexec.execSelect();
      ResultSetFormatter.out(System.out, rs, query);
    }
  }
}
