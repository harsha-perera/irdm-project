/**
 * 
 */
package irdm.project.pagerank;

import java.util.Set;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Harsha Perera
 *
 * Data structure to hold link data of pages whilst a web crawl is underway.
 */
public class WebGraph {

	ConcurrentHashMap<String, Vector<String>> outgoingUrls;
	ConcurrentHashMap<String, Vector<String>> incomingUrls;
	
	public WebGraph() {
		super();
		this.outgoingUrls = new ConcurrentHashMap<>();
		this.incomingUrls = new ConcurrentHashMap<>();
	}


	public void addPage(String url, Vector<String> outgoingLinks){
		outgoingUrls.put(url, outgoingLinks);
		for (String targetUrl : outgoingLinks) {
			outgoingUrls.computeIfAbsent(targetUrl, k -> new Vector<>());
			Vector<String> incomingUrlsForTarget = incomingUrls.computeIfAbsent(targetUrl, k -> new Vector<>());
			incomingUrlsForTarget.add(url);
		}
	}
	
	public int getTotalPageCount(){
		return outgoingUrls.size();
	}
	
    public Set<String> getAllUrls(){
		return outgoingUrls.keySet();		
	}
	
    public Vector<String> getIncomingLinks(String url){
		return incomingUrls.get(url);
	}
    
    public Vector<String> getOutgoingLinks(String url){
		return outgoingUrls.get(url);
	}
	
}
