/**
 * 
 */
package irdm.project.index;

import irdm.project.crawler.CrawlerFactory;
import irdm.project.crawler.CustomPageFetcher;
import irdm.project.pagerank.PageRankCalculator;
import irdm.project.pagerank.TerrierFeatureScoreWriter;
import irdm.project.pagerank.WebGraph;
import irdm.project.run.ApplicationConfig;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.terrier.realtime.memory.MemoryIndex;
import org.terrier.utility.ApplicationSetup;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;


/**
 * @author Shruti Sinha
 * @author Harsha Perera
 *
 */
public class IndexBuilder {	
	
	static{
		ApplicationSetup.setProperty("indexer.meta.forward.keys", "title,url,body,docno");
		ApplicationSetup.setProperty("indexer.meta.forward.keylens", "140,200,5000,200");
		ApplicationSetup.setProperty("indexer.meta.reverse.keys", "docno");
		ApplicationSetup.setProperty("indexer.meta.reverse.keylens", "200");
		
		ApplicationSetup.setProperty("metaindex.crop", "true");
		ApplicationSetup.setProperty("ignore.low.idf.terms", "false");
		
		ApplicationSetup.setProperty("stopwords.filename", ApplicationConfig.stopWordListPath);
		ApplicationSetup.setProperty("stopwords.intern.terms", "true");
		
		ApplicationSetup.setProperty("termpipelines", "Stopwords,PorterStemmer");
//		ApplicationSetup.setProperty("querying.default.controls", "start,end,decorate:on,summaries:content,emphasis:title;content");
//		ApplicationSetup.setProperty("querying.postfilters.controls", "decorate:org.terrier.querying.Decorate");
//		ApplicationSetup.setProperty("querying.postfilters.order", "org.terrier.querying.Decorate");
	}
	
	
	
	
	private MemoryIndex memIndex; 
	private String indexPath;
	private String crawlPath;
	private String url;
	private int linkdepth;
	private HashMap<String, Double> pageRank;
	
	public IndexBuilder(String crawlPath, String indexPath, String url, int linkdepth ){
		this.indexPath = indexPath;		
		this.crawlPath = crawlPath;
		this.url = url;
		this.linkdepth = linkdepth;
		
		memIndex = new MemoryIndex();		
	}
	
	private void writeIndex(String prefix) {		
		try {
			File dir = new File(indexPath);

			if (!dir.exists()) {
				dir.mkdirs();
			}
			
			memIndex.write(indexPath, prefix);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}		
	}
	
	public void indexWebsite() {							

        CrawlConfig config = new CrawlConfig();
        config.setCrawlStorageFolder(crawlPath);
        config.setMaxDepthOfCrawling(linkdepth);
        config.setFollowRedirects(true);
    
        CustomPageFetcher pageFetcher = new CustomPageFetcher(config);
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
        CrawlController controller = null;
		try {
			controller = new CrawlController(config, pageFetcher, robotstxtServer);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
     
        controller.addSeed(url);

		WebGraph g = new WebGraph();    
       
        controller.start(new CrawlerFactory(url, memIndex, g), ApplicationConfig.NumberOfCrawlers);   		
        controller.waitUntilFinish();        
		PageRankCalculator calc = new PageRankCalculator();
		pageRank = calc.calculatePageRank(g, ApplicationConfig.PageRankMaxIterations, ApplicationConfig.PageRankTeleportProbability);        
	}
	
	
	public void write(){
		writeIndex("data");
		
		TerrierFeatureScoreWriter writer = new TerrierFeatureScoreWriter(ApplicationConfig.PageRankScoreFilePath);
        try {
			writer.PersistPageRankScores(pageRank, true);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	

}
