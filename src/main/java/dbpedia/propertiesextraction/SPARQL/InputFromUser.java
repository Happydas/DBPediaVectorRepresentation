package dbpedia.propertiesextraction.SPARQL;


import com.hp.hpl.jena.query.*;
        import com.hp.hpl.jena.rdf.model.*;
        import com.hp.hpl.jena.util.*;

public class InputFromUser {

    public static void main(String[]args)
    {
        sparqlTest();
    }

    public static void sparqlTest()
    {
         /*String queryString = "SELECT ?o WHERE {"+
                                "?s ?p ?o ."+
                                "} LIMIT 10";*/
        String str="Obama";
        String queryString = "PREFIX pr:<http://xmlns.com/foaf/0.1/>\n" +
                "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"+
                "SELECT DISTINCT ?s ?label WHERE {" +                              "?s rdfs:label ?label . "+
                "?s a pr:Person . "+
                "FILTER (lang(?label) = 'en') . "+
                "?label <bif:contains> \""+str+"\" ."+
                "}";
        Query query = QueryFactory.create(queryString);
        QueryExecution qexec =         QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", query);
        try
        {
            ResultSet results = qexec.execSelect();
            while(results.hasNext()){
                QuerySolution soln = results.nextSolution();
                //Literal name = soln.getLiteral("x");
                System.out.println(soln);
            }
        }
        finally{
            qexec.close();
        }

    }
}