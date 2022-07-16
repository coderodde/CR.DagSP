package com.github.coderodde.graph.impl;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DirectedGraphTest {
    
    private final DirectedGraph g = new DirectedGraph();
    
    @Before
    public void setup() {
        g.clear();
    }
    
    @Test
    public void copyConstructor() {
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
    
    @Test
    public void addNode() {
        g.addNode(1);
        g.addNode(2);
        
        assertTrue(g.hasNode(1));
        assertTrue(g.hasNode(2));
        
        assertFalse(g.hasNode(3));
    }
    
    @Test
    public void addEdge() {
        g.addEdge(1, 2, 1.0);
        g.addEdge(2, 1, 2.0);
        g.addEdge(2, 3, 3.0);
        
        assertTrue(g.hasEdge(1, 2));
        assertTrue(g.hasEdge(2, 1));
        assertTrue(g.hasEdge(2, 3));
        
        assertFalse(g.hasEdge(3, 2));
    }
    
    @Test
    public void clearNode() {
        g.addEdge(1, 2, 1.0);
        g.addEdge(3, 2, 2.0);
        g.addEdge(2, 4, 1.5);
        g.addEdge(1, 4, 3.0);
        
        assertEquals(4, g.size());
        assertEquals(4, g.getNumberOfEdges());
        
        g.clearNode(2);
        
        assertEquals(4, g.size());
        assertEquals(1, g.getNumberOfEdges());
    }
    
    @Test
    public void removeNode() {
        g.addEdge(1, 2, 1.0);
        g.addEdge(3, 2, 2.0);
        g.addEdge(2, 4, 1.5);
        g.addEdge(1, 4, 3.0);
        
        assertEquals(4, g.size());
        assertEquals(4, g.getNumberOfEdges());
        
        g.removeNode(2);
        
        assertEquals(3, g.size());
        assertEquals(1, g.getNumberOfEdges());
    }
    
    @Test
    public void removeEdge() {
        g.addEdge(1, 2, 1.0);
        g.addEdge(3, 2, 2.0);
        g.addEdge(2, 4, 1.5);
        g.addEdge(1, 4, 3.0);
        
        assertEquals(4, g.size());
        assertEquals(4, g.getNumberOfEdges());
        
        g.removeEdge(1, 4);
        
        assertEquals(4, g.size());
        assertEquals(3, g.getNumberOfEdges());
    }
    
    @Test
    public void clear() {
        g.addEdge(1, 2, 1.0);
        g.addEdge(3, 2, 2.0);
        g.addEdge(2, 4, 1.5);
        g.addEdge(1, 4, 3.0);
        
        assertEquals(4, g.size());
        assertEquals(4, g.getNumberOfEdges());
        
        g.clear();
        
        assertEquals(0, g.size());
        assertEquals(0, g.getNumberOfEdges());
    }
    
    @Test
    public void hasNode() {
        g.addNode(1);
        
        assertTrue(g.hasNode(1));
        assertFalse(g.hasNode(2));
    }
    
    @Test
    public void hasEdge() {
        g.addEdge(1, 2, 1.0);
        g.addEdge(3, 4, 2.0);
        
        assertTrue(g.hasEdge(1, 2));
        assertTrue(g.hasEdge(3, 4));
        
        assertFalse(g.hasEdge(2, 1));
        assertFalse(g.hasEdge(4, 3));
    }
}