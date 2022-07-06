package upm.oeg.loom.utils;

import org.apache.commons.io.FileUtils;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.RDFLanguages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Run basic SPARQL queries
 *
 * @author Wenqi Jiang
 */
public class SparqlExecutor {
    private static final Logger LOGGER = LoggerFactory.getLogger(SparqlExecutor.class);
    private static Query query;
    private static QueryExecution qexec;

    public static ResultSet getResultSet(String sparql, String filename) {
        LOGGER.info("run sparql query from the source: {} ", filename);
        return getResultSet(sparql, RDFDataMgr.loadModel(filename));
    }

    public static ResultSet getResultSet(String sparql, Model source) {
        LOGGER.info("run sparql query:{} ", sparql);
        query = QueryFactory.create(sparql);
        qexec = QueryExecutionFactory.create(query, source);
        return qexec.execSelect();
    }

    /**
     * print the result of select sparql query
     *
     * @param sparql sparql query
     */
    public static void printResultSet(String sparql) {
        printResultSet(sparql, ModelFactory.createDefaultModel());
    }

    public static void printResultSet(String sparql, String filename) {
        LOGGER.info("run sparql query from the source: {} ", filename);
        printResultSet(sparql, RDFDataMgr.loadModel(filename));
    }

    public static void printResultSet(String sparql, Model source) {
        ResultSet results = getResultSet(sparql, source);
        ResultSetFormatter.out(System.out, results, query);
    }

    /**
     * Get the construct from source with sparql query
     *
     * @param sparql sparql query
     * @param source source to be queried
     * @return the model / construct
     */
    public static Model getModel(String sparql, Model source) {
        LOGGER.info("run sparql query:\n{} ", sparql);
        query = QueryFactory.create(sparql);
        qexec = QueryExecutionFactory.create(query, source);
        return qexec.execConstruct();
    }

    /**
     * Get the construct from source file with sparql query
     *
     * @param sparql   sparql query
     * @param filename source file to be queried
     * @return the model / construct
     */
    public static Model getModel(String sparql, String filename) {
        LOGGER.info("run sparql query from the file {}:", filename);
        Model source = RDFDataMgr.loadModel(filename);
        return getModel(sparql, source);
    }

    /**
     * print the result of construct sparql query
     *
     * @param sparql sparql query
     */
    public static void printModel(String sparql) {
        Model model = getModel(sparql, ModelFactory.createDefaultModel());
        model.write(System.out, "TURTLE");
    }

    /**
     * print the result of construct sparql query
     *
     * @param sparql sparql query
     * @param source source to be queried
     * @param target filename to store the result
     * @throws FileNotFoundException if the target file is not found
     */
    public static void saveModel(String sparql, Model source, String target) throws IOException {
        Model model = getModel(sparql, source);
        if (model.isEmpty()) {
            LOGGER.warn("{}: The model is empty", target);
            return;
        }
        Lang lang = RDFLanguages.filenameToLang(target);
//        LOGGER.info(
//                "Save sparql query:\n{} \nresult into {}, format: {}", sparql, target, lang.getName());
        OutputStream out = FileUtils.openOutputStream(new File(target));
        RDFDataMgr.write(out, model, lang);
    }

    public static void saveModel(Model source, String target) throws IOException {
        if (source.isEmpty()) {
            LOGGER.warn("{}: The model is empty", target);
            return;
        }
        Lang lang = RDFLanguages.filenameToLang(target);
//        LOGGER.info(
//                "Save sparql query:\n{} \nresult into {}, format: {}", sparql, target, lang.getName());
        OutputStream out = FileUtils.openOutputStream(new File(target));
        RDFDataMgr.write(out, source, lang);
    }

    public static void printModel(String sparql, Model model) {
        getModel(sparql, model).write(System.out, "TURTLE");
    }
}
