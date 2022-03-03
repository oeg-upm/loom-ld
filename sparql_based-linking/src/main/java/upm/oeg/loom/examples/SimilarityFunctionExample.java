package upm.oeg.loom.examples;

import org.apache.jena.query.*;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.sparql.resultset.ResultsFormat;
import upm.oeg.loom.functions.CustomFunctions;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

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
    SimilarityFunctionExample example = new SimilarityFunctionExample();
    example.test2();
  }

  private void test() {

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

  private void test2() {
    String sparqlString =
        "PREFIX wd: <http://www.wikidata.org/entity/>"
            + "PREFIX wdt: <http://www.wikidata.org/prop/direct/>"
            + "PREFIX dbo: <http://dbpedia.org/ontology/> "
            + "PREFIX dbr: <http://dbpedia.org/resource/> "
            + "PREFIX loom:    <http://oeg.upm.es/loom-ld/functions/link#>"
            + "SELECT * WHERE {"
            + "  SERVICE <https://query.wikidata.org/sparql> {"
            + "    SELECT ?work1 WHERE {"
            + "      ?work1 wdt:P50 wd:Q159552 ."
            + "    }"
            + "  }"
            + "  SERVICE <http://DBpedia.org/sparql> {"
            + "    SELECT ?work2 WHERE {"
            + "      ?work2 dbo:author dbr:Johannes_V._Jensen ."
            + "    }"
            + "  }"
            + "BIND(loom:levenshtein(?work1, ?work2) AS ?levenshtein)"
            + "BIND(loom:cosine(?work1, ?work2) AS ?cosine)"
            + "}";
    Query query = QueryFactory.create(sparqlString);
    try (QueryExecution qexec =
        QueryExecutionFactory.create(query, ModelFactory.createDefaultModel())) {
      ResultSet rs = qexec.execSelect();


//            ResultSetFormatter.out(System.out, rs, query);
      OutputStream outputStream = new FileOutputStream("./test.rdf");
      ResultSetFormatter.output(outputStream, rs, ResultsFormat.FMT_RDF_NT);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }
}
