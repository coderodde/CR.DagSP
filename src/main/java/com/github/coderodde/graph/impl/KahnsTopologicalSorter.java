package com.github.coderodde.graph.impl;

import com.github.coderodde.graph.GraphContainsCyclesException;
import com.github.coderodde.graph.TopologicalSorter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6 ()
 * @since 1.6 ()
 */
public class KahnsTopologicalSorter implements TopologicalSorter {

    @Override
    public List<Integer> sort(DirectedGraph graph)
            throws GraphContainsCyclesException {
        
        List<Integer> children = new ArrayList<>(graph.size());
        // First, get a copy of the input graph, since the algorithm  removes
        // the arcs from the graph it works on:
        graph = new DirectedGraph(graph);
        List<Integer> sortedNodeList = new ArrayList<>(graph.size());
        Set<Integer> nodesToProcess = getStartNodes(graph);
        
        while (!nodesToProcess.isEmpty()) {
            Integer node = removeFromSet(nodesToProcess);
            sortedNodeList.add(node);
            
            children.clear();
            children.addAll(graph.getChildrenOf(node));
            
            for (Integer child : children) {
                graph.removeEdge(node, child);
                
                if (graph.getParentsOf(child).size() == 0) {
                    nodesToProcess.add(child);
                }
            }
        }
        
        if (graph.getNumberOfEdges() > 0) {
            throw new GraphContainsCyclesException("The input digraph is not acyclic.");
        }
        
        return sortedNodeList;
    }
    
    private Set<Integer> getStartNodes(DirectedGraph graph) {
        Set<Integer> set = new HashSet<>();
        
        for (Integer node : graph.getAllNodes()) {
            if (graph.getParentsOf(node).isEmpty()) {
                set.add(node);
            }
        }
        
        return set;
    }
    
    private Integer removeFromSet(Set<Integer> set) {
        Iterator<Integer> iterator = set.iterator();
        Integer node = iterator.next();
        iterator.remove();
        return node;
    }
}
