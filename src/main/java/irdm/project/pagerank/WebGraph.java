/**
 * 
 */
package irdm.project.pagerank;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * @author Harsha Perera
 *
 * Data structure to hold link data of pages whilst a web crawl is underway.
 */
public class WebGraph {

	private ConcurrentHashMap<String, Collection<String>> outgoingUrls;
	private ConcurrentHashMap<String, ConcurrentLinkedDeque<String>> incomingUrls;
	
	public WebGraph() {
		super();
		this.outgoingUrls = new ConcurrentHashMap<>();
		this.incomingUrls = new ConcurrentHashMap<>();
	}


	public void addPage(String url, Collection<String> outgoingLinks){
		outgoingUrls.put(url, outgoingLinks);
		for (String targetUrl : outgoingLinks) {
			ConcurrentLinkedDeque<String> incomingUrlsForTarget = incomingUrls.computeIfAbsent(targetUrl, k -> new ConcurrentLinkedDeque<>());
			incomingUrlsForTarget.add(url);
		}
	}
	
	public int getTotalPageCount(){
		return outgoingUrls.size();
	}
	
    public Set<String> getAllUrls(){
		return outgoingUrls.keySet();		
	}
	
    public Collection<String> getIncomingLinks(String url){
		return incomingUrls.get(url);
	}
    
    public Collection<String> getOutgoingLinks(String url){
		return outgoingUrls.get(url);
	}
	
}
