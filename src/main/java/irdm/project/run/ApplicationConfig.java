/**
 * 
 */
package irdm.project.run;

import java.io.File;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;

/**
 * @author Harsha Perera
 *
 */
public class ApplicationConfig {

	public static String SeedUrl = "http://www.cs.ucl.ac.uk";
	public static String HomePath = "C:/TerrierSearchEngine/index_data_2";

	public static String stopWordListPath = "C:/TerrierSearchEngine/stopword-list.txt";

	public static String IndexPath = HomePath + File.separator + "IndexData";
	public static String CrawlPath = HomePath + File.separator + "CrawlData";

	public static int CrawlMaxDepth = 10;

	public static int NumberOfCrawlers = 3;

	public static boolean UsePageRank = false;
	// Maximum iterations to use in the power method when calculating the page
	// rank
	public static int PageRankMaxIterations = 10;
	public static double PageRankTeleportProbability = 0.15;
	public static double PageRankWeighting = 0.5;
	public static String PageRankScoreFilePath = IndexPath + File.separator + "pagerankscores.dat";

	static {
		// TODO: Change this to get the home path from the command line
		File configFile = new File("C:/Users/Harsha/workspace/Terrier/etc/engine.properties");
		if (configFile.exists()) {

			Configurations configs = new Configurations();
			try {

				Configuration config = configs.properties(configFile);

				SeedUrl = config.getString("seedurl");
				HomePath = config.getString("homepath");

				stopWordListPath = config.getString("stoplistpath");
				if (stopWordListPath == null || stopWordListPath.isEmpty()) {
					stopWordListPath = HomePath + File.separator + "etc" + File.separator + "stopword-list.txt";
				}

				IndexPath = config.getString("indexpath");
				if (IndexPath == null || IndexPath.isEmpty()) {
					IndexPath = HomePath + File.separator + "index";
				}
				
				CrawlPath = config.getString("crawlpath");
				if (CrawlPath == null || CrawlPath.isEmpty()) {
					CrawlPath = HomePath + File.separator + "crawl";
				}

				CrawlMaxDepth = config.getInt("crawlmaxdepth");

				NumberOfCrawlers = config.getInt("numberofcrawlers");

				UsePageRank = config.getBoolean("usepagerank");
				PageRankMaxIterations = config.getInt("pagerankmaxiterations");
				PageRankTeleportProbability = config.getDouble("pagerankteleportprobability");
				PageRankWeighting = config.getDouble("pagerankweighting");
				PageRankScoreFilePath = config.getString("pagerankscorefilepath");
				if (PageRankScoreFilePath == null || PageRankScoreFilePath.isEmpty()) {
					PageRankScoreFilePath = IndexPath + File.separator + "pagerankscores.dat";
				}
				// access configuration properties
			} catch (ConfigurationException cex) {
				// TODO: If the file isn't there populate it with default
				// values.
				System.out.println(cex.getMessage());
			}
			catch (Exception e){
				System.out.println(e.getMessage());
			}
		}

	}

}
