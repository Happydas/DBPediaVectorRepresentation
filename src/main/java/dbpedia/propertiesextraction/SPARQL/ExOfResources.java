package dbpedia.propertiesextraction.SPARQL;

        import org.eclipse.rdf4j.model.Value;
        import org.eclipse.rdf4j.query.BindingSet;
        import org.eclipse.rdf4j.query.QueryLanguage;
        import org.eclipse.rdf4j.query.TupleQuery;
        import org.eclipse.rdf4j.query.TupleQueryResult;
        import org.eclipse.rdf4j.repository.Repository;
        import org.eclipse.rdf4j.repository.RepositoryConnection;
        import org.eclipse.rdf4j.repository.sparql.SPARQLRepository;

public class ExOfResources {
    public static void main(String[] args) {
        String sparqlEndpoint = "http://dbpedia.org/sparql";
        Repository repo = new SPARQLRepository(sparqlEndpoint);
        repo.initialize();

        try (RepositoryConnection conn = repo.getConnection()) {
            String queryString = "SELECT ?resource ?l count(?resource) as ?Totaledge WHERE {\n" +
                    "?obj ?p ?resource .\n" +
                    "?resource <http://www.w3.org/2000/01/rdf-schema#label> ?l .\n" +
                    "?l bif:contains '\"South Africa\"' .\n" +
                    "FILTER (!regex(str(?resource), '^http://dbpedia.org/resource/Category:')).\n" +
                    "FILTER (!regex(str(?resource), '^http://dbpedia.org/resource/List')).\n" +
                    "FILTER (!regex(str(?resource), '^http://sw.opencyc.org/')).\n" +
                    "FILTER (lang(?l) = 'en').\n" +
                    "FILTER (!isLiteral(?obj)).\n" +
                    "} ORDER BY DESC(?Totaledge) LIMIT 10";


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
