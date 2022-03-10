package upm.oeg.loom.utils;

import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;

/**
 * @author Vinci
 */
public class SPARQLExecutor {
    /**
     * print the result of select sparql query
     * @param sparql sparql query
     */
    public static void printSelectResult(String sparql) {
        System.out.println(sparql);
        Query query = QueryFactory.create(sparql);
        try (QueryExecution qexec =
                     QueryExecutionFactory.create(query, ModelFactory.createDefaultModel())) {
            ResultSet rs = qexec.execSelect();
            ResultSetFormatter.out(System.out, rs, query);
        }
    }

    /**
     * print the result of construct sparql query
     * @param sparql sparql query
     */
    public static void printConstructResult(String sparql) {
        System.out.println(sparql);
        Query query = QueryFactory.create(sparql);
        try (QueryExecution qexec =
                     QueryExecutionFactory.create(query, ModelFactory.createDefaultModel())) {
            Model model = qexec.execConstruct();
            model.write(System.out, "TURTLE");
        }
    }

}
