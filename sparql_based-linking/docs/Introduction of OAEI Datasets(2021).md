# Introduction of OAEI Datasets(2021)

The aim of the instance matching benchmark for spatial data challenge is to test the performance of IM tools that implement topological approaches for identifying matching spatial entities. The IM frameworks will be evaluated for both accuracy (precision, recall and f-measure) and time performance.

Spatial benchmark measures how well the systems can identify DE-9IM (Dimensionally Extended nine-Intersection Model) topological relations. The supported spatial relations are the following: Equals, Disjoint, Touches, Contains/Within, Covers/CoveredBy, Intersects, Crosses, Overlaps  and the  traces are represented in Well-known text (WKT) format. For each relation, a different pair of source and target dataset will be given to the participants.

## Task 1 : TomTom dataset
1. Match LineStrings to LineString 
2. Match LineStrings to Polygons

## Task 2 : Spaten dataset
1. Match LineStrings to LineString
2. Match LineStrings to Polygons

# The namespaces of the datasets are:

## Tomtom
- `http://www.tomtom.com/ontologies/traces#` for the Traces (LineStrings)
- `http://www.tomtom.com/ontologies/regions#` for the Regions (Polygons)

## Spaten 
- `http://www.spaten.com/ontologies/traces#` for the Traces (LineStrings)
- `http://www.spaten.com/ontologies/regions#` for the Regions (Polygons)

hobbit:implementsAPI bench:SpatialAPI;

# Each participant must:

Submit results related to one, more, or even all the expected tasks. Each task is articulated in two tests with different scales (i.e., number of instances to match):

- Sandbox (small scale): It contains two datasets called source and target as well as the set of expected mappings (i.e., reference alignment). For Task 1, we provide 100 instances and for every subtask of Task 2, 20 instances per source and target file.

- Mainbox (medium/large scale): It contains two datasets called source and target. This test is blind, meaning that the reference alignment is not given to the participants.  

In both tests, the goal is to discover the matching pairs (i.e., mappings) among the instances in the source dataset and the instances in the target dataset.

---

The SPIMBENCH task is composed of two datasets with different scales (i.e., number of instances to match):

- Sandbox (~380 INSTANCES, ~10000 TRIPLES). It contains two datasets called source (Tbox1) and target (Tbox2) as well as the set of expected mappings (i.e., reference alignment).

- Mainbox (~1800 CWs, ~50000 TRIPLES). It contains two datasets called source (Tbox1) and target (Tbox2). This test is blind, meaning that the reference alignment is not given to the participants.

The SPIMBENCH datasets are generated and transformed using SPIMBENCH by altering a set of original data through value-based, structure-based, and semantics-aware transformations (simple combination of transformations).

hobbit:implementsAPI bench:spimbenchAPI
