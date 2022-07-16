package com.github.coderodde.graph.impl;

import org.junit.Before;

public class AbstractTopologicalSorterTest {

    protected final DirectedGraph acyclicGraph = new DirectedGraph();
    protected final DirectedGraph cyclicGraph = new DirectedGraph();
    
    @Before
    public void before() {
        acyclicGraph.addEdge(1, 2);
        acyclicGraph.addEdge(2, 3);
        acyclicGraph.addEdge(1, 4);
        acyclicGraph.addEdge(4, 3);
        acyclicGraph.addEdge(1, 3);
        
        cyclicGraph.addEdge(1, 2);
        cyclicGraph.addEdge(2, 3);
        cyclicGraph.addEdge(3, 4);
        cyclicGraph.addEdge(4, 1);
        cyclicGraph.addEdge(1, 3);
    }
}