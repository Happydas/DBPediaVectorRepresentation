package dbpedia.propertiesextraction.SPARQL;


import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.QueryLanguage;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.sparql.SPARQLRepository;

public class ExtractionOfProp {
    public static void main(String[] args) {
        String sparqlEndpoint = "http://dbpedia.org/sparql";
        Repository repo = new SPARQLRepository(sparqlEndpoint);
        repo.initialize();

        try (RepositoryConnection conn = repo.getConnection()) {
            String queryString = "PREFIX dbp: <http://dbpedia.org/property/>\n" +
                    "PREFIX dbo: <http://dbpedia.org/ontology/>\n" +
                    "PREFIX s: <http://schema.org/>\n" +
                    "PREFIX dbr: <http://dbpedia.org/resource/>" +
                    "SELECT DISTINCT * WHERE {\n" +
                    "?h a s:Hotel .\n" +
                    "?h dbo:location dbr:Italy  .\n" +
                    "}";
            TupleQuery tupleQuery = conn.prepareTupleQuery(QueryLanguage.SPARQL, queryString);
            //TupleQueryResult result = tupleQuery.evaluate();
            try (TupleQueryResult result = tupleQuery.evaluate()) {
                while (result.hasNext()) {  // iterate over the result
                    BindingSet bindingSet = result.next();
                    System.out.println(bindingSet);

                    //Value valueOfX = bindingSet.getValue("N");
                    //Value valueOfY = bindingSet.getValue("O");
                    //System.out.println("x:" + valueOfX + ", y:" + valueOfY);
                }
            }
        }

        repo.shutDown();
    }
}
