package upm.oeg.loom.examples;

import info.debatty.java.stringsimilarity.Cosine;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.RDFDataMgr;
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
//        String queryString =
//                "PREFIX linking:    <http://oeg.upm.es/loom-ld/functions/linking#>"
//                        + "SELECT * "
//                        + "WHERE { "
//                        + "    SERVICE <http://dbpedia-live.openlinksw.com/sparql?timeout=2000> { "
//                        + "        SELECT DISTINCT ?company where {?company a <http://dbpedia.org/ontology/Company>} LIMIT 20"
//                        + "    }"
//                        + "    SERVICE <http://dbpedia-live.openlinksw.com/sparql?timeout=2000> { "
//                        + "        SELECT DISTINCT ?company1 where {?company1 a <http://dbpedia.org/ontology/Company>} LIMIT 20"
//                        + "    }"
//                        + "BIND(linking:(?company, ?company1) AS ?grade)"
//                        + "}";
        String queryString = "PREFIX rdf:      <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
                + "PREFIX foaf:     <http://xmlns.com/foaf/0.1/>\n"
                + "PREFIX dbo:      <http://dbpedia.org/ontology/>\n"
                + "PREFIX wd:       <http://www.wikidata.org/entity/>\n"
                + "PREFIX wdt:      <http://www.wikidata.org/prop/direct/>\n"
                + "PREFIX linking:    <http://oeg.upm.es/loom-ld/functions/linking#>\n"
                + "SELECT ?name1 ?name2 ?similarity\n"
                + "WHERE {\n"
                + "   SERVICE <http://live.dbpedia.org/sparql?timeout=300000> {\n"
                + "     ?scientist1 rdf:type dbo:Scientist.\n"
                + "     ?scientist1 foaf:name ?name1. \n"
                + "   }\n"
                + "   SERVICE <https://query.wikidata.org/sparql?timeout=300000> {\n"
                + "     ?scientist2 wdt:P106 wd:Q901.\n"
                + "     ?scientist2 wdt:P1559 ?name2.\n"
                + "   }\n"
                + "   BIND(linking:(?name1, ?name2) AS ?similarity)\n"
                + "   FILTER ( ?similarity >= 0.5)\n"
                + "}\n"
                + "Limit 10";

        Model model = ModelFactory.createDefaultModel();
        Query query = QueryFactory.create(queryString);

        try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
            ResultSet results = qexec.execSelect();
            ResultSetFormatter.out(System.out, results, query);
        }
    }

    public static class CustomFunction extends FunctionBase2 {
        Cosine cosine = new Cosine();

        @Override
        public NodeValue exec(NodeValue v1, NodeValue v2) {
            return NodeValue.makeDouble(cosine.similarity(v1.asString(), v2.asString()));
        }
    }
}
