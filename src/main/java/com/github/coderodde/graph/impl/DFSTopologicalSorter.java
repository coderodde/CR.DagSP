package com.github.coderodde.graph.impl;

import com.github.coderodde.graph.GraphContainsCyclesException;
import com.github.coderodde.graph.TopologicalSorter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
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
        Deque<Integer> nodeStack = new ArrayDeque<>();
        Deque<Iterator<Integer>> iteratorStack = new ArrayDeque<>();
        
        while (!unmarkedNodes.isEmpty()) {
            Integer root = removeNodeFromSet(unmarkedNodes);
            nodeStack.add(root);
            iteratorStack.add(graph.getChildrenOf(root).iterator());
            visit(graph,
                  nodeStack,
                  iteratorStack, 
                  unmarkedNodes,
                  temporarilyMarkedNodes,
                  permanentlyMarkedNodes,
                  sortedNodes);
        }
        
        Collections.reverse(sortedNodes);
        return sortedNodes;
    }
    
    private static void visit(DirectedGraph graph,
                              Deque<Integer> nodeStack,
                              Deque<Iterator<Integer>> iteratorStack,
                              Set<Integer> unmarkedNodes,
                              Set<Integer> temporarilyMarkedNodes,
                              Set<Integer> permanentlyMarkedNodes,
                              List<Integer> sortedNodes) {
        mainLoop:
        while (!nodeStack.isEmpty()) {
            Integer node = nodeStack.peek();
            Iterator<Integer> iterator = iteratorStack.peek();
            
            unmarkedNodes.remove(node);
            temporarilyMarkedNodes.add(node);
            
            while (iterator.hasNext()) {
                Integer child = iterator.next();
                
                if (unmarkedNodes.contains(child)) {
                    nodeStack.push(child);
                    iteratorStack.push(graph.getChildrenOf(child).iterator());
                    continue mainLoop;
                } else if (temporarilyMarkedNodes.contains(child)) {
                    throw new GraphContainsCyclesException();
                }
            }
            
            while (!iteratorStack.isEmpty() 
                    && !iteratorStack.peek().hasNext()) {
                iteratorStack.pop();
                node = nodeStack.pop();
                temporarilyMarkedNodes.remove(node);
                permanentlyMarkedNodes.add(node);
            }
        }
    }
}
