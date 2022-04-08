package upm.oeg.loom.tasks;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.riot.RDFDataMgr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import upm.oeg.loom.functions.CustomFunctions;
import upm.oeg.loom.utils.SparqlExecutor;

import java.io.IOException;

/**
 * @author Wenqi
 */
public class SpatenLinking {
  private static final Logger LOGGER = LoggerFactory.getLogger(SpatenLinking.class);
  private static final String SOURCE_PATH =
      "Spaten_LinesTOLines/CONTAINS/SourceDatasets/sourceCONTAINS-0001.nt";
  private static final String TARGET_PATH =
      "Spaten_LinesTOLines/CONTAINS/TargetDatasets/targetCONTAINS-0001.nt";

  private static final String SPARQL =
      "PREFIX rdf:     <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
          + "PREFIX geometry:    <http://oeg.upm.es/loom-ld/functions/linking/geometry#>\n"
          + "PREFIX strdf:     <http://strdf.di.uoa.gr/ontology#>\n"
          + "CONSTRUCT {\n"
          + "   ?source ?target 1.0 \n"
          + "}\n"
          + "WHERE {\n"
          + "  ?source rdf:type ?traces .\n"
          + "  ?source strdf:hasGeometry ?sourceGeometry .\n"
          + "  ?target rdf:type ?traces .\n"
          + "  ?target strdf:hasGeometry ?targetGeometry. \n"
          + "  FILTER ( strstarts(str(?source), \"http://www.spaten.com\") )\n"
          + "  FILTER ( strstarts(str(?target), \"http://www.hobbit\") )\n"
          + "  BIND ( geometry:contains(?sourceGeometry, ?targetGeometry ) AS ?contains )\n"
          + "  FILTER ( ?contains = TRUE )\n"
          + "}\n";

  public static void main(String[] args) throws IOException {
    CustomFunctions.loadGeometryFunctions();
    Model model = RDFDataMgr.loadModel(SOURCE_PATH);
    model.add(RDFDataMgr.loadModel(TARGET_PATH));
    SparqlExecutor.saveModel(SPARQL, model, "tasks/Spaten_LinesTOLines/CONTAINSResults.nt");
//    SparqlExecutor.printModel(SPARQL, model);
  }
}
