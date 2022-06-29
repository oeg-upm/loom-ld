package upm.oeg.loom.examples;

import org.apache.commons.io.IOUtils;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.RDFParser;
import org.apache.jena.riot.RDFParserBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author wenqi
 */
public class TestDemo {
    private static final String RDF = "@prefix github: <https://github.com/oeg-upm/> .\n" +
            "@prefix oeg:    <https://oeg.fi.upm.es/researcher#> .\n" +
            "@prefix vcard:  <http://www.w3.org/2006/vcard/ns#> .\n" +
            "oeg:wenqi  vcard:hasName  \"Wenqi Jiang\" ;\n" +
            "        vcard:hasProject  github:LOOM-LD .\n" +
            "github:LOOM-LD  vcard:hasName  \"LOOM-LD\" .";
    private static final String SPARQL = "PREFIX oeg: <https://oeg.fi.upm.es/researcher#>\n" +
            "PREFIX github: <https://github.com/oeg-upm/>\n" +
            "PREFIX vcard: <http://www.w3.org/2006/vcard/ns#>\n" +
            "SELECT ?name ?projectName\n" +
            "WHERE {\n" +
            "    ?researcher vcard:hasProject ?project .\n" +
            "    ?researcher vcard:hasName ?name .\n" +
            "    ?project vcard:hasName ?projectName .\n" +
            "}";

    public static void main(String[] args) {
        Model model = ModelFactory
                .createDefaultModel()
                .read(IOUtils.toInputStream(RDF, StandardCharsets.UTF_8), null, "TURTLE");
        Query query = QueryFactory.create(SPARQL);

        try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
            ResultSet results = qexec.execSelect();
            ResultSetFormatter.out(System.out, results, query);
        }
    }
}
