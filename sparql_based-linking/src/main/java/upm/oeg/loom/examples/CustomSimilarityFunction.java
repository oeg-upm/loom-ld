package upm.oeg.loom.examples;

import info.debatty.java.stringsimilarity.Jaccard;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.sparql.expr.NodeValue;
import org.apache.jena.sparql.function.FunctionBase2;
import org.apache.jena.sparql.function.FunctionRegistry;
import org.apache.jena.sparql.syntax.Element;
import org.apache.jena.sparql.syntax.ElementFilter;
import org.apache.jena.sparql.syntax.ElementGroup;

/**
 * @author Wenqi
 */
public class CustomSimilarityFunction {
    public static void main(String[] args) {
        FunctionRegistry.get().put("http://loom.oeg.upm.es/functions#sm", SimilarityPercentage.class);
    String queryStr =
        "PREFIX :        <http://www.semanticweb.org/chetan/ontologies/2014/5/untitled-ontology-11#>\n"
            + "PREFIX owl:     <http://www.w3.org/2002/07/owl#>\n"
            + "PREFIX rdf:     <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
            + "PREFIX rdfs:    <http://www.w3.org/2000/01/rdf-schema#>\n"
            + "PREFIX foaf:    <http://xmlns.com/foaf/0.1/>\n"
            + "PREFIX loom:    <http://loom.oeg.upm.es/functions>\n"
            + "SELECT ?name1 ?name2 ?score WHERE {\n"
            + "  SERVICE < > {\n"
            + "    ?subject1 rdfs:label ?name1 .\n"
            + "  }\n"
            + "\n"
            + "  SERVICE <> {\n"
            + "    ?subject2  rdfs:label ?name2 .\n"
            + "  }\n"
            + "  BIND ( levenshtain(?name1, ?name2) AS ?score )\n"
            + "  FILTER (  ?score > 2 ) .\n"
            + "} ORDER BY ?score";

        Query query = QueryFactory.create(queryStr);
        Model init = ModelFactory.createDefaultModel();
        QueryExecution queryExecution = QueryExecutionFactory.create(query, init);
        Model model = queryExecution.execConstruct();
        model.write(System.out, "TURTLE");

        ElementGroup queryPattern = (ElementGroup) query.getQueryPattern();
        for (Element element : queryPattern.getElements()) {
            if (element instanceof ElementFilter) {
                ElementFilter filter = (ElementFilter) element;
                System.out.println(filter.getExpr());
            }
        }
    }

    private static class SimilarityPercentage extends FunctionBase2 {
        @Override
        public NodeValue exec(NodeValue v1, NodeValue v2) {
            Jaccard jaccard = new Jaccard();
            return NodeValue.makeDouble(jaccard.similarity(v1.toString(), v2.toString()));
        }
    }
}
