/**
 * 
 */
package irdm.project.crawler;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.terrier.indexing.TaggedDocument;
import org.terrier.indexing.tokenisation.Tokeniser;
import org.terrier.realtime.memory.MemoryIndex;
import org.terrier.utility.ApplicationSetup;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import irdm.project.pagerank.WebGraph;

/**
 * @author Shruti Sinha
 * @author Harsha Perera
 *
 */
public class Crawler extends WebCrawler {
	private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|bmp|gif|jpe?g|ico|png|tiff?" + "|mid|mp2|mp3|mp4"
			+ "|wav|avi|mov|mpeg|ram|m4v|pdf" + "|rm|smil|wmv|swf|wma|zip|rar|gz))$");
	
	boolean configured = false;
	Object configurelock = new Object();

	private Tokeniser tokeniser;

	private String hostname;
	private MemoryIndex index;

	private WebGraph webGraph;

	/**
	 * @param webGraph
	 */
	public Crawler(String hostName, MemoryIndex index, WebGraph webGraph) {
		super();
		this.hostname = hostName;
		this.index = index;
		this.webGraph = webGraph;
	}

	public void init() {
		synchronized (configurelock) {
			ApplicationSetup.setProperty("TaggedDocument.abstracts", "title,body");
			ApplicationSetup.setProperty("TaggedDocument.abstracts.tags", "title,ELSE");
			ApplicationSetup.setProperty("TaggedDocument.abstracts.lengths", "140,5000");
			ApplicationSetup.setProperty("TaggedDocument.abstracts.tags.casesensitive", "false");

			tokeniser = Tokeniser.getTokeniser();
			configured = true;
		}
	}

	@Override
	public boolean shouldVisit(Page page, WebURL url) {
		if (!configured)
			init();		

		return shouldVisit(url.getURL());
	}

	private boolean shouldVisit(String href) {
		String lhref = href.toLowerCase();
		return !FILTERS.matcher(lhref).matches() && lhref.contains(hostname);
	}

	@Override
	public void visit(Page page) {
		if (!configured)
			init();
		String url = page.getWebURL().getURL();
		System.out.println("URL: " + url);

		if (!(page.getParseData() instanceof HtmlParseData)) {
			return;
		}

		HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();

		String html = htmlParseData.getHtml();

		Map<String, String> docProperties = new HashMap<String, String>();
		docProperties.put("url", url);
		docProperties.put("docno", url);
		docProperties.put("encoding", "UTF-8");

		ByteArrayInputStream inputStream = null;
		try {
			inputStream = new ByteArrayInputStream(html.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e1) {
			throw new AssertionError(e1);
		}

		TaggedDocument tg = new TaggedDocument(inputStream, docProperties, tokeniser);
		try {
			index.indexDocument(tg);
		} catch (Exception e) {
			e.printStackTrace();
		}

		Set<WebURL> links = htmlParseData.getOutgoingUrls();
		Map<String,List<String>> outgoingLinkData = new HashMap<String,List<String>>(links.size());
		for (WebURL webUrl : links) {
			String targetUrl = webUrl.getURL();
			if(targetUrl.equals(url)){
				continue;
			}
			
			if (shouldVisit(targetUrl)) {				
				List<String> anchorTextList = outgoingLinkData.computeIfAbsent(targetUrl,k-> new ArrayList<String>());
				anchorTextList.add(webUrl.getAnchor());
			}
		}
		this.webGraph.addPage(url, outgoingLinkData);

	}
}
