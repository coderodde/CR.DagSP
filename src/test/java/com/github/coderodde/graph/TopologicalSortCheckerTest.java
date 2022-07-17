package com.github.coderodde.graph;

import static com.github.coderodde.graph.TopologicalSortChecker.isTopologicallySorted;
import com.github.coderodde.graph.impl.DirectedGraph;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class TopologicalSortCheckerTest {
    
    public TopologicalSortCheckerTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of isTopologicallySorted method, of class TopologicalSortChecker.
     */
    @Test
    public void testIsTopologicallySorted() {
        DirectedGraph g = new DirectedGraph();
        List<Integer> nodeList = new ArrayList<>();
        
        g.addEdge(1, 2, 1.0);
        g.addEdge(2, 3, 2.0);
        g.addEdge(1, 4, 3.0);
        g.addEdge(4, 3, 4.0);
        
        nodeList.addAll(Arrays.asList(1, 2, 4, 3));
                
        boolean topologicallySorted = isTopologicallySorted(g, nodeList);
        
        assertTrue(topologicallySorted);
        
        nodeList.clear();
        nodeList.addAll(Arrays.asList(1, 2, 3, 4));
                
        topologicallySorted = isTopologicallySorted(g, nodeList);
        
        assertFalse(topologicallySorted);
        
        // Check the graph with a self-loop:
        g.clear();
        g.addEdge(1, 2, 1.0);
        g.addEdge(2, 3, 2.0);
        g.addEdge(2, 2, 3.0);
        
        nodeList.clear();
        nodeList.addAll(Arrays.asList(1, 2, 3));
        
        topologicallySorted = isTopologicallySorted(g, nodeList);
        assertFalse(topologicallySorted);
    }
}
