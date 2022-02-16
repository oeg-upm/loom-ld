package upm.oeg.loom.examples;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import upm.oeg.loom.functions.CustomFunctions;

/** @author Wenqi */
public class CustomFunctionExample {
  public static void main(String[] args) {
    CustomFunctions.loadSimilarityFunctions();
    String queryStr =
        "PREFIX :        <http://www.semanticweb.org/chetan/ontologies/2014/5/untitled-ontology-11#>\n"
            + "PREFIX owl:     <http://www.w3.org/2002/07/owl#>\n"
            + "PREFIX rdf:     <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
            + "PREFIX rdfs:    <http://www.w3.org/2000/01/rdf-schema#>\n"
            + "PREFIX foaf:    <http://xmlns.com/foaf/0.1/>\n"
            + "PREFIX loom:    <http://oeg.upm.es/loom-ld/functions/link#>\n"
            + "SELECT ?name1 ?name2 ?score WHERE {\n"
            + "  SERVICE <> {\n"
            + "    ?subject1 rdfs:label ?name1 .\n"
            + "  }\n"
            + "\n"
            + "  SERVICE <> {\n"
            + "    ?subject2  rdfs:label ?name2 .\n"
            + "  }\n"
            + "  BIND ( loom:levenshtain(?name1, ?name2) AS ?score )\n"
            + "  FILTER (  ?score > 2 ) .\n"
            + "} ORDER BY ?score";

    Query query = QueryFactory.create(queryStr);
    Model init = ModelFactory.createDefaultModel();
    QueryExecution queryExecution = QueryExecutionFactory.create(query, init);
    Model model = queryExecution.execConstruct();
    model.write(System.out, "TURTLE");
  }
}
