package upm.oeg.loom.tasks;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONWriter;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author wenqi jiang
 */
public class ConfusionMatrix {

    private double precision;
    private double recall;
    private double f1;

    private long milliseconds;

    public double getPrecision() {
        return precision;
    }

    public void setPrecision(double precision) {
        this.precision = precision;
    }

    public double getRecall() {
        return recall;
    }

    public void setRecall(double recall) {
        this.recall = recall;
    }

    public double getF1() {
        return f1;
    }

    public void setF1(double f1) {
        this.f1 = f1;
    }

    public long getMilliseconds() {
        return milliseconds;
    }

    public void setMilliseconds(int milliseconds) {
        this.milliseconds = milliseconds;
    }

    public ConfusionMatrix(double precision, double recall, long milliseconds) {
        this.precision = precision;
        this.recall = recall;
        f1 = 2 * precision * recall / (precision + recall);
        this.milliseconds = milliseconds;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this, JSONWriter.Feature.PrettyFormat);
    }

    public void save(File file) throws IOException {
        FileUtils.writeStringToFile(file, this.toString(), StandardCharsets.UTF_8);
    }
}