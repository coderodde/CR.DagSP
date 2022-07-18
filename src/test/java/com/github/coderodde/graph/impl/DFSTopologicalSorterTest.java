package com.github.coderodde.graph.impl;

import com.github.coderodde.graph.GraphContainsCyclesException;
import static com.github.coderodde.graph.TopologicalSortChecker.isTopologicallySorted;
import com.github.coderodde.graph.TopologicalSorter;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

public class DFSTopologicalSorterTest extends AbstractTopologicalSorterTest {
    
    private final TopologicalSorter sorter = new DFSTopologicalSorter();
    
    @Test
    public void testSortAcyclic() {
        List<Integer> sortedNodes = sorter.sort(acyclicGraph);
        
        assertTrue(isTopologicallySorted(acyclicGraph, sortedNodes));
    }
    
    @Test(expected = GraphContainsCyclesException.class)
    public void testSortCyclic() {
        sorter.sort(cyclicGraph);
        fail("Should have thrown.");
    }
}
