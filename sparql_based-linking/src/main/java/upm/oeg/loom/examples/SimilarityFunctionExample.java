package upm.oeg.loom.examples;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import upm.oeg.loom.functions.CustomFunctions;

/**
 * This example is for test loom-ld similarity functions
 *
 * @author Wenqi
 */
public class SimilarityFunctionExample {
  public SimilarityFunctionExample() {
    CustomFunctions.loadSimilarityFunctions();
  }

  public static void main(String[] args) {

    String sparql3 =
        "PREFIX rdf:     <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
            + "PREFIX owl:     <http://www.w3.org/2002/07/owl#>\n"
            + "PREFIX loom:    <http://oeg.upm.es/loom-ld/functions/link#>"
            + "PREFIX foaf:    <http://xmlns.com/foaf/0.1/>\n"
            + "PREFIX dbo:    <http://dbpedia.org/ontology/>\n"
            + "PREFIX schema: <http://schema.org/>\n"
            + "CONSTRUCT {\n"
            + "   ?city1 owl:sameAs ?city2 .\n"
            + "}\n"
            + "WHERE {\n"
            + "   SERVICE <http://dbpedia-live.openlinksw.com/sparql?timeout=3000> { \n"
            + "     ?city1 rdf:type schema:City.\n"
            + "     ?city1 foaf:name ?cityName1. \n"
            + "   }"
            + "   SERVICE <http://dbpedia-live.openlinksw.com/sparql?timeout=3000> {\n"
            + "     ?city2 rdf:type dbo:City.\n"
            + "     ?city2 foaf:name ?cityName2. \n"
            + "   }\n"
            + "   FILTER ( loom:cosine(?cityName1, ?cityName2 ) > 0.8)\n"
            + "}\n"
            + "Limit 200";
    System.out.println(sparql3);
    Query query = QueryFactory.create(sparql3);
    try (QueryExecution qexec =
        QueryExecutionFactory.create(query, ModelFactory.createDefaultModel())) {
      Model model = qexec.execConstruct();
      model.write(System.out, "TURTLE");
    }
  }
}
