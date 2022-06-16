package upm.oeg.loom.examples;

import org.apache.jena.query.*;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.sparql.exec.http.QueryExecutionHTTP;
import upm.oeg.loom.utils.SparqlExecutor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Here are 3 different ways run sparql from remote databases
 *
 * @author Vinci
 */
public class HttpQueryExample {

    public static void main(String[] args) {
        HttpQueryExample queries = new HttpQueryExample();
        queries.getFromEndPoint();
        queries.getFromService();
        queries.getFromService("http://dbpedia-live.openlinksw.com/sparql");
    }

    private void getFromService() {
        String queryString =
                "SELECT * WHERE { "
                        + "    SERVICE <http://dbpedia-live.openlinksw.com/sparql?timeout=2000> { "
                        + "        SELECT DISTINCT ?company where {?company a <http://dbpedia.org/ontology/Company>} LIMIT 20"
                        + "    }"
                        + "}";
        SparqlExecutor.printResultSet(queryString);
    }

    private void getFromEndPoint() {
        String queryStr = "SELECT DISTINCT ?Concept where {[] a ?Concept} LIMIT 100";
        Query query = QueryFactory.create(queryStr);


        // Build-execute
        try (QueryExecution qExec =
                     QueryExecutionHTTP.create()
                             .endpoint("http://dbpedia.org/sparql")
//            .endpoint("http://datos.bne.es/sparql")
                             .query(query)
                             .param("timeout", "10000")
                             .build()) {
            // Execute.
            ResultSet rs = qExec.execSelect();
            ResultSetFormatter.out(System.out, rs, query);
        }
    }

    private void getFromService(String serviceURI) {
        String queryString =
                "SELECT * WHERE { "
                        + "    SERVICE <"
                        + serviceURI
                        + "> { "
                        + "        SELECT DISTINCT ?company where {?company a <http://dbpedia.org/ontology/Company>} LIMIT 20"
                        + "    }"
                        + "}";

        Query query = QueryFactory.create(queryString);

        // Local execution which uses SERBVICE for remote access.
        QueryExecutionDatasetBuilder.create().context(null);

        try (QueryExecution qexec =
                     QueryExecutionFactory.create(query, ModelFactory.createDefaultModel())) {
            Map<String, Map<String, List<String>>> serviceParams = new HashMap<>();
            Map<String, List<String>> params = new HashMap<>();
            List<String> values = new ArrayList<>();
            values.add("2000");
            params.put("timeout", values);
            serviceParams.put(serviceURI, params);
            qexec.getContext().set(ARQ.serviceParams, serviceParams);
            ResultSet rs = qexec.execSelect();
            ResultSetFormatter.out(System.out, rs, query);
        }
    }
}
