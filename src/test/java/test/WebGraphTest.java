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

import irdm.project.pagerank.WebGraph;

/**
 * @author Harsha Perera
 *
 */
public class WebGraphTest {

    @Test
    public void testNodeCount() {
    	WebGraph graph = new WebGraph();
    	Map<String,List<String>> outgoing_1 = new HashMap<String, List<String>>();
    	outgoing_1.put("2.html",Arrays.asList("2"));
    	outgoing_1.put("3.html",Arrays.asList("3"));   	
    	graph.addPage("1.html", outgoing_1);
    	
    	Map<String,List<String>> outgoing_3 = new HashMap<String, List<String>>();
    	outgoing_3.put("4.html",Arrays.asList("4"));
    	outgoing_3.put("5.html",Arrays.asList("5"));    	
    	graph.addPage("3.html", outgoing_3);    	
        assertEquals(5, graph.getTotalPageCount());        
    }

    @Test
    public void testOutgoingLinks() {
    	WebGraph graph = new WebGraph();
    	Map<String,List<String>> outgoing_1 = new HashMap<String, List<String>>();
    	outgoing_1.put("2.html",Arrays.asList("2"));
    	outgoing_1.put("3.html",Arrays.asList("3"));   	    	
    	graph.addPage("1.html", outgoing_1);
    	
    	Map<String,List<String>> outgoing_2 = new HashMap<String, List<String>>();
    	outgoing_2.put("6.html",Arrays.asList("6"));
    	outgoing_2.put("4.html",Arrays.asList("4"));
    	graph.addPage("2.html", outgoing_2);    	  	

    	Map<String,List<String>> outgoing_3 = new HashMap<String, List<String>>();
    	outgoing_3.put("4.html",Arrays.asList("4"));
    	outgoing_3.put("5.html",Arrays.asList("5"));    	    	
    	graph.addPage("3.html", outgoing_3);
    	
        assertEquals(6, graph.getTotalPageCount());
        assertEquals(0, graph.getOutgoingLinks("6.html").size());
        assertEquals(2, graph.getOutgoingLinks("2.html").size());
        assertTrue(graph.getOutgoingLinks("2.html").contains("4.html"));
        assertTrue(graph.getOutgoingLinks("2.html").contains("6.html"));
        
    }
    
    @Test
    public void testIncomingLinks() {
    	WebGraph graph = new WebGraph();
    	Map<String,List<String>> outgoing_1 = new HashMap<String, List<String>>();
    	outgoing_1.put("2.html",Arrays.asList("2"));
    	outgoing_1.put("3.html",Arrays.asList("3"));   	    	
    	graph.addPage("1.html", outgoing_1);
    	
    	Map<String,List<String>> outgoing_2 = new HashMap<String, List<String>>();
    	outgoing_2.put("6.html",Arrays.asList("6"));
    	outgoing_2.put("4.html",Arrays.asList("4"));
    	graph.addPage("2.html", outgoing_2);    	
    	
    	Map<String,List<String>> outgoing_3 = new HashMap<String, List<String>>();
    	outgoing_3.put("4.html",Arrays.asList("4"));
    	outgoing_3.put("5.html",Arrays.asList("5"));    	    	
    	graph.addPage("3.html", outgoing_3);
    	
        assertEquals(6, graph.getTotalPageCount());
        assertEquals(2, graph.getIncomingLinks("4.html").size());
        assertTrue(graph.getIncomingLinks("4.html").contains("2.html"));
        assertTrue(graph.getIncomingLinks("4.html").contains("3.html"));
    }
    
    @Test
    public void testOutgoingLinksCounts() {
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

        assertEquals(3, graph.getOutgoingLinks("1.html").size());
        assertEquals(2, graph.getOutgoingLinks("2.html").size());
        assertEquals(1, graph.getOutgoingLinks("3.html").size());
        assertEquals(2, graph.getOutgoingLinks("4.html").size());    	    
    }    


    

}
