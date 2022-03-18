# Introduction of OAEI Datasets(2021)

# SPIMBENCH

The goal of the SPIMBENCH task is to determine when two instances describe the same Creative Work. A dataset is composed of a Tbox (contains the ontology and the instances) and corresponding Abox (contains only the instances). The datasets share almost the same ontology (with some difference in the properties’ level, due to the structure-based transformations). What we expect from participants. Participants are requested to match instances in the source dataset (Tbox1) against the instances of the target dataset (Tbox2). The task goal is to produce a set of mappings between the pairs of matching instances that are found to refer to the same real-world entity. An instance in the source (Tbox1) dataset can have none or one matching counterparts in the target dataset (Tbox2). We ask the participants to map only instances of Creative Works (http://www.bbc.co.uk/ontologies/creativework/NewsItem, http://www.bbc.co.uk/ontologies/creativework/BlogPost and http://www.bbc.co.uk/ontologies/creativework/Programme) and not the instances of the other classes.

You can download training data here: [SPIMBENCH training data](http://users.ics.forth.gr/~jsaveta/.index.php?dir=OAEI_IM_SPIMBENCH_2021)


The **SPIMBENCH** task is composed of two datasets with different scales (i.e., number of instances to match):

- Sandbox (~380 INSTANCES, ~10000 TRIPLES). It contains two datasets called source (Tbox1) and target (Tbox2) as well as the set of expected mappings (i.e., reference alignment).

- Mainbox (~1800 CWs, ~50000 TRIPLES). It contains two datasets called source (Tbox1) and target (Tbox2). This test is blind, meaning that the reference alignment is not given to the participants.

The SPIMBENCH datasets are generated and transformed using SPIMBENCH by altering a set of original data through value-based, structure-based, and semantics-aware transformations (simple combination of transformations).

hobbit:implementsAPI bench:spimbenchAPI
---

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

