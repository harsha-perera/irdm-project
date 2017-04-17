package irdm.project.pagerank;

import java.util.HashMap;
import java.util.Set;
import java.util.Vector;

/**
 * @author Harsha Perera
 *
 * Calculates Page Rank scores for all pages. This is called at the end of a web crawl. It uses the outgoing link data gathered
 * during the web crawl to calculate the page rank score.
 */
public class PageRankCalculator {
		
	public HashMap<String, Double> calculatePageRank(WebGraph webGraph, int numberOfIterations, double teleportProbability){		
		double rankFromTeleport = teleportProbability/webGraph.getTotalPageCount();		
		double startingPageRank = 1.0/webGraph.getTotalPageCount();
		HashMap<String, Double> pageRank = new HashMap<>(webGraph.getTotalPageCount());
		Set<String> allUrls = webGraph.getAllUrls();
		// Initialise the page ranks of all pages with the starting page rank value
		for (String url : allUrls) {
			pageRank.put(url, startingPageRank);
		}
		
		for (int iteration = 0; iteration < numberOfIterations; iteration++) {
			HashMap<String, Double> nextPageRank = new HashMap<>(pageRank.size());
			for (String url : allUrls) {
				double rankFromIncomingLinks = 0;
				Vector<String> incomingLinks = webGraph.getIncomingLinks(url);
				for (String incomingLink : incomingLinks){
					// Add the weight given by this incoming link
					rankFromIncomingLinks += pageRank.get(incomingLink).doubleValue()/webGraph.getOutgoingLinks(incomingLink).size();
				}
				nextPageRank.put(url, rankFromTeleport + (1-teleportProbability)*rankFromIncomingLinks);
			}			
			pageRank = nextPageRank;
		}	
		return pageRank;
	}

}
