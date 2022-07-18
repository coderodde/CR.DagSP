package com.github.coderodde.graph;

import com.github.coderodde.graph.impl.DirectedGraph;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * This interface defines the API for topological sorters.
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6 (Jul 15, 2022)
 * @since 1.6 (Jul 15, 2022)
 */
public interface TopologicalSorter {

    /**
     * Attempts to sort the input graph topologically. If the input graph 
     * contains cycles, an instance of {@link GraphContainsCyclesException} is
     * thrown.
     * 
     * @param graph the graph to sort.
     * @return a list of nodes in topological order.
     * @throws GraphContainsCyclesException if the input graph contains cycles.
     */
    List<Integer> sort(DirectedGraph graph) throws GraphContainsCyclesException;
    
    /**
     * Removes and returns a node from a set of nodes.
     * 
     * @param set the set to remove from.
     * @return a node.
     */
    default Integer removeNodeFromSet(Set<Integer> set) {
        Iterator<Integer> iterator = set.iterator();
        Integer node = iterator.next();
        iterator.remove();
        return node;
    }
}
