package upm.oeg.loom.tasks;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.sparql.expr.NodeValue;
import org.apache.jena.sparql.function.FunctionBase2;
import org.apache.jena.sparql.function.FunctionRegistry;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;

/**
 * @author Wenqi
 */
public class SpatenLinking {
  private static final String SOURCE_PATH =
      "Spaten_LinesTOLines/CONTAINS/SourceDatasets/sourceCONTAINS-0001.nt";
  private static final String TARGET_PATH =
      "Spaten_LinesTOLines/CONTAINS/TargetDatasets/targetCONTAINS-0001.nt";

  public static void main(String[] args) {
    FunctionRegistry ref = FunctionRegistry.get();
    ref.put("http://oeg.upm.es/loom-ld/functions/linking/spatial#contains", SpatialContains.class);
    Model model = RDFDataMgr.loadModel(SOURCE_PATH);
    model.add(RDFDataMgr.loadModel(TARGET_PATH));
    String sparql =
        "PREFIX rdf:     <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
            + "PREFIX spatial:    <http://oeg.upm.es/loom-ld/functions/linking/spatial#>\n"
            + "PREFIX strdf:     <http://strdf.di.uoa.gr/ontology#>\n"
            + "CONSTRUCT {\n"
            + "   ?data1 ?data2 ?contains .\n"
            + "}\n"
            + "WHERE {\n"
            + "  ?data1 rdf:type ?traces .\n"
            + "  ?data1 strdf:hasGeometry ?geometry1 .\n"
            + "  ?data2 rdf:type ?traces .\n"
            + "  ?data2 strdf:hasGeometry ?geometry2. \n"
            + "  FILTER ( !strstarts(str(?data1), \"http://www.spaten.com/\") )\n"
            + "  FILTER ( !strstarts(str(?data2), \"http://www.hobbit\") )\n"
            + "  BIND ( spatial:contains(?geometry1, ?geometry2 ) AS ?contains )\n"
            + "  FILTER ( ?contains = 1.0 )\n"
            + "}\n";
    System.out.println(sparql);

    Query query = QueryFactory.create(sparql);
    QueryExecution qexec = QueryExecutionFactory.create(query, model);
    Model res = qexec.execConstruct();
    res.write(System.out, "NT");
  }

  public static class SpatialContains extends FunctionBase2 {
    WKTReader reader = new WKTReader();

    @Override
    public NodeValue exec(NodeValue v1, NodeValue v2) {
      try {
        Geometry g1 = reader.read(v1.asString());
        Geometry g2 = reader.read(v2.asString());
        if (g1.contains(g2) || g2.contains(g1)) {
          return NodeValue.makeDouble(1.0);
        }
      } catch (ParseException e) {
        System.out.println(e.getMessage());
      }
      return NodeValue.makeDouble(0.0);
    }
  }
}
