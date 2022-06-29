package upm.oeg.loom.tasks.spatial;

import upm.oeg.loom.enums.GeometryRelation;

import java.io.File;

public class SpatialFile {
    private String name;
    private GeometryRelation relation;
    private File sourceFile;
    private File targetFile;
    private File goldenFile;
    private File resultFile;
    private File confusionMatrixFile;

    public SpatialFile() {
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

    @Override
    public String toString() {
        return "SpatialFile{" +
                "name='" + name + '\'' +
                ", relation=" + relation +
                ", sourceFile=" + sourceFile +
                ", targetFile=" + targetFile +
                ", goldenFile=" + goldenFile +
                ", resultFile=" + resultFile +
                ", confusionMatrixFile=" + confusionMatrixFile +
                '}';
    }
}