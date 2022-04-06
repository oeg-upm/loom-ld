package upm.oeg.loom.utils;

import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.RDFDataMgr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Run basic SPARQL queries
 *
 * @author Wenqi Jiang
 */
public class SparqlExecutor {
  private static final Logger LOGGER = LoggerFactory.getLogger(SparqlExecutor.class);
  private static Query query;
  private static QueryExecution qexec;

  /**
   * print the result of select sparql query
   *
   * @param sparql sparql query
   */
  public static void printResultSet(String sparql) {
    LOGGER.info("run sparql query:\n{} ", sparql);
    query = QueryFactory.create(sparql);
    qexec = QueryExecutionFactory.create(query, ModelFactory.createDefaultModel());
    ResultSet rs = qexec.execSelect();
    ResultSetFormatter.out(System.out, rs, query);
  }

  public static void printResultSet(String sparql, String filename) {

    LOGGER.info("run sparql query:\n{} from the source: {} ", sparql, filename);
    printResultSet(sparql, RDFDataMgr.loadModel(filename));
  }

  public static void printResultSet(String sparql, Model source) {
    LOGGER.info("run sparql query:\n{} ", sparql);
    query = QueryFactory.create(sparql);
    qexec = QueryExecutionFactory.create(query, source);
    ResultSet rs = qexec.execSelect();
    ResultSetFormatter.out(System.out, rs, query);
  }

  /**
   * print the result of construct sparql query
   *
   * @param sparql sparql query
   */
  public static void printModel(String sparql) {
    LOGGER.info("run sparql query:\n{} ", sparql);
    query = QueryFactory.create(sparql);
    qexec = QueryExecutionFactory.create(query, ModelFactory.createDefaultModel());
    Model model = qexec.execConstruct();
    model.write(System.out, "TURTLE");
  }

  /**
   * Get the construct from source with sparql query
   *
   * @param sparql sparql query
   * @param source source to be queried
   * @return the model / construct
   */
  public static Model getModel(String sparql, Model source) {
    LOGGER.info("run sparql query:\n{} ", sparql);
    query = QueryFactory.create(sparql);
    qexec = QueryExecutionFactory.create(query, source);
    return qexec.execConstruct();
  }

  /**
   * Get the construct from source file with sparql query
   *
   * @param sparql sparql query
   * @param filename source file to be queried
   * @return the model / construct
   */
  public static Model getModel(String sparql, String filename) {
    LOGGER.info("run sparql query:\n{} from the file {}:", sparql, filename);
    Model source = RDFDataMgr.loadModel(filename);
    return getModel(sparql, source);
  }
}
