package upm.oeg.loom.enums;

/**
 * @author Vinci
 */
public enum GeometryRelation {
  /** The geometry contains the other geometry completely. */
  CONTAINS("contains"),
  /** a is covered by b (extends Within): geometry a lies in b */
  COVERED_BY("coveredBy"),
  /** a covers b (extends Within): geometry a contains b */
  COVERS("covers"),
  /**
   * a and b are disjoint: they have no point in common. They form a set of disconnected geometries.
   */
  DISJOINT("disjoint"),
  /** The geometry is completely equal the other geometry: Within & Contains */
  EQUALS("equals"),
  /** a intersects b: geometries a and b have at least one point in common. */
  INTERSECTS("intersects"),
  /**
   * a overlaps b (extends Within): geometry a is contained by b, but neither a nor b contains the
   * other.
   */
  OVERLAPS("overlaps"),
  /** a touches b: they have at least one point in common, but their interiors do not intersect. */
  TOUCHES("touches"),
  /** a is within b: a lies in the interior of b. */
  CROSSES("crosses"),
  WITHIN("within");

  private final String relation;

  GeometryRelation(String relation) {
    this.relation = relation;
  }

  public String getValue() {
    return relation;
  }

  @Override
  public String toString() {
    return relation;
  }
}
