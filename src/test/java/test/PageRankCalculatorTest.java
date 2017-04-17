/**
 * 
 */
package test;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Vector;

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
    	Vector<String> outgoing_a = new Vector<>();
    	outgoing_a.add("b");
    	graph.addPage("a", outgoing_a);
    	
    	Vector<String> outgoing_b = new Vector<>();
    	outgoing_b.add("e");
    	graph.addPage("b", outgoing_b);    	
    	
    	Vector<String> outgoing_c = new Vector<>();
    	outgoing_c.add("a");
    	outgoing_c.add("b");
    	outgoing_c.add("d");
    	outgoing_c.add("e");
    	graph.addPage("c", outgoing_c);
    	
    	Vector<String> outgoing_d = new Vector<>();
    	outgoing_d.add("c");
    	outgoing_d.add("e");
    	graph.addPage("d", outgoing_d);    	
    	
    	Vector<String> outgoing_e = new Vector<>();
    	outgoing_e.add("d");
    	graph.addPage("e", outgoing_e);    	
    	
    	PageRankCalculator pageRankCalc = new PageRankCalculator();
    	HashMap<String, Double> pageRank = pageRankCalc.calculatePageRank(graph, 2, 0);
        assertEquals(pageRank.size(), 5);        
        assertEquals(1.0/40, pageRank.get("a").doubleValue(), 0.01);
        assertEquals(3.0/40, pageRank.get("b").doubleValue(), 0.01);
        assertEquals(5.0/40, pageRank.get("c").doubleValue(), 0.01);
        assertEquals(15.0/40, pageRank.get("d").doubleValue(), 0.01);
        assertEquals(16.0/40, pageRank.get("e").doubleValue(), 0.01);
    }
    

}
