package upm.oeg.loom.tasks.spimbench;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.riot.RDFDataMgr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import upm.oeg.loom.enums.SimilarityAlgorithm;
import upm.oeg.loom.functions.CustomFunctions;
import upm.oeg.loom.tasks.ConfusionMatrix;
import upm.oeg.loom.tasks.spatial.SpatialDataLinking;
import upm.oeg.loom.tasks.spatial.SpatialFile;
import upm.oeg.loom.utils.SparqlExecutor;

import java.io.File;
import java.text.DecimalFormat;

/**
 * https://hobbit-project.github.io/OAEI_2021.html RDF API:
 * https://jena.apache.org/tutorials/rdf_api.html
 *
 * @author Wenqi
 */
public class SPIMBENCHLinking {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpatialDataLinking.class);
    private static final String TBOX_1_FILENAME = "SPIMBENCH/SPIMBENCH_small/Tbox1.nt";
    private static final String TBOX_2_FILENAME = "SPIMBENCH/SPIMBENCH_small/Tbox2.nt";
    private static final String REFALIGN_FILENAME = "SPIMBENCH/SPIMBENCH_small/refalign.rdf";
    private static final String GOLDEN_FILENAME = "src/main/resources/SPIMBENCH/SPIMBENCH_small/golden.nt";

    private static final String RESULT_FILENAME = "src/main/resources/SPIMBENCH/SPIMBENCH_small/result.nt";
    private static final String MATRIX_FILENAME = "src/main/resources/SPIMBENCH/confusion_matrix.json";
    private static final String SPARQL1 =
            "PREFIX rdf:     <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
                    + "PREFIX rdfs:     <http://www.w3.org/2000/01/rdf-schema#>\n"
                    + "PREFIX loom:     <https://oeg.upm.es/loom-ld/functions/linking/text#>\n"
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
                    + "  OPTIONAL {?thing cwork:title ?title .}\n"
                    + "  OPTIONAL {?thing cwork:shortTitle ?shortTitle .}\n"
                    + "  OPTIONAL {?thing cwork:description ?description .}\n"
//                    + "  FILTER(?work IN (cwork:NewsItem, cwork:BlogPost, cwork:Programme))\n"
                    + "}\n";
    private static final String SPARQL2 =
            "PREFIX rdf:     <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
                    + "PREFIX rdfs:     <http://www.w3.org/2000/01/rdf-schema#>\n"
                    + "PREFIX loom:     <https://oeg.upm.es/loom-ld/functions/linking/text#>\n"
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
                    + "  OPTIONAL {?thing cwork:title ?title .}\n"
                    + "  OPTIONAL {?thing cwork:shortTitle ?shortTitle .}\n"
                    + "  OPTIONAL {?thing cwork:description ?description .}\n"
//                    + "  FILTER(?work IN (cwork:NewsItem, cwork:BlogPost, cwork:Programme))\n"
                    + "}\n";


    private static final String GOLDEN_SPARQL =
            "PREFIX heterogeneity: <http://knowledgeweb.semanticweb.org/heterogeneity/>\n"
                    + "PREFIX rdf:     <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
                    + "PREFIX rdfs:     <http://www.w3.org/2000/01/rdf-schema#>\n"
                    + "PREFIX owl:      <http://www.w3.org/2002/07/owl#>\n"
                    + "PREFIX loom:     <https://oeg.upm.es/loom-ld/functions/linking/text#>\n"
                    + "PREFIX cwork:    <http://www.bbc.co.uk/ontologies/creativework/>\n"
                    + "PREFIX seals:    <http://www.seals-project.eu/ontologies/SEALSMetadata.owl#>\n"
                    + "CONSTRUCT {\n"
                    + " ?entity1 owl:sameAs ?entity2 .\n"
                    + "}\n"
                    + "WHERE {\n"
                    + " ?alignment heterogeneity:alignmententity1 ?entity1.\n"
                    + " ?alignment heterogeneity:alignmententity2 ?entity2.\n"
                    + " FILTER ( ?entity1 != ?entity2)\n"
                    + "}\n";

    public static void main(String[] args) throws Exception {
        CustomFunctions.loadTextFunctions();
        Model tBox1 = SparqlExecutor.getModel(SPARQL1, TBOX_1_FILENAME);
        Model tBox2 = SparqlExecutor.getModel(SPARQL2, TBOX_2_FILENAME);
        Model tBox = tBox1.add(tBox2);
//        Model refalign = SparqlExecutor.getModel(GOLDEN_SPARQL, REFALIGN_FILENAME);
//        SparqlExecutor.saveModel(refalign, GOLDEN_FILENAME);
        Model golden = RDFDataMgr.loadModel(GOLDEN_FILENAME);

        double[] percentages = new double[]{0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 1.0};
        for (SimilarityAlgorithm algorithm : SimilarityAlgorithm.values()) {
            for (double p : percentages) {
                String resultSparql = buildResultSparql(algorithm, p);
                long start = System.currentTimeMillis();
                //                SparqlExecutor.saveModel(resultSparql, tBox, RESULT_FILENAME);
                //                Model result = RDFDataMgr.loadModel(RESULT_FILENAME);
                Model result = SparqlExecutor.getModel(resultSparql, tBox);
                long last = System.currentTimeMillis() - start;
                long intersectionNumber = result.intersection(golden).size();
                double precision = intersectionNumber * 1.0 / result.size();
                double recall = intersectionNumber * 1.0 / golden.size();
                ConfusionMatrix cm = new ConfusionMatrix(precision, recall, last);
//                                    cm.save(new File(MATRIX_FILENAME));
                //                LOGGER.info("result size: {}", result.size());
                //                LOGGER.info("golden size: {}", golden.size());
                //                LOGGER.info("matrix: {}", cm);
                printResult(algorithm.toString(), p, cm, golden, result);


            }
        }


    }

    private static String buildResultSparql(SimilarityAlgorithm algorithm, double p) {
        return "PREFIX heterogeneity: <http://knowledgeweb.semanticweb.org/heterogeneity/>\n"
                + "PREFIX rdf:     <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
                + "PREFIX rdfs:     <http://www.w3.org/2000/01/rdf-schema#>\n"
                + "PREFIX owl:      <http://www.w3.org/2002/07/owl#>\n"
                + "PREFIX loom:     <https://oeg.upm.es/loom-ld/functions/linking/text#>\n"
                + "PREFIX cwork:    <http://www.bbc.co.uk/ontologies/creativework/>\n"
                + "PREFIX seals:    <http://www.seals-project.eu/ontologies/SEALSMetadata.owl#>\n"
                + "CONSTRUCT {\n"
                + "  ?thing1 owl:sameAs ?thing2 .\n"
                + "}\n"
                + "WHERE {\n"
                + "  ?thing1 seals:isLocatedAt \"tbox1.nt\" .\n"
                + "  OPTIONAL {?thing1 cwork:title ?title1 .}\n"
                + "  OPTIONAL {?thing1 cwork:shortTitle ?shortTitle1 .}\n"
                + "  OPTIONAL {?thing1 cwork:description ?description1 .}\n"
                + "  ?thing2 seals:isLocatedAt \"tbox2.nt\" .\n"
                + "  OPTIONAL {?thing2 cwork:title ?title2 .}\n"
                + "  OPTIONAL {?thing2 cwork:shortTitle ?shortTitle2 .}\n"
                + "  OPTIONAL {?thing2 cwork:description ?description2 .}\n"
                + "  BIND(loom:" + algorithm + "(?title1, ?title2 ) AS ?titleGrade) \n"
                + "  BIND(loom:" + algorithm + "(?shortTitle1, ?shortTitle2 ) AS ?shortTitleGrade) \n"
                + "  BIND(loom:" + algorithm + "(?description1, ?description2 ) AS ?descriptionGrade) \n"
                + "  FILTER ( ?thing1 != ?thing2 )\n"
                + "  FILTER ( ?titleGrade >= " + p + " || ?shortTitleGrade >=  " + p + " || ?descriptionGrade >=  " + p + " )\n"
                + "}\n";
    }

    private static void printResult(String algorithm, double percentage, ConfusionMatrix matrix, Model golden, Model result) {
        DecimalFormat df = new DecimalFormat("#.###");
        long goldenSize = golden.size();
        long resultSize = result.size();
        double precision = matrix.getPrecision();
        double recall = matrix.getRecall();
        double f1 = matrix.getF1();
        long milliseconds = matrix.getMilliseconds();
        System.out.println(algorithm + " & " + percentage + " & " + goldenSize + " & " + resultSize + " & " + df.format(precision) + " & " + df.format(recall) + " & " + df.format(f1) + " & " + milliseconds + " \\\\ \\hline ");
    }
}
