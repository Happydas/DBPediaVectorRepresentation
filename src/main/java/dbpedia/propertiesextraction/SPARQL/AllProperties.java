package dbpedia.propertiesextraction.SPARQL;



        import org.eclipse.rdf4j.query.BindingSet;
        import org.eclipse.rdf4j.query.QueryLanguage;
        import org.eclipse.rdf4j.query.TupleQuery;
        import org.eclipse.rdf4j.query.TupleQueryResult;
        import org.eclipse.rdf4j.repository.Repository;
        import org.eclipse.rdf4j.repository.RepositoryConnection;
        import org.eclipse.rdf4j.repository.sparql.SPARQLRepository;
        import java.io.FileNotFoundException;
        import java.io.FileOutputStream;
        import java.io.PrintStream;


public class AllProperties {
    public static void main(String[] args) {
        String sparqlEndpoint = "http://dbpedia.org/sparql";
        Repository repo = new SPARQLRepository(sparqlEndpoint);
        repo.initialize();

        try (RepositoryConnection conn = repo.getConnection()) {
            String queryString = "SELECT ?p (count(?p) as ?totalCount) FROM <http://dbpedia.org> WHERE {\n" +
                    "?s ?p ?o .\n" +
                    "} GROUP BY ?p ORDER BY DESC(?totalCount)";


            TupleQuery tupleQuery = conn.prepareTupleQuery(QueryLanguage.SPARQL, queryString);
            //TupleQueryResult result = tupleQuery.evaluate();
            try (TupleQueryResult result = tupleQuery.evaluate()) {
                PrintStream out = new PrintStream(new FileOutputStream(
                        "output/allproperties.txt"));
                while (result.hasNext()) {  // iterate over the result
                    BindingSet bindingSet = result.next();
                    out.println(bindingSet);




                }
                out.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }

        repo.shutDown();
    }
}
