package dbpedia.propertiesextraction.SPARQL;

        import org.eclipse.rdf4j.model.Value;
        import org.eclipse.rdf4j.query.BindingSet;
        import org.eclipse.rdf4j.query.QueryLanguage;
        import org.eclipse.rdf4j.query.TupleQuery;
        import org.eclipse.rdf4j.query.TupleQueryResult;
        import org.eclipse.rdf4j.repository.Repository;
        import org.eclipse.rdf4j.repository.RepositoryConnection;
        import org.eclipse.rdf4j.repository.sparql.SPARQLRepository;

public class PropertiesCountry {
    public static void main(String[] args) {
        String sparqlEndpoint = "http://dbpedia.org/sparql";
        Repository repo = new SPARQLRepository(sparqlEndpoint);
        repo.initialize();

        try (RepositoryConnection conn = repo.getConnection()) {
            String queryString = "PREFIX dbp: <http://dbpedia.org/property/>\n" +
                    "PREFIX skos: <http://www.w3.org/2004/02/skos/core#>\n" +
                    "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                    "PREFIX dbr: <http://dbpedia.org/resource/>" +
                    "SELECT DISTINCT * WHERE {\n" +
                    "dbr:Paris ?Country dbr:France\n" +
                    "FILTER ((?Country!=rdf:type) &&\n" +
                    "(?Country!=skos:subject) &&\n" +
                    "(?Country!=dbp:wikiPageUsesTemplate) &&\n" +
                    "(?Country!=dbp:wordnet_type ) &&\n" +
                    "(?Country!=dbr:Paris) &&\n" +
                    "(?Country!=dbr:France)\n" +
                    ").\n" +
                    "} LIMIT 10";

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
