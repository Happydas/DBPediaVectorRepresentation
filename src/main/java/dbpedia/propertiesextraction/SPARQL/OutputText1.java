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

public class OutputText1 {
    public static void main(String[] args) {
        String sparqlEndpoint = "http://dbpedia.org/sparql";
        Repository repo = new SPARQLRepository(sparqlEndpoint);
        repo.initialize();

        try (RepositoryConnection conn = repo.getConnection()) {
            String queryString = "SELECT ?l count(?resource) as ?Totaledge WHERE {\n" +
                    "?obj ?p ?resource .\n" +
                    "?resource <http://www.w3.org/2000/01/rdf-schema#label> ?l .\n" +
                    "?l bif:contains '\"Germany\"' .\n" +
                    "FILTER (!regex(str(?resource), '^http://dbpedia.org/resource/Category:')).\n" +
                    "FILTER (!regex(str(?resource), '^http://dbpedia.org/resource/List')).\n" +
                    "FILTER (!regex(str(?resource), '^http://sw.opencyc.org/')).\n" +
                    "FILTER (lang(?l) = 'en').\n" +
                    "FILTER (!isLiteral(?obj)).\n" +
                    "} ORDER BY DESC(?Totaledge) LIMIT 10";


            TupleQuery tupleQuery = conn.prepareTupleQuery(QueryLanguage.SPARQL, queryString);
            //TupleQueryResult result = tupleQuery.evaluate();
            try (TupleQueryResult result = tupleQuery.evaluate()) {
                PrintStream out = new PrintStream(new FileOutputStream(
                        "output/resource.txt"));
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

