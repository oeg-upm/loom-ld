package upm.oeg.loom.tasks;

import org.apache.jena.rdf.model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import upm.oeg.loom.functions.CustomFunctions;
import upm.oeg.loom.utils.SparqlExecutor;

/**
 * https://hobbit-project.github.io/OAEI_2021.html RDF API:
 * https://jena.apache.org/tutorials/rdf_api.html
 *
 * @author Wenqi
 */
public class SPIMBENCHLinking {
  private static final String TBOX_1_FILENAME = "SPIMBENCH_large/Tbox1.nt";
  private static final String TBOX_2_FILENAME = "SPIMBENCH_large/Tbox2.nt";
  private static final Logger LOGGER = LoggerFactory.getLogger(SPIMBENCHLinking.class);

  public static void main(String[] args) throws Exception {
    CustomFunctions.loadSimilarityFunctions();

    String sparql1 =
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
    Model tBox1 = SparqlExecutor.getModel(sparql1, TBOX_1_FILENAME);
    String sparql2 =
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

    Model tBox2 = SparqlExecutor.getModel(sparql2, TBOX_2_FILENAME);
    Model tBox = tBox1.add(tBox2);
    String sparql =
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
            + "  FILTER ( ?grade > 0.8 && ?thing1 != ?thing2)\n"
            + "}\n";
    SparqlExecutor.saveModel(sparql, tBox, "tasks/SPIMBENCH/results.nt");
  }





}
