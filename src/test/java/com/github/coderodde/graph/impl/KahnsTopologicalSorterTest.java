package com.github.coderodde.graph.impl;

import com.github.coderodde.graph.GraphContainsCyclesException;
import static com.github.coderodde.graph.TopologicalSortChecker.isTopologicallySorted;
import com.github.coderodde.graph.TopologicalSorter;
import java.util.List;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class KahnsTopologicalSorterTest extends AbstractTopologicalSorterTest {

    private final TopologicalSorter sorter = new KahnsTopologicalSorter();
    
    @Test
    public void testSortAcyclic() throws GraphContainsCyclesException {
        List<Integer> sortedNodes = sorter.sort(acyclicGraph);
        
        assertTrue(isTopologicallySorted(acyclicGraph, sortedNodes));
    }
    
    @Test(expected = GraphContainsCyclesException.class)
    public void testSortCyclic() throws GraphContainsCyclesException {
        sorter.sort(cyclicGraph);
    }
}
