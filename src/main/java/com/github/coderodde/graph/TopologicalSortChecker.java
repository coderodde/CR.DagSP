package com.github.coderodde.graph;

import com.github.coderodde.graph.impl.DirectedGraph;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * This class contains a static method for checking whether the input node list
 * is topologically sorted.
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6 (Jul 16, 2022)
 * @since 1.6 (Jul 16, 2022)
 */
public final class TopologicalSortChecker {

    /**
     * Checks whether the input node list is in topological order.
     * 
     * @param graph the graph.
     * @param nodes the node list to check.
     * @return {@code true} if and only if the input node list is in 
     *         topological order.
     */
    public static boolean isTopologicallySorted(DirectedGraph graph,
                                                List<Integer> nodes) {
        Objects.requireNonNull(nodes);
        Map<Integer, Integer> indices = getIndexMap(nodes); 
        
        for (Integer node : nodes) {
            if (!isValidNodePosition(graph, node, indices)) {
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * Returns the map mapping each node to its appearance index in the 
     * {@code nodes} list.
     * 
     * @param nodes the node list from which to compute the index map.
     * @return the node-to-index map.
     */
    private static Map<Integer, Integer> getIndexMap(List<Integer> nodes) {
        Map<Integer, Integer> map = new HashMap<>(nodes.size());
        
        for (int index = 0; index < nodes.size(); index++) {
            map.put(nodes.get(index), index);
        }
        
        return map;
    }
    
    /**
     * Checks that there is no parents of {@code node} on the right of 
     * {@code node}, and that there is no children of {@code node} on the left
     * of {@code node}.
     * 
     * @param graph the target graph.
     * @param node the node to check.
     * @param indexMap the map mapping nodes to their appearance indices.
     * @return {@code true} if and only if the {@code node} is in valid 
     *         position.
     */
    private static boolean isValidNodePosition(DirectedGraph graph,
                                               Integer node, 
                                               Map<Integer, Integer> indexMap) {
        Integer nodeIndex = indexMap.get(node);
        
        for (Integer parent : graph.getParentsOf(node)) {
            // >= in the condition in order to fail on self-loops:
            if (indexMap.get(parent) >= nodeIndex) {
                return false;
            }
        }
        
        for (Integer child : graph.getChildrenOf(node)) {
            if (indexMap.get(child) < nodeIndex) {
                return false;
            }
        }
        
        return true;
    }
}
