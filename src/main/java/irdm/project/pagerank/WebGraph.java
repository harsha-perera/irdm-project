/**
 * 
 */
package irdm.project.pagerank;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Harsha Perera
 *
 *         Data structure to hold link data of pages whilst a web crawl is
 *         underway.
 */
public class WebGraph {

	private ConcurrentHashMap<String, Collection<String>> outgoingUrls;
	// url -> (source url->list of anchors)
	private ConcurrentHashMap<String, ConcurrentHashMap<String, List<String>>> incomingUrls;

	public WebGraph() {
		super();
		this.outgoingUrls = new ConcurrentHashMap<>();
		this.incomingUrls = new ConcurrentHashMap<>();
	}

	public void addPage(String url, Map<String, List<String>> outgoingLinksData) {
		outgoingUrls.put(url, outgoingLinksData.keySet());
		incomingUrls.computeIfAbsent(url, k -> new ConcurrentHashMap<>());
		outgoingLinksData.forEach((targetUrl, anchorTextList) -> {
			outgoingUrls.computeIfAbsent(targetUrl, k -> new ArrayList<>());
			ConcurrentHashMap<String, List<String>> incomingUrlsForTarget = incomingUrls.computeIfAbsent(targetUrl,
					k -> new ConcurrentHashMap<>());
			incomingUrlsForTarget.put(url, anchorTextList);
		});
	}

	public int getTotalPageCount() {
		return outgoingUrls.size();
	}

	public Set<String> getAllUrls() {
		return outgoingUrls.keySet();
	}

	public Collection<String> getIncomingLinks(String url) {
		return incomingUrls.get(url).keySet();
	}

	public Map<String, List<String>> getIncomingLinkData(String url) {
		return incomingUrls.get(url);
	}

	public Collection<String> getOutgoingLinks(String url) {
		return outgoingUrls.get(url);
	}

}
