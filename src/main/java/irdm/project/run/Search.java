/**
 * 
 */
package irdm.project.run;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.terrier.matching.ResultSet;
import org.terrier.querying.Manager;
import org.terrier.querying.SearchRequest;
import org.terrier.structures.Index;
import org.terrier.utility.ApplicationSetup;

import irdm.project.index.IndexBuilder;
import irdm.project.pagerank.TerrierPageRankScoreModifier;

/**
 * @author Harsha Perera
 *
 * Top level class for running the crawl and search commands
 */
public class Search {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//args = new String[]{"C:\\Users\\Harsha\\workspace\\Terrier", "q", "BM25", "krinke"};
		// Args - home dir path, i to index
		//        home dir path, q retrievalModelName "query terms" to run a single query
		//        home dir path, bq retrievalModelName "query file path" "result path" to run a batch of queries and save the output
		if(args==null || args.length < 2){
			// Incorrect number of args		
			System.err.println("Incorrect usage");
			return;
		}
		String homePath = args[0];
		ApplicationConfig.init(homePath);
		TerrierInitialiser.InitTerrier();
		
		String cmd = args[1].toLowerCase();
		switch(cmd){
		case "i":
			indexSite();
			System.out.println("Finished indexing");
			break;
			
		case "q":
			if (args.length<4){
				System.err.println("Insufficient arguments for batch query command");
			}			
			search(args[3], ApplicationConfig.UsePageRank, args[2]);
			break;
			
		case "bq":
			if (args.length<5){
				System.err.println("Insufficient arguments for batch query command");
			}
			try {
				batchSearch(args[3], args[2], ApplicationConfig.UsePageRank, args[4]);
				System.out.println("Finished Batch Search");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
			default:
				System.err.println("Unsupported command");

		}
	}
	
	public static void indexSite() {		
		IndexBuilder indexBuilder = new IndexBuilder(ApplicationConfig.CrawlPath, ApplicationConfig.IndexPath,
				ApplicationConfig.SeedUrl, ApplicationConfig.CrawlMaxDepth);
		indexBuilder.indexWebsite();
		indexBuilder.write();
	}
	
	public static void search(String query, boolean usePageRank, String retrievalModelName) {	
		ApplicationSetup.setProperty("querying.postprocesses.order", "QueryExpansion");
		ApplicationSetup.setProperty("querying.postprocesses.controls", "qe:QueryExpansion");
		ApplicationSetup.setProperty("querying.postfilters.order", "SimpleDecorate,SiteFilter,Scope");
		ApplicationSetup.setProperty("querying.postfilters.controls",
				"decorate:SimpleDecorate,site:SiteFilter,scope:Scope");
		ApplicationSetup.setProperty("querying.default.controls", "");
		ApplicationSetup.setProperty("querying.allowed.controls", "scope,qe,qemodel,start,end,site,scope");
		// Document Score Modifier for PageRank
		if (ApplicationConfig.UsePageRank) {
			ApplicationSetup.setProperty("matching.dsms", TerrierPageRankScoreModifier.class.getName());
		}
		
		Index searchIndex;
		if(!usePageRank){
			searchIndex = Index.createIndex(ApplicationConfig.IndexPath, IndexNames.Data);
		}
		else{
			searchIndex = Index.createIndex(ApplicationConfig.IndexPath, IndexNames.Data_Anchor);
		}

		StringBuffer sb = new StringBuffer();

		sb.append(query);

		Manager queryingManager = new Manager(searchIndex);

		SearchRequest srq = queryingManager.newSearchRequest("query", sb.toString());
		srq.addMatchingModel("Matching",retrievalModelName);
		srq.setOriginalQuery(sb.toString());
		srq.setControl("decorate", "on");
		queryingManager.runPreProcessing(srq);
		queryingManager.runMatching(srq);
		queryingManager.runPostProcessing(srq);
		queryingManager.runPostFilters(srq);
		ResultSet result = srq.getResultSet();
		
		String[] urls = result.getMetaItems("url");
		double[] scores = result.getScores();

		for (int i = 0; i<20 && i < scores.length; i++)
			System.out.println(urls[i] + ", Score:" + scores[i]);		
	}
	
	
	public static void batchSearch(String queryFileName, String retrievalModelName, boolean usePageRank, String resultFileName) throws IOException {
		ApplicationSetup.setProperty("querying.postprocesses.order", "QueryExpansion");
		ApplicationSetup.setProperty("querying.postprocesses.controls", "qe:QueryExpansion");
		ApplicationSetup.setProperty("querying.postfilters.order", "SimpleDecorate,SiteFilter,Scope");
		ApplicationSetup.setProperty("querying.postfilters.controls",
				"decorate:SimpleDecorate,site:SiteFilter,scope:Scope");
		ApplicationSetup.setProperty("querying.default.controls", "");
		ApplicationSetup.setProperty("querying.allowed.controls", "scope,qe,qemodel,start,end,site,scope");
		// Document Score Modifier for PageRank
		if (ApplicationConfig.UsePageRank) {
			ApplicationSetup.setProperty("matching.dsms", TerrierPageRankScoreModifier.class.getName());
		}

		
		// QueryFile format - topicid, topic, query id, query text
//		final int topic_id_idx = 0;
//		final int topic_idx = 1;
		final int query_id_idx = 0;
		final int query_idx = 1;				
		
		// Result file format - query id, rank, result url, algorithm
		
		// Work out which search index to use from the input parameters		
		Index searchIndex;
		if(!usePageRank){
			searchIndex = Index.createIndex(ApplicationConfig.IndexPath, IndexNames.Data);
		}
		else{
			searchIndex = Index.createIndex(ApplicationConfig.IndexPath, IndexNames.Data_Anchor);
		}
		
		CSVFormat csvFormat = CSVFormat.DEFAULT;
		Manager queryingManager = new Manager(searchIndex);
		
		// Open the input query file
		Reader in = new FileReader(queryFileName);
		// Open the output file
		FileWriter fileWriter = new FileWriter(resultFileName);
		CSVPrinter csvWriter = new CSVPrinter(fileWriter, csvFormat);
		// Output header record
		List<String> header = new ArrayList<>();
		header.add("Query ID");
		header.add("Rank");
		header.add("Result Url");
		header.add("Retrieval Model");
		header.add("Score");
		csvWriter.printRecord(header);
		
		Iterable<CSVRecord> records = csvFormat.parse(in);
		java.util.Iterator<CSVRecord> recordsIterator = records.iterator();
		if(recordsIterator.hasNext()){
			// Skip header
			recordsIterator.next();
		}
		while(recordsIterator.hasNext()){	
			CSVRecord record = recordsIterator.next();
			// Run the query
			String query = record.get(query_idx);
			SearchRequest srq = queryingManager.newSearchRequest("query", query);
			srq.addMatchingModel("Matching", retrievalModelName);// BM25,
																	// TF_IDF,
																	// DrichletLM
			srq.setOriginalQuery(query);
			srq.setControl("decorate", "on");
			queryingManager.runPreProcessing(srq);
			queryingManager.runMatching(srq);
			queryingManager.runPostProcessing(srq);
			queryingManager.runPostFilters(srq);
			ResultSet result = srq.getResultSet();
			// Output the top 10 results
			String[] urls = result.getMetaItems("url");
			double[] scores = result.getScores();
			for (int i = 0; i < 10 && i < urls.length; i++) {

				List<String> queryResult = new ArrayList<>();
				queryResult.add(record.get(query_id_idx));
				queryResult.add(Integer.toString(i + 1));
				queryResult.add(urls[i]);
				queryResult.add(retrievalModelName);
				queryResult.add(String.valueOf(scores[i]));
				csvWriter.printRecord(queryResult);
			}
		}
		csvWriter.close();
		fileWriter.close();
		in.close();
	}
	

}
