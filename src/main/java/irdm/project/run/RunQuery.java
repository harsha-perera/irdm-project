/**
 * 
 */
package irdm.project.run;

import org.terrier.matching.ResultSet;
import org.terrier.querying.Manager;
import org.terrier.querying.SearchRequest;
import org.terrier.structures.Index;
import org.terrier.utility.ApplicationSetup;

import irdm.project.pagerank.TerrierPageRankScoreModifier;

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
		if (ApplicationConfig.UsePageRank) {
			ApplicationSetup.setProperty("matching.dsms", TerrierPageRankScoreModifier.class.getName());
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		TerrierInitialiser.InitTerrier();
		ResultSet result = search("krinke");

		// String[] displayKeys = result.getMetaKeys(); // url, title, body
		// String[][] meta = new String[displayKeys.length][];
		//
		//
		// for(int j=0;j<displayKeys.length;j++)
		// {
		// meta[j] = result.getMetaItems(displayKeys[j]);
		// }

		String[] urls = result.getMetaItems("url");
		double[] scores = result.getScores();

		for (int i = 0; i < scores.length; i++)
			System.out.println(urls[i] + ", Score:" + scores[i]);

	}

	public static ResultSet search(String query) {
		Index index = Index.createIndex(ApplicationConfig.IndexPath, "data");
		StringBuffer sb = new StringBuffer();

		sb.append(query);

		Manager queryingManager = new Manager(index);

		SearchRequest srq = queryingManager.newSearchRequest("query", sb.toString());
		// srq.addMatchingModel("Matching","DirichletLM");
		srq.addMatchingModel("Matching", "BM25"); // http://terrier.org/docs/v4.0/javadoc/org/terrier/matching/models/package-summary.html
		srq.setOriginalQuery(sb.toString());
		srq.setControl("decorate", "on");
		queryingManager.runPreProcessing(srq);
		queryingManager.runMatching(srq);
		queryingManager.runPostProcessing(srq);
		queryingManager.runPostFilters(srq);
		return srq.getResultSet();
	}

}
