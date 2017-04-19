/**
 * 
 */
package test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import irdm.project.pagerank.PageRankCalculator;
import irdm.project.pagerank.WebGraph;


/**
 * @author Harsha Perera
 *
 */
public class PageRankCalculatorTest {
	
    @Test
    public void testBasicPageRank() {
    	WebGraph graph = new WebGraph();
    	Map<String,List<String>> outgoing_1 = new HashMap<String, List<String>>();
    	outgoing_1.put("2.html",Arrays.asList("2"));
    	outgoing_1.put("3.html",Arrays.asList("3"));
    	outgoing_1.put("4.html",Arrays.asList("4"));
    	graph.addPage("1.html", outgoing_1);
    	
    	Map<String,List<String>> outgoing_2 = new HashMap<String, List<String>>();
    	outgoing_2.put("3.html",Arrays.asList("3"));
    	outgoing_2.put("4.html",Arrays.asList("4"));
    	graph.addPage("2.html", outgoing_2);    	
    	
    	Map<String,List<String>> outgoing_3 = new HashMap<String, List<String>>();
    	outgoing_3.put("1.html",Arrays.asList("1"));
    	graph.addPage("3.html", outgoing_3);
    	
    	Map<String,List<String>> outgoing_4 = new HashMap<String, List<String>>();
    	outgoing_4.put("1.html",Arrays.asList("1"));
    	outgoing_4.put("3.html",Arrays.asList("3"));
    	graph.addPage("4.html", outgoing_4);    	
    	
    	PageRankCalculator pageRankCalc = new PageRankCalculator();
    	HashMap<String, Double> pageRank = pageRankCalc.calculatePageRank(graph, 7, 0);
        assertEquals(pageRank.size(), 4);        
        assertEquals(0.38, pageRank.get("1.html").doubleValue(), 0.01);
        assertEquals(0.12, pageRank.get("2.html").doubleValue(), 0.01);
        assertEquals(0.29, pageRank.get("3.html").doubleValue(), 0.01);
        assertEquals(0.19, pageRank.get("4.html").doubleValue(), 0.01);
    }

    @Test
    public void testBasicPageRank2() {
    	WebGraph graph = new WebGraph();
    	Map<String,List<String>> outgoing_a = new HashMap<String, List<String>>();
    	outgoing_a.put("b.html",Arrays.asList("b"));
    	graph.addPage("a.html", outgoing_a);
    	
    	Map<String,List<String>> outgoing_b = new HashMap<String, List<String>>();
    	outgoing_b.put("e.html",Arrays.asList("e"));
    	graph.addPage("b.html", outgoing_b);    	
    	
    	Map<String,List<String>> outgoing_c = new HashMap<String, List<String>>();
    	outgoing_c.put("a.html",Arrays.asList("a"));
    	outgoing_c.put("b.html",Arrays.asList("b"));
    	outgoing_c.put("d.html",Arrays.asList("d"));
    	outgoing_c.put("e.html",Arrays.asList("e"));
    	graph.addPage("c.html", outgoing_c);
    	
    	Map<String,List<String>> outgoing_d = new HashMap<String, List<String>>();
    	outgoing_d.put("c.html",Arrays.asList("c"));
    	outgoing_d.put("e.html",Arrays.asList("e"));
    	graph.addPage("d.html", outgoing_d);    	
    	
    	Map<String,List<String>> outgoing_e = new HashMap<String, List<String>>();
    	outgoing_e.put("d.html",Arrays.asList("d"));
    	graph.addPage("e.html", outgoing_e);    	
    	
    	PageRankCalculator pageRankCalc = new PageRankCalculator();
    	HashMap<String, Double> pageRank = pageRankCalc.calculatePageRank(graph, 2, 0);
        assertEquals(5, pageRank.size());        
        assertEquals(1.0/40, pageRank.get("a.html").doubleValue(), 0.01);
        assertEquals(3.0/40, pageRank.get("b.html").doubleValue(), 0.01);
        assertEquals(5.0/40, pageRank.get("c.html").doubleValue(), 0.01);
        assertEquals(15.0/40, pageRank.get("d.html").doubleValue(), 0.01);
        assertEquals(16.0/40, pageRank.get("e.html").doubleValue(), 0.01);
    }
    

}
