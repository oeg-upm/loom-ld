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