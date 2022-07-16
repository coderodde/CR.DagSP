package com.github.coderodde.graph.sp.impl;

import com.github.coderodde.graph.GraphTopologyListener;
import com.github.coderodde.graph.PathDoesNotExistException;
import com.github.coderodde.graph.impl.DirectedGraph;
import com.github.coderodde.graph.sp.AbstractDagShortestPathQueryProvider;
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
public class PreprocessingDagShortestPathQueryProvider 
        extends AbstractDagShortestPathQueryProvider {
    
    private List<Integer> topologicallySortedNodes;
    private Map<Integer, Integer> indexMap;
    
    /**
     * Constructs the preprocessing shortest path query provider.
     * 
     * @param graph             the graph in which to run the shortest path 
     *                          search.
     * @param graphPreprocessor the graph preprocessor.
     */
    public PreprocessingDagShortestPathQueryProvider(
            DirectedGraph graph,
            AbstractGraphPreprocessor graphPreprocessor) {
        super(graph, graphPreprocessor);
        graph.addGraphTopologyListener(new AnyGraphOperationListener());
    }

    @Override
    public List<Integer> queryShortestPath(Integer sourceNode, 
                                           Integer targetNode) {
        if (!graph.hasNode(sourceNode)) {
            throw new IllegalArgumentException(
                    "The source node (" 
                            + sourceNode 
                            + ") is not in the graph.");
        }
        
        if (!graph.hasNode(targetNode)) {
            throw new IllegalArgumentException(
                    "The target node (" 
                            + sourceNode 
                            + ") is not in the graph.");
        }
        
        Map<Integer, Double> costMap = new HashMap<>();
        Map<Integer, Integer> parentMap = new HashMap<>();
        
        costMap.put(sourceNode, 0.0);
        parentMap.put(sourceNode, null);
        
        int sourceIndex = indexMap.get(sourceNode);
        int targetIndex = indexMap.get(targetNode);
        
        for (int i = sourceIndex; i <= targetIndex; i++) {
            Integer u = topologicallySortedNodes.get(i);
            
            if (!costMap.containsKey(u)) {
                continue;
            }
            
            if (u.equals(targetNode)) {
                return tracebackPath(u, parentMap);
            }
            
            for (Integer child : graph.getChildrenOf(u)) {
                if (!costMap.containsKey(child) 
                        || costMap.get(child) > 
                           costMap.get(u) + graph.getEdgeWeight(u, child)) {
                    costMap.put(child, 
                                costMap.get(u) + 
                                        graph.getEdgeWeight(u, child));
                    
                    parentMap.put(child, u);
                }
            }
        }
        
        throw new PathDoesNotExistException(sourceNode, targetNode);
    }

    @Override
    public long getPreprocessingDuration() {
        return graphPreprocessor.getPreprocessingDuration();
    }
    
    private final class AnyGraphOperationListener 
            implements GraphTopologyListener {
        
        @Override
        public void onAny() {
            graphPreprocessor.preprocessGraph();
            indexMap = graphPreprocessor.getIndexMap();
            topologicallySortedNodes = 
                    graphPreprocessor.getTopologicallySortedNodes();
        }
    }
}
