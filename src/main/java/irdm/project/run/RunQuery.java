/**
 * 
 */
package irdm.project.run;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.*;
import org.terrier.matching.ResultSet;
import org.terrier.querying.Manager;
import org.terrier.querying.SearchRequest;
import org.terrier.structures.Index;
import org.terrier.utility.ApplicationSetup;

import irdm.project.pagerank.TerrierPageRankScoreModifier;
import ucar.nc2.ft2.Structure.Iterator;

/**
 * @author Shruti Sinha
 * @author Harsha Perera
 *
 */
public class RunQuery {
	static {
		ApplicationSetup.setProperty("querying.postprocesses.order", "QueryExpansion");
		ApplicationSetup.setProperty("querying.postprocesses.controls", "qe:QueryExpansion");
		ApplicationSetup.setProperty("querying.postfilters.order", "SimpleDecorate,SiteFilter,Scope");
		ApplicationSetup.setProperty("querying.postfilters.controls",
				"decorate:SimpleDecorate,site:SiteFilter,scope:Scope");
		ApplicationSetup.setProperty("querying.default.controls", "");
		ApplicationSetup.setProperty("querying.allowed.controls", "scope,qe,qemodel,start,end,site,scope");
		// Document Score Modifier for PageRank
//		if (ApplicationConfig.UsePageRank) {
//			ApplicationSetup.setProperty("matching.dsms", TerrierPageRankScoreModifier.class.getName());
//		}
	}

	/**
	 * @param args
	 */
	public static void main1(String[] args) {
		TerrierInitialiser.InitTerrier();
		ResultSet result = search("jens krinke", true);
		//ResultSet result = search("news");

		String[] urls = result.getMetaItems("url");
		double[] scores = result.getScores();

		for (int i = 0; i<20 && i < scores.length; i++)
			System.out.println(urls[i] + ", Score:" + scores[i]);
	}

	public static void main(String[] args) {
		TerrierInitialiser.InitTerrier();
		try {
			batchSearch(ApplicationConfig.HomePath + File.separator + "QueryTerms.csv", RetrievalModelNames.BM25, false, ApplicationConfig.HomePath + File.separator + "QueryResultsBM25.csv");
			System.out.println("Finished Batch Search");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}    	
	}
	
	public static ResultSet search(String query, boolean usePageRank) {	
		Index searchIndex;
		if(!usePageRank){
			searchIndex = Index.createIndex(ApplicationConfig.IndexPath, IndexNames.Data);
		}
		else{
			searchIndex = Index.createIndex(ApplicationConfig.IndexPath, IndexNames.Data_Anchor);
		}

		/*if (ApplicationConfig.UsePageRank) {
			Index anchorIndex = Index.createIndex(ApplicationConfig.AnchorIndexPath, IndexNames.Anchor);
			searchIndex = new MultiIndex(new Index[] { searchIndex, anchorIndex });
		}*/
		
		StringBuffer sb = new StringBuffer();

		sb.append(query);

		//Manager queryingManager = new Manager(index);
		Manager queryingManager = new Manager(searchIndex);

		SearchRequest srq = queryingManager.newSearchRequest("query", sb.toString());
		srq.addMatchingModel("Matching","TF_IDF");
		//srq.addMatchingModel("Matching","DirichletLM");
		//srq.addMatchingModel("Matching", "BM25"); // http://terrier.org/docs/v4.0/javadoc/org/terrier/matching/models/package-summary.html
		srq.setOriginalQuery(sb.toString());
		srq.setControl("decorate", "on");
		queryingManager.runPreProcessing(srq);
		queryingManager.runMatching(srq);
		queryingManager.runPostProcessing(srq);
		queryingManager.runPostFilters(srq);
		return srq.getResultSet();
	}
	
	
	public static void batchSearch(String queryFileName, String retrievalModelName, boolean usePageRank, String resultFileName) throws IOException {
		// QueryFile format - topicid, topic, query id, query text
		final int topic_id_idx = 0;
		final int topic_idx = 1;
		final int query_idx = 2;		
		final int query_id_idx = 3;
		
		// Result file format - query id, rank, result url, algorithm??
		
		// Work out which search index to use from the input parameters		
		Index searchIndex;
		if(!usePageRank){
			searchIndex = Index.createIndex(ApplicationConfig.IndexPath, IndexNames.Data);
		}
		else{
			searchIndex = Index.createIndex(ApplicationConfig.IndexPath, IndexNames.Data_Anchor);
		}
		/*if (ApplicationConfig.UsePageRank) {
			Index anchorIndex = Index.createIndex(ApplicationConfig.AnchorIndexPath, IndexNames.Anchor);
			searchIndex = new MultiIndex(new Index[] { searchIndex, anchorIndex });
		}*/
		
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
		for (CSVRecord record = recordsIterator.next(); recordsIterator.hasNext();record = recordsIterator.next()){			
//		}
//		for (CSVRecord record : records) {
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
