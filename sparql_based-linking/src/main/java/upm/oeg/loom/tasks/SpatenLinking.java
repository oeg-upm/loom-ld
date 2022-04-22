package upm.oeg.loom.tasks;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONWriter;
import org.apache.commons.io.FileUtils;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.riot.RDFDataMgr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import upm.oeg.loom.enums.GeometryRelation;
import upm.oeg.loom.functions.CustomFunctions;
import upm.oeg.loom.utils.SparqlExecutor;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class ConfusionMatrix {
    private int tp;
    private int fp;
    private double precision;

    ConfusionMatrix(int tp, int fp) {
        this.tp = tp;
        this.fp = fp;
        this.precision = (double) tp / (tp + fp);
    }

    public int getTp() {
        return tp;
    }

    public void setTp(int tp) {
        this.tp = tp;
    }

    public int getFp() {
        return fp;
    }

    public void setFp(int fp) {
        this.fp = fp;
    }

    public double getPrecision() {
        return precision;
    }

    public void setPrecision(double precision) {
        this.precision = precision;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this, JSONWriter.Feature.PrettyFormat);
    }

    void save(File file) throws IOException {
        FileUtils.writeStringToFile(file, this.toString(), StandardCharsets.UTF_8);
    }
}

class Spaten {
    private String name;
    private GeometryRelation relation;
    private File sourceFile;
    private File targetFile;
    private File goldenFile;
    private File resultFile;
    private File confusionMatrixFile;

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

    public File getConfusionMatrixFile() {
        return confusionMatrixFile;
    }

    public void setConfusionMatrixFile(File confusionMatrixFile) {
        this.confusionMatrixFile = confusionMatrixFile;
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
                    String evaluationFile = String.format("./src/main/resources/Spaten/%s/%s/%s-%s-ConfusionMatrix.json", name, strings[6], name, strings[6]);
                    spaten.setConfusionMatrixFile(new File(evaluationFile));
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
            LOGGER.info(spaten.getName() + "-" + spaten.getRelation());
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


            Model resultModel = RDFDataMgr.loadModel(spaten.getResultFile().getPath());
            Model goldenModel = RDFDataMgr.loadModel(spaten.getGoldenFile().getPath());
            int tp = Math.toIntExact(resultModel.intersection(goldenModel).size());
            int fp = Math.toIntExact(resultModel.difference(goldenModel).size());
            ConfusionMatrix cm = new ConfusionMatrix(tp, fp);
            cm.save(spaten.getConfusionMatrixFile());
        }


    }
}
