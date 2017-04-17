/**
 * 
 */
package test;

import static org.junit.Assert.*;

import java.util.Vector;

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
    	Vector<String> outgoing_1 = new Vector<>();
    	outgoing_1.add("2.html");
    	outgoing_1.add("3.html");    	
    	graph.addPage("1.html", outgoing_1);
    	Vector<String> outgoing_3 = new Vector<>();
    	outgoing_3.add("4.html");
    	outgoing_3.add("5.html");
    	graph.addPage("3.html", outgoing_3);    	
        assertEquals(5, graph.getTotalPageCount());        
    }

    @Test
    public void testOutgoingLinks() {
    	WebGraph graph = new WebGraph();
    	Vector<String> outgoing_1 = new Vector<>();
    	outgoing_1.add("2.html");
    	outgoing_1.add("3.html");    	
    	graph.addPage("1.html", outgoing_1);
    	Vector<String> outgoing_2 = new Vector<>();
    	outgoing_2.add("4.html");
    	outgoing_2.add("6.html");
    	graph.addPage("2.html", outgoing_2);    	
    	
    	Vector<String> outgoing_3 = new Vector<>();
    	outgoing_3.add("4.html");
    	outgoing_3.add("5.html");
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
    	Vector<String> outgoing_1 = new Vector<>();
    	outgoing_1.add("2.html");
    	outgoing_1.add("3.html");    	
    	graph.addPage("1.html", outgoing_1);
    	Vector<String> outgoing_2 = new Vector<>();
    	outgoing_2.add("4.html");
    	outgoing_2.add("6.html");
    	graph.addPage("2.html", outgoing_2);    	
    	
    	Vector<String> outgoing_3 = new Vector<>();
    	outgoing_3.add("4.html");
    	outgoing_3.add("5.html");
    	graph.addPage("3.html", outgoing_3);
    	
        assertEquals(6, graph.getTotalPageCount());
        assertEquals(2, graph.getIncomingLinks("4.html").size());
        assertTrue(graph.getIncomingLinks("4.html").contains("2.html"));
        assertTrue(graph.getIncomingLinks("4.html").contains("3.html"));
    }
    
    @Test
    public void testOutgoingLinksCounts() {
    	WebGraph graph = new WebGraph();
    	Vector<String> outgoing_1 = new Vector<>();
    	outgoing_1.add("2.html");
    	outgoing_1.add("3.html");
    	outgoing_1.add("4.html");
    	graph.addPage("1.html", outgoing_1);
    	
    	Vector<String> outgoing_2 = new Vector<>();
    	outgoing_2.add("3.html");
    	outgoing_2.add("4.html");
    	graph.addPage("2.html", outgoing_2);    	
    	
    	Vector<String> outgoing_3 = new Vector<>();
    	outgoing_3.add("1.html");
    	graph.addPage("3.html", outgoing_3);
    	
    	Vector<String> outgoing_4 = new Vector<>();
    	outgoing_4.add("1.html");
    	outgoing_4.add("3.html");
    	graph.addPage("4.html", outgoing_4);

        assertEquals(3, graph.getOutgoingLinks("1.html").size());
        assertEquals(2, graph.getOutgoingLinks("2.html").size());
        assertEquals(1, graph.getOutgoingLinks("3.html").size());
        assertEquals(2, graph.getOutgoingLinks("4.html").size());    	    
    }    


    

}
