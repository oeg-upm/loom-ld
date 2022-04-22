package upm.oeg.loom.tasks;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.riot.RDFDataMgr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import upm.oeg.loom.functions.CustomFunctions;
import upm.oeg.loom.utils.SparqlExecutor;

import java.io.File;

/**
 * https://hobbit-project.github.io/OAEI_2021.html RDF API:
 * https://jena.apache.org/tutorials/rdf_api.html
 *
 * @author Wenqi
 */
public class SPIMBENCHLinking {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpatenLinking.class);
    private static final String TBOX_1_FILENAME = "SPIMBENCH/SPIMBENCH_large/Tbox1.nt";
    private static final String TBOX_2_FILENAME = "SPIMBENCH/SPIMBENCH_large/Tbox2.nt";

    private static final String REFALIGN_FILENAME = "SPIMBENCH/SPIMBENCH_small/refalign.rdf";

    private static final String RESULT_FILENAME = "src/main/resources/SPIMBENCH/result.nt";
    private static final String MATRIX_FILENAME = "src/main/resources/SPIMBENCH/confusion_matrix.json";
    private static final String SPARQL1 =
            "PREFIX rdf:     <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
                    + "PREFIX rdfs:     <http://www.w3.org/2000/01/rdf-schema#>\n"
                    + "PREFIX loom:     <http://oeg.upm.es/loom-ld/functions/link#>\n"
                    + "PREFIX cwork:    <http://www.bbc.co.uk/ontologies/creativework/>\n"
                    + "PREFIX seals:    <http://www.seals-project.eu/ontologies/SEALSMetadata.owl#>\n"
                    + "CONSTRUCT {\n"
                    + "  ?thing rdf:type ?work .\n"
                    + "  ?thing cwork:title ?title .\n"
                    + "  ?thing cwork:shortTitle ?shortTitle .\n"
                    + "  ?thing cwork:description ?description .\n"
                    + "  ?thing seals:isLocatedAt \"tbox1.nt\" \n"
                    + "}\n"
                    + "WHERE {\n"
                    + "  ?thing rdf:type ?work .\n"
                    + "  ?thing cwork:title ?title .\n"
                    + "  ?thing cwork:shortTitle ?shortTitle .\n"
                    + "  ?thing cwork:description ?description .\n"
                    + "  FILTER(?work IN (cwork:NewsItem, cwork:BlogPost, cwork:Programme))\n"
                    + "}\n";
    private static final String SPARQL2 =
            "PREFIX rdf:     <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
                    + "PREFIX rdfs:     <http://www.w3.org/2000/01/rdf-schema#>\n"
                    + "PREFIX loom:     <http://oeg.upm.es/loom-ld/functions/link#>\n"
                    + "PREFIX cwork:    <http://www.bbc.co.uk/ontologies/creativework/>\n"
                    + "PREFIX seals:    <http://www.seals-project.eu/ontologies/SEALSMetadata.owl#>\n"
                    + "CONSTRUCT {\n"
                    + "  ?thing rdf:type ?work .\n"
                    + "  ?thing cwork:title ?title .\n"
                    + "  ?thing cwork:shortTitle ?shortTitle .\n"
                    + "  ?thing cwork:description ?description .\n"
                    + "  ?thing seals:isLocatedAt \"tbox2.nt\" \n"
                    + "}\n"
                    + "WHERE {\n"
                    + "  ?thing rdf:type ?work .\n"
                    + "  ?thing cwork:title ?title .\n"
                    + "  ?thing cwork:shortTitle ?shortTitle .\n"
                    + "  ?thing cwork:description ?description .\n"
                    + "  FILTER(?work IN (cwork:NewsItem, cwork:BlogPost, cwork:Programme))\n"
                    + "}\n";

    private static final String RESULT_SPARQL =
            "PREFIX rdf:     <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
                    + "PREFIX rdfs:     <http://www.w3.org/2000/01/rdf-schema#>\n"
                    + "PREFIX owl:      <http://www.w3.org/2002/07/owl#>\n"
                    + "PREFIX loom:     <http://oeg.upm.es/loom-ld/functions/link#>\n"
                    + "PREFIX cwork:    <http://www.bbc.co.uk/ontologies/creativework/>\n"
                    + "PREFIX seals:    <http://www.seals-project.eu/ontologies/SEALSMetadata.owl#>\n"
                    + "CONSTRUCT {\n"
                    + "  ?thing1 owl:sameAs ?thing2 .\n"
                    //            + "  ?thing1 cwork:title ?title1 .\n"
                    //            + "  ?thing2 cwork:title ?title2 \n"
                    + "}\n"
                    + "WHERE {\n"
                    + "  ?thing1 seals:isLocatedAt \"tbox1.nt\" .\n"
                    + "  ?thing1 cwork:title ?title1 .\n"
                    + "  ?thing1 seals:isLocatedAt \"tbox2.nt\" .\n"
                    + "  ?thing2 cwork:title ?title2 .\n"
                    + "  BIND(loom:jaro-winkler(?title1, ?title2 ) AS ?grade)"
                    + "  FILTER ( ?grade > 0.85 && ?thing1 != ?thing2)\n"
                    + "}\n";

    private static final String GOLDEN_SPARQL =
            "PREFIX heterogeneity: <http://knowledgeweb.semanticweb.org/heterogeneity/>\n"
                    + "PREFIX rdf:     <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
                    + "PREFIX rdfs:     <http://www.w3.org/2000/01/rdf-schema#>\n"
                    + "PREFIX owl:      <http://www.w3.org/2002/07/owl#>\n"
                    + "CONSTRUCT {\n"
                    + " ?entity1 owl:sameAs ?entity2 .\n"
                    + "}\n"
                    + "WHERE {\n"
                    + " ?alignment heterogeneity:alignmententity1 ?entity1.\n"
                    + " ?alignment heterogeneity:alignmententity2 ?entity2.\n"
//                    + " FILTER ( ?entity1 != ?entity2)\n"
                    + "}\n";

    public static void main(String[] args) throws Exception {
        CustomFunctions.loadSimilarityFunctions();


        Model tBox1 = SparqlExecutor.getModel(SPARQL1, TBOX_1_FILENAME);

        Model tBox2 = SparqlExecutor.getModel(SPARQL2, TBOX_2_FILENAME);
        Model tBox = tBox1.add(tBox2);

        SparqlExecutor.saveModel(RESULT_SPARQL, tBox, RESULT_FILENAME);
//        Model result = SparqlExecutor.getModel(RESULT_SPARQL, tBox);
        Model result = RDFDataMgr.loadModel(RESULT_FILENAME);

        Model golden = SparqlExecutor.getModel(GOLDEN_SPARQL, REFALIGN_FILENAME);

        int tp = Math.toIntExact(result.intersection(golden).size());
        int fp = Math.toIntExact(result.difference(golden).size());
        ConfusionMatrix cm = new ConfusionMatrix(tp, fp);
        if (cm.getPrecision() < 1.0) {
            LOGGER.error("Precision is less than 1.0");
        }
        cm.save(new File(MATRIX_FILENAME));

        LOGGER.info("result size: {}", result.size());
        LOGGER.info("golden size: {}", golden.size());
        golden.difference(result).write(System.out, "NT");
        LOGGER.info("==========================================");
        golden.intersection(result).write(System.out, "NT");
        LOGGER.info("=========================================");
        result.difference(golden).write(System.out, "NT");
    }
}
