package upm.oeg.loom.examples;

import upm.oeg.loom.functions.CustomFunctions;
import upm.oeg.loom.utils.SPARQLExecutor;

/**
 * This example is for test loom-ld similarity functions
 *
 * @author Wenqi
 */
public class SimilarityFunctionExample {
  private static final String SPARQL1 =
      "PREFIX rdf:     <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
          + "PREFIX owl:     <http://www.w3.org/2002/07/owl#>\n"
          + "PREFIX loom:    <http://oeg.upm.es/loom-ld/functions/link#>\n"
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
          + "   }\n"
          + "   SERVICE <http://live.dbpedia.org/sparql?timeout=3000> {\n"
          + "     ?city2 rdf:type dbo:City.\n"
          + "     ?city2 foaf:name ?cityName2. \n"
          + "   }\n"
          + "   FILTER ( loom:levenshtein(?cityName1, ?cityName2 ) > 0.9)\n"
          + "}\n"
          + "Limit 20";
  private static final String SPARQL2 =
      "PREFIX rdf:      <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
          + "PREFIX owl:      <http://www.w3.org/2002/07/owl#>\n"
          + "PREFIX loom:     <http://oeg.upm.es/loom-ld/functions/link#>\n"
          + "PREFIX foaf:     <http://xmlns.com/foaf/0.1/>\n"
          + "PREFIX dbo:      <http://dbpedia.org/ontology/>\n"
          + "PREFIX wd:       <http://www.wikidata.org/entity/>\n"
          + "PREFIX wdt:      <http://www.wikidata.org/prop/direct/>\n"
          + "CONSTRUCT {\n"
          + "   ?scientist1 owl:sameAs ?name2 .\n"
          + "}\n"
          + "WHERE {\n"
          + "   SERVICE <http://live.dbpedia.org/sparql?timeout=3000> {\n"
          + "     ?scientist1 rdf:type dbo:Scientist.\n"
          + "     ?scientist1 foaf:name ?name1. \n"
          + "   }\n"
          + "   SERVICE <https://query.wikidata.org/sparql?timeout=3000> {\n"
          + "     ?scientist2 wdt:P106 wd:Q901.\n"
          + "     ?scientist2 wdt:P1559 ?name2.\n"
          + "   }\n"
          + "   FILTER ( loom:levenshtein(?name1, ?name2 ) > 0.85)\n"
          + "}\n"
          + "Limit 6";

  public static void main(String[] args) {
    CustomFunctions.loadSimilarityFunctions();

    SPARQLExecutor.printConstructResult(SPARQL2);
  }
}
