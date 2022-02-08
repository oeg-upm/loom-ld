package upm.oeg.loom.functions.similarities;

import info.debatty.java.stringsimilarity.Cosine;
import upm.oeg.loom.enums.SimilarityAlgorithm;

public class CosineSimilarity extends AbstractStringSimilarityFunction{

    private final Cosine cosine;

    public CosineSimilarity(){
        super(SimilarityAlgorithm.COSINE);
        cosine = new Cosine();
    }
    @Override
    public Double compareStrings(String element1, String element2) {

        return cosine.similarity(element1, element2);
    }
}
