package upm.oeg.loom.tasks.spatial;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.riot.RDFDataMgr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import upm.oeg.loom.enums.GeometryRelation;
import upm.oeg.loom.functions.CustomFunctions;
import upm.oeg.loom.tasks.ConfusionMatrix;
import upm.oeg.loom.utils.SparqlExecutor;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * @author Wenqi
 */
public class SpatialDataLinking {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpatialDataLinking.class);

    private List<SpatialFile> loadSpatialFiles() throws IOException {
        List<SpatialFile> spatialFiles = new ArrayList<>();
        try (Stream<Path> paths = Files.walk(Paths.get("./src/main/resources/Spaten"))) {
            SpatialFile spatialFile = new SpatialFile();
            for (Path path : paths.filter(Files::isRegularFile).collect(Collectors.toList())) {
                String[] strings = path.toString().split("/");
                String name = strings[5];
                GeometryRelation relation = GeometryRelation.valueOf(strings[6]);
                String fileType = strings[7];
                if (!spatialFile.getName().equals(name) || !spatialFile.getRelation().equals(relation)) {
                    if (!"".equals(spatialFile.getName())) {
                        spatialFiles.add(spatialFile);
                    }
                    // reset Spaten
                    spatialFile = new SpatialFile();
                    spatialFile.setName(name);
                    spatialFile.setRelation(relation);
                    String resultFile = String.format("./src/main/resources/Spaten/%s/%s/ResultDatasets/%smappings.nt", name, strings[6], strings[6]);
                    spatialFile.setResultFile(new File(resultFile));
                    String evaluationFile = String.format("./src/main/resources/Spaten/%s/%s/%s-%s-ConfusionMatrix.json", name, strings[6], name, strings[6]);
                    spatialFile.setConfusionMatrixFile(new File(evaluationFile));
                }

                switch (fileType) {
                    case "GoldStandards":
                        spatialFile.setGoldenFile(path.toFile());
                        break;
                    case "SourceDatasets":
                        spatialFile.setSourceFile(path.toFile());
                        break;
                    case "TargetDatasets":
                        spatialFile.setTargetFile(path.toFile());
                        break;
                    default:
                        break;
                }
            }
        }

        return spatialFiles;
    }


    public static void main(String[] args) throws IOException {
        CustomFunctions.loadGeometryFunctions();
        SpatialDataLinking spatialDataLinking = new SpatialDataLinking();
        List<SpatialFile> spatialFiles = spatialDataLinking.loadSpatialFiles();
        for (SpatialFile spatialFile : spatialFiles) {
//            LOGGER.info(spatialFile.getName() + "-" + spatialFile.getRelation());
            String sourceSparql = "PREFIX rdf:     <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
                    + "PREFIX geometry:    <https://oeg.upm.es/loom-ld/functions/linking/geometry#>\n"
                    + "PREFIX strdf:     <http://strdf.di.uoa.gr/ontology#>\n"
                    + "CONSTRUCT {\n"
                    + "  ?source strdf:hasGeometry ?sourceGeometry .\n"
                    + "  ?source geometry:isLocatedAt \"" + spatialFile.getSourceFile().getName() + "\"\n"
                    + "}\n"
                    + "WHERE {\n"
                    + "  ?source strdf:hasGeometry ?sourceGeometry .\n"
                    + "}\n";

            Model sourceModel = SparqlExecutor.getModel(sourceSparql, spatialFile.getSourceFile().getPath());

            String targetSparql = "PREFIX rdf:     <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
                    + "PREFIX geometry:    <https://oeg.upm.es/loom-ld/functions/linking/geometry#>\n"
                    + "PREFIX strdf:     <http://strdf.di.uoa.gr/ontology#>\n"
                    + "CONSTRUCT {\n"
                    + "  ?target strdf:hasGeometry ?sourceGeometry .\n"
                    + "  ?target geometry:isLocatedAt \"" + spatialFile.getTargetFile().getName() + "\"\n"
                    + "}\n"
                    + "WHERE {\n"
                    + "  ?target strdf:hasGeometry ?sourceGeometry .\n"
                    + "}\n";
            Model targetModel = SparqlExecutor.getModel(targetSparql, spatialFile.getTargetFile().getPath());

            long start = System.currentTimeMillis();
            String resultSparql = "PREFIX rdf:     <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
                    + "PREFIX geometry:    <https://oeg.upm.es/loom-ld/functions/linking/geometry#>\n"
                    + "PREFIX strdf:     <http://strdf.di.uoa.gr/ontology#>\n"
                    + "CONSTRUCT {\n"
                    + "   ?source ?target 1.0 \n"
                    + "}\n"
                    + "WHERE {\n"
                    + "  ?source geometry:isLocatedAt \"" + spatialFile.getSourceFile().getName() + "\" .\n"
                    + "  ?source strdf:hasGeometry ?sourceGeometry .\n"
                    + "  ?target geometry:isLocatedAt \"" + spatialFile.getTargetFile().getName() + "\" .\n"
                    + "  ?target strdf:hasGeometry ?targetGeometry .\n"
                    + "  BIND ( geometry:" + spatialFile.getRelation() + " (?sourceGeometry, ?targetGeometry ) AS ?relation )\n"
                    + "  FILTER ( ?relation = TRUE )\n"
                    + "}\n";

            SparqlExecutor.saveModel(resultSparql, sourceModel.union(targetModel), spatialFile.getResultFile().getPath());

            Model resultModel = RDFDataMgr.loadModel(spatialFile.getResultFile().getPath());
            Model goldenModel = RDFDataMgr.loadModel(spatialFile.getGoldenFile().getPath());

            long intersectionSize = resultModel.intersection(goldenModel).size();
            double precision = intersectionSize * 1.0 / resultModel.size();
            double recall = intersectionSize * 1.0 / goldenModel.size();

            ConfusionMatrix cm = new ConfusionMatrix(precision, recall, (System.currentTimeMillis() - start));
//            LOGGER.info("{}", cm);
//            cm.save(spatialFile.getConfusionMatrixFile());

            printResult(spatialFile, cm, goldenModel, resultModel);
        }


    }

    private static void printResult(SpatialFile file, ConfusionMatrix matrix, Model golden, Model result) {
        String dataset = file.getName().split("_")[0];
        String type = file.getName().split("_")[1];
        String relation = file.getRelation().toString();
        long goldenSize = golden.size();
        long resultSize = result.size();
        double precision = matrix.getPrecision();
        double recall = matrix.getRecall();
        double f1 = matrix.getF1();
        long milliseconds = matrix.getMilliseconds();
        System.out.println(dataset +" & "+ type +" & "+ relation +" & "+ goldenSize +" & "+ resultSize +" & "+ precision +" & "+ recall +" & "+ f1 +" & "+ milliseconds +"\\\\ \\hline ");
    }
}
