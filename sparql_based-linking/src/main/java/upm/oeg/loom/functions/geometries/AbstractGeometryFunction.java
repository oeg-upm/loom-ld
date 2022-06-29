package upm.oeg.loom.functions.geometries;

import org.apache.jena.sparql.function.FunctionBase2;
import org.locationtech.jts.io.WKTReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import upm.oeg.loom.enums.GeometryRelation;
import upm.oeg.loom.utils.SparqlExecutor;

/**
 * @author Wenqi Jiang
 */
public abstract class AbstractGeometryFunction extends FunctionBase2 {
    public static final Logger LOGGER = LoggerFactory.getLogger(SparqlExecutor.class);
    GeometryRelation relation;
    WKTReader reader;

    public AbstractGeometryFunction(GeometryRelation relation) {
        this.relation = relation;
        reader = new WKTReader();
    }

    public GeometryRelation getRelation() {
        return relation;
    }

    public void setRelation(GeometryRelation relation) {
        this.relation = relation;
    }
}
