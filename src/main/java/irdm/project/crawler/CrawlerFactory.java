/**
 * 
 */
package irdm.project.crawler;

import org.terrier.realtime.memory.MemoryIndex;

import edu.uci.ics.crawler4j.crawler.CrawlController;
import irdm.project.pagerank.WebGraph;

/**
 * @author Harsha Perera
 *
 */
public class CrawlerFactory implements CrawlController.WebCrawlerFactory<Crawler> {


    private String hostname;
    private MemoryIndex index;	
	private WebGraph webGraph;
	
    public CrawlerFactory(String hostName, MemoryIndex index,WebGraph webGraph) {
		this.hostname = hostName;
		this.index = index;    	
    	this.webGraph = webGraph;
    }

    @Override
    public Crawler newInstance() {
        return new Crawler(this.hostname, this.index, this.webGraph);
    }
}