package com.github.coderodde.graph;

import com.github.coderodde.graph.impl.DirectedGraph;
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
        System.out.println("isTopologicallySorted");
        DirectedGraph graph = null;
        List<Integer> nodes = null;
        boolean expResult = false;
        boolean result = TopologicalSortChecker.isTopologicallySorted(graph, nodes);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
