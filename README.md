# LOOM-LD

## The proposal
Develop a web service that allows writing and running these SPARQL-based link rules

## Roadmap
1. Review the state of the art for RDF linking or link discovery
2. Develop a service that accepts SPARQL queries and runs them
   1. For the specification use the [standard](https://www.w3.org/TR/sparql11-overview)
   2. Use YASQUE/YASGUI for Web interface
      E.g.: https://github.com/oeg-upm/helio-publisher/blob/master/src/main/resources/templates/sparql.html
   3. Use [Jena](https://jena.apache.org/) for the SPARQL processor
   4. Use [sparkjava](https://sparkjava.com/) for the service
Test the [implementation](core/src/main/resources/test.sparql) with the query
3. Add linking functions as JenaARQ extensions to the implementation 
   - Import [functions](https://github.com/AndreaCimminoArriaga/EvA4LD/tree/master/tdg.link_discovery.connector.sparql/tdg/link_discovery/connector/sparql/evaluator/arq/linker/string_similarities)
   
> Build a set of HTML views to assist users for writing the link rules
> Compare the time required by our proposal for linking two datasets with Limes or Silk for the same datasets.