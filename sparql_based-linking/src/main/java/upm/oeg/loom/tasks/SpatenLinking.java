package upm.oeg.loom.tasks;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.riot.RDFDataMgr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import upm.oeg.loom.enums.GeometryRelation;
import upm.oeg.loom.functions.CustomFunctions;
import upm.oeg.loom.utils.SparqlExecutor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class Spaten {
    private String name;
    private GeometryRelation relation;
    private File sourceFile;
    private File targetFile;
    private File goldenFile;
    private File resultFile;
    private File evaluation;

    public Spaten() {
        name = "";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public GeometryRelation getRelation() {
        return relation;
    }

    public void setRelation(GeometryRelation relation) {
        this.relation = relation;
    }

    public File getSourceFile() {
        return sourceFile;
    }

    public void setSourceFile(File sourceFile) {
        this.sourceFile = sourceFile;
    }

    public File getTargetFile() {
        return targetFile;
    }

    public void setTargetFile(File targetFile) {
        this.targetFile = targetFile;
    }

    public File getGoldenFile() {
        return goldenFile;
    }

    public void setGoldenFile(File goldenFile) {
        this.goldenFile = goldenFile;
    }

    public File getResultFile() {
        return resultFile;
    }

    public void setResultFile(File resultFile) {
        this.resultFile = resultFile;
    }

    public File getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(File evaluation) {
        this.evaluation = evaluation;
    }
}

/**
 * @author Wenqi
 */
public class SpatenLinking {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpatenLinking.class);


    private List<Spaten> loadSpatens() throws IOException {
        List<Spaten> spatens = new ArrayList<>();
        try (Stream<Path> paths = Files.walk(Paths.get("./src/main/resources/Spaten"))) {
            Spaten spaten = new Spaten();
            for (Path path : paths.filter(Files::isRegularFile).collect(Collectors.toList())) {
                String[] strings = path.toString().split("/");
                String name = strings[5];
                GeometryRelation relation = GeometryRelation.valueOf(strings[6]);
                String fileType = strings[7];
                if (!spaten.getName().equals(name) || !spaten.getRelation().equals(relation)) {
                    if (!"".equals(spaten.getName())) {
                        spatens.add(spaten);
                    }
                    // reset Spaten
                    spaten = new Spaten();
                    spaten.setName(name);
                    spaten.setRelation(relation);
                    String resultFile = String.format("./src/main/resources/Spaten/%s/%s/ResultDatasets/%smappings.nt", name, strings[6], strings[6]);
                    spaten.setResultFile(new File(resultFile));
                    String evaluationFile = String.format("./src/main/resources/Spaten/%s/%s/Evaluation/%s-%s-Evaluation.json", name, strings[6], name, strings[6]);
                    spaten.setEvaluation(new File(evaluationFile));
                }

                switch (fileType) {
                    case "GoldStandards":
                        spaten.setGoldenFile(path.toFile());
                        break;
                    case "SourceDatasets":
                        spaten.setSourceFile(path.toFile());
                        break;
                    case "TargetDatasets":
                        spaten.setTargetFile(path.toFile());
                        break;
                    default:
                        break;
                }
            }
        }

        return spatens;
    }


    public static void main(String[] args) throws IOException {
        CustomFunctions.loadGeometryFunctions();
        SpatenLinking spatenLinking = new SpatenLinking();
        List<Spaten> spatens = spatenLinking.loadSpatens();
        for (Spaten spaten : spatens) {
            String sourceSparql = "PREFIX rdf:     <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
                    + "PREFIX geometry:    <http://oeg.upm.es/loom-ld/functions/linking/geometry#>\n"
                    + "PREFIX strdf:     <http://strdf.di.uoa.gr/ontology#>\n"
                    + "CONSTRUCT {\n"
                    + "  ?source strdf:hasGeometry ?sourceGeometry .\n"
                    + "  ?source geometry:isLocatedAt \"" + spaten.getSourceFile().getName() + "\"\n"
                    + "}\n"
                    + "WHERE {\n"
                    + "  ?source strdf:hasGeometry ?sourceGeometry .\n"
                    + "}\n";

            Model sourceModel = SparqlExecutor.getModel(sourceSparql, spaten.getSourceFile().getPath());

            String targetSparql = "PREFIX rdf:     <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
                    + "PREFIX geometry:    <http://oeg.upm.es/loom-ld/functions/linking/geometry#>\n"
                    + "PREFIX strdf:     <http://strdf.di.uoa.gr/ontology#>\n"
                    + "CONSTRUCT {\n"
                    + "  ?target strdf:hasGeometry ?sourceGeometry .\n"
                    + "  ?target geometry:isLocatedAt \"" + spaten.getTargetFile().getName() + "\"\n"
                    + "}\n"
                    + "WHERE {\n"
                    + "  ?target strdf:hasGeometry ?sourceGeometry .\n"
                    + "}\n";
            Model targetModel = SparqlExecutor.getModel(targetSparql, spaten.getTargetFile().getPath());
            String sparql = "PREFIX rdf:     <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
                    + "PREFIX geometry:    <http://oeg.upm.es/loom-ld/functions/linking/geometry#>\n"
                    + "PREFIX strdf:     <http://strdf.di.uoa.gr/ontology#>\n"
                    + "CONSTRUCT {\n"
                    + "   ?source ?target 1.0 \n"
                    + "}\n"
                    + "WHERE {\n"
                    + "  ?source geometry:isLocatedAt \"" + spaten.getSourceFile().getName() + "\" .\n"
                    + "  ?source strdf:hasGeometry ?sourceGeometry .\n"
                    + "  ?target geometry:isLocatedAt \"" + spaten.getTargetFile().getName() + "\" .\n"
                    + "  ?target strdf:hasGeometry ?targetGeometry .\n"
                    + "  BIND ( geometry:" + spaten.getRelation() + " (?sourceGeometry, ?targetGeometry ) AS ?relation )\n"
                    + "  FILTER ( ?relation = TRUE )\n"
                    + "}\n";
            Model model = sourceModel.union(targetModel);
            SparqlExecutor.saveModel(sparql, model, spaten.getResultFile().getPath());

            LOGGER.info(spaten.getName() + "-" + spaten.getRelation());
            Model resultModel = RDFDataMgr.loadModel(spaten.getResultFile().getPath());
            Model goldenModel = RDFDataMgr.loadModel(spaten.getGoldenFile().getPath());
            LOGGER.info("Result model size: " + resultModel.size());
            LOGGER.info("Golden model size: " + goldenModel.size());
            LOGGER.info("Result has, but golden does not: " + (resultModel.size() - goldenModel.size()));
            resultModel.difference(goldenModel).write(System.out, "NT");
            LOGGER.info("Golden has, but result does not: " + (goldenModel.size() - resultModel.size()));
            goldenModel.difference(resultModel).write(System.out, "NT");
            LOGGER.info("Result and golden are equal: " + resultModel.isIsomorphicWith(goldenModel));
        }



    }
}
