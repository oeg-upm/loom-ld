package upm.oeg.loom.enums;

/**
 * @author Wenqi
 */
public enum SimilarityAlgorithm {
    /**
     *
     */
    COSINE("cosine"),
    /**
     *
     */
    JACCARD("jaccard"),

    /**
     *
     */
    JARO_WINKLER("jaro-winkler"),
    /**
     *
     */
    LEVENSHTEIN("levenshtein"),

    LCS("lcs"),
    /**
     *
     */
    RATCLIFF_OBERSHELP("ratcliff-obershelp");
    private final String name;

    /**
     * @param name Algorithm name
     */
    SimilarityAlgorithm(final String name) {
        this.name = name;
    }

    /**
     * @see java.lang.Enum#toString()
     */
    @Override
    public String toString() {
        return name;
    }
}
