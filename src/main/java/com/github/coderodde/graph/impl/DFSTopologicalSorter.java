package com.github.coderodde.graph.impl;

import com.github.coderodde.graph.GraphContainsCyclesException;
import com.github.coderodde.graph.TopologicalSorter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * This class implemnts
 * @author Rodion "rodde" Efremov
 * @version 1.6 ()
 * @since 1.6 ()
 */
public class DFSTopologicalSorter implements TopologicalSorter {
    
    @Override
    public List<Integer> sort(DirectedGraph graph) 
            throws GraphContainsCyclesException {
        
        List<Integer> sortedNodes = new ArrayList<>(graph.size());
        Set<Integer> unmarkedNodes = new HashSet<>(graph.getAllNodes());
        Set<Integer> temporarilyMarkedNodes = new HashSet<>();
        Set<Integer> permanentlyMarkedNodes = new HashSet<>();
        
        while (!unmarkedNodes.isEmpty()) {
            Integer node = removeNode(unmarkedNodes);
            sortUtil(node, 
                     temporarilyMarkedNodes, 
                     permanentlyMarkedNodes, 
                     graph, 
                     sortedNodes);
        }
        
        Collections.reverse(sortedNodes);
        return sortedNodes;
    }
    
    private static Integer removeNode(Set<Integer> nodes) {
        Iterator<Integer> iterator = nodes.iterator();
        Integer removedNode = iterator.next();
        iterator.remove();
        return removedNode;
    }
    
    private static void sortUtil(
            Integer node, 
            Set<Integer> temporarilyMarked, 
            Set<Integer> permanentlyMarked, 
            DirectedGraph graph, 
            List<Integer> sortedNodes) 
    throws GraphContainsCyclesException {
        
        if (permanentlyMarked.contains(node)) {
            return;
        }
        
        if (temporarilyMarked.contains(node)) {
            throw new GraphContainsCyclesException();
        }
        
        temporarilyMarked.add(node);
        
        for (Integer child : graph.getChildrenOf(node)) {
            sortUtil(child, 
                     temporarilyMarked, 
                     permanentlyMarked, 
                     graph, 
                     sortedNodes);
        }
        
        temporarilyMarked.remove(node);
        permanentlyMarked.add(node);
        // Here we need to prepend the node, and not to append.
        // Since ArrayList is slow on prepend, we will reverse
        //the list when computatoin is done.
        sortedNodes.add(node); 
    }
}
