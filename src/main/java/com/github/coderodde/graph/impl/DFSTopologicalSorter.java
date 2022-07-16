package com.github.coderodde.graph.impl;

import com.github.coderodde.graph.GraphContainsCyclesException;
import com.github.coderodde.graph.TopologicalSorter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6 ()
 * @since 1.6 ()
 */
public class DFSTopologicalSorter implements TopologicalSorter {

    private enum NodeColor {
        UNMARKED,
        TEMPORARY,
        PERMANENT,
    }
    
    @Override
    public List<Integer> sort(DirectedGraph graph) 
            throws GraphContainsCyclesException {
        
        List<Integer> sortedNodeList = new ArrayList<>(graph.size());
        Map<Integer, NodeColor> colorMap = new HashMap<>();
        
        
        
        return sortedNodeList;
    }
}
