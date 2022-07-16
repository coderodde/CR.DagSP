package com.github.coderodde.graph.impl;

import java.util.Set;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class DirectedGraphTest {
    
    @Test
    public void copyConstructor() {
        DirectedGraph g = new DirectedGraph();
        
        g.addNode(1);
        g.addNode(2);
        g.addNode(3);
        
        g.addEdge(1, 2, 1.0);
        g.addEdge(2, 3, 2.0);
        g.addEdge(3, 1, 3.0);
        g.addEdge(2, 1, 4.0);
        
        DirectedGraph copied = new DirectedGraph(g);
        
        assertTrue(copied.hasEdge(1, 2));
        assertTrue(copied.hasEdge(2, 3));
        assertTrue(copied.hasEdge(3, 1));
        assertTrue(copied.hasEdge(2, 1));
        
        assertFalse(copied.hasEdge(3, 2));
        assertFalse(copied.hasEdge(1, 3));
        
        assertEquals(1.0, copied.getEdgeWeight(1, 2), 0.01);
        assertEquals(2.0, copied.getEdgeWeight(2, 3), 0.01);
        assertEquals(3.0, copied.getEdgeWeight(3, 1), 0.01);
        assertEquals(4.0, copied.getEdgeWeight(2, 1), 0.01);
    }
}