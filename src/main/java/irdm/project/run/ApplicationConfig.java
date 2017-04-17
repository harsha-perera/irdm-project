/**
 * 
 */
package irdm.project.run;

import java.io.File;

/**
 * @author Harsha Perera
 *
 */
public class ApplicationConfig {

	public static String SeedUrl = "http://www.cs.ucl.ac.uk";
	public static String HomePath = "C:/TerrierSearchEngine/index_data_2";
	
	public static String stopWordListPath = "C:/TerrierSearchEngine/stopword-list.txt";
	
	public static String IndexPath = HomePath + File.separator +  "IndexData";
	public static String CrawlPath = HomePath + File.separator +  "CrawlData";
	
	public static int CrawlMaxDepth = 10;
	
	public static int NumberOfCrawlers = 3;
	
	public static boolean UsePageRank = false;
	// Maximum iterations to use in the power method when calculating the page rank
	public static int PageRankMaxIterations = 10;
	public static double PageRankTeleportProbability = 0.15;
	public static double PageRankWeighting = 0.5;
	public static String PageRankScoreFilePath = IndexPath + File.separator + "pagerankscores.dat";

}
