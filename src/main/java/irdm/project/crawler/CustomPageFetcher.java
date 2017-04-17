/**
 * 
 */
package irdm.project.crawler;

import java.io.IOException;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.exceptions.PageBiggerThanMaxSizeException;
import edu.uci.ics.crawler4j.fetcher.PageFetchResult;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.url.WebURL;

/**
 * @author Shruti Sinha
 *
 */
public class CustomPageFetcher extends PageFetcher {

	public CustomPageFetcher(CrawlConfig config) {
		super(config);
	}
	
	@Override
	public PageFetchResult fetchPage(WebURL webUrl)
			throws InterruptedException, IOException, PageBiggerThanMaxSizeException{
		
		String url = webUrl.getURL();		
		PageFetchResult result = null;
		
		try {			
			result= super.fetchPage(webUrl);	
			System.out.println("Succesfully loaded page:" + url);					
		} catch (Exception e) {
			try{
				CloseableHttpClient temp = httpClient;
				httpClient = HttpClientBuilder.create().build();
				result= super.fetchPage(webUrl);		
				httpClient = temp;
				System.out.println("Succesfully loaded page:" + url);
			}catch(Exception ex){
				System.out.println("Failed to load page:" + url);
			}			
		}
		
		return result;
	}

}
