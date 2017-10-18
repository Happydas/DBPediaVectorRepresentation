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

public class OutputText3 {


    public static void main(String[] args) {
        String sparqlEndpoint = "http://dbpedia.org/sparql";
        Repository repo = new SPARQLRepository(sparqlEndpoint);
        repo.initialize();

        try (RepositoryConnection conn = repo.getConnection()) {
            String queryString ="SELECT distinct ?resource ?l ?Totaledge \n" +
                    "WHERE {\n" +
                    "{\n" +

                    "SELECT ?resource ?l count(?resource) as ?Totaledge WHERE {\n" +
                    "?obj ?p ?resource .\n" +
                    "?resource <http://www.w3.org/2000/01/rdf-schema#label> ?l .\n" +
                    "?l bif:contains '\"Berlin\"' .\n" +
                    "FILTER (!regex(str(?resource), '^http://dbpedia.org/resource/Category:')).\n" +
                    "FILTER (!regex(str(?resource), '^http://dbpedia.org/resource/List')).\n" +
                    "FILTER (!regex(str(?resource), '^http://sw.opencyc.org/')).\n" +
                    "FILTER (lang(?l) = 'en').\n" +
                    "FILTER (!isLiteral(?obj)).\n" +
                    "} ORDER BY DESC(?Totaledge) LIMIT 20\n" +
                    "}\n" +

                    "UNION\n" +

                    "{\n" +

                    "SELECT ?resource ?l count(?resource) as ?Totaledge WHERE {\n" +
                    "?obj ?p ?resource .\n" +
                    "?resource <http://www.w3.org/2000/01/rdf-schema#label> ?l .\n" +
                    "?l bif:contains '\"Germany\"' .\n" +
                    "FILTER (!regex(str(?resource), '^http://dbpedia.org/resource/Category:')).\n" +
                    "FILTER (!regex(str(?resource), '^http://dbpedia.org/resource/List')).\n" +
                    "FILTER (!regex(str(?resource), '^http://sw.opencyc.org/')).\n" +
                    "FILTER (lang(?l) = 'en').\n" +
                    "FILTER (!isLiteral(?obj)).\n" +
                    "} ORDER BY DESC(?Totaledge) LIMIT 20\n" +
                    "}\n" +
                    "}";




            TupleQuery tupleQuery = conn.prepareTupleQuery(QueryLanguage.SPARQL, queryString);
            //TupleQueryResult result = tupleQuery.evaluate();
            try (TupleQueryResult result = tupleQuery.evaluate()) {
                PrintStream out = new PrintStream(new FileOutputStream(
                        "output/word2.txt"));
                while (result.hasNext()) {  // iterate over the result
                    BindingSet bindingSet = result.next();
                    out.println(bindingSet);




                    //Value valueOfX = bindingSet.getValue("N");
                    //Value valueOfY = bindingSet.getValue("O");
                    //System.out.println("x:" + valueOfX + ", y:" + valueOfY);

                }
                out.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }

        repo.shutDown();
    }
}
