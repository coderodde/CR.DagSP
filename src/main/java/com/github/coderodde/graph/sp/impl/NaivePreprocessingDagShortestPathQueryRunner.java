package com.github.coderodde.graph.sp.impl;

import com.github.coderodde.graph.PathDoesNotExistException;
import com.github.coderodde.graph.impl.DirectedGraph;
import com.github.coderodde.graph.sp.AbstractDagShortestPathQueryRunner;
import com.github.coderodde.graph.sp.AbstractGraphPreprocessor;
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
public class NaivePreprocessingDagShortestPathQueryRunner 
        extends AbstractDagShortestPathQueryRunner {
    
    private final List<Integer> topologicallySortedNodes = new ArrayList<>();
    
    /**
     * Constructs the preprocessing shortest path query provider.
     * 
     * @param graph             the graph in which to run the shortest path 
     *                          search.
     * @param graphPreprocessor the graph preprocessor.
     */
    public NaivePreprocessingDagShortestPathQueryRunner(
            DirectedGraph graph,
            AbstractGraphPreprocessor graphPreprocessor) {
        super(graph, graphPreprocessor);
    }

    @Override
    public DirectedGraph.Path queryShortestPath(Integer sourceNode, 
                                                Integer targetNode) {
        // If this runner's expected mod count does not match the mod count of
        // the graph, graph is changed and so we need to re-preprocess the 
        // graph.
        checkGraphDirtyStatus();
        
        checkSourceNode(sourceNode);
        checkTargetNode(targetNode);
        
        Map<Integer, Double> costMap = new HashMap<>();
        Map<Integer, Integer> parentMap = new HashMap<>();
        
        costMap.put(sourceNode, 0.0);
        parentMap.put(sourceNode, null);
        
        for (Integer node : topologicallySortedNodes) {
            if (!costMap.containsKey(node)) {
                continue;
            }
            
            if (node.equals(targetNode)) {
                return tracebackPath(node, parentMap);
            }
            
            for (Integer child : graph.getChildrenOf(node)) {
                if (!costMap.containsKey(child) 
                        || costMap.get(child) > 
                           costMap.get(node) + 
                            graph.getEdgeWeight(node, child)) {
                    
                    costMap.put(child, 
                                costMap.get(node) + 
                                        graph.getEdgeWeight(node, child));
                    
                    parentMap.put(child, node);
                }
            }
        }
        
        throw new PathDoesNotExistException(sourceNode, targetNode);
    }

    @Override
    public long getPreprocessingDuration() {
        return graphPreprocessor.getPreprocessingDuration();
    }
    
    private void checkGraphDirtyStatus() {
        if (expectedGraphModCount != graph.getModificationCount()) {
            expectedGraphModCount = graph.getModificationCount();
            graphPreprocessor.preprocessGraph();
            topologicallySortedNodes.clear();
            topologicallySortedNodes.addAll(
                    graphPreprocessor.getTopologicallySortedNodes());
        }
    }
}
