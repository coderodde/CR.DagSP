package com.github.coderodde.graph.sp;

import com.github.coderodde.graph.impl.DirectedGraph;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6 ()
 * @since 1.6 ()
 */
public abstract class AbstractDagShortestPathQueryRunner {
    
    protected final DirectedGraph graph;
    protected final AbstractGraphPreprocessor graphPreprocessor;
    
    public AbstractDagShortestPathQueryRunner(
            DirectedGraph graph,
            AbstractGraphPreprocessor graphPreprocessor) {
        this.graph = Objects.requireNonNull(graph);
        this.graphPreprocessor = graphPreprocessor;
    }
    
    public abstract long getPreprocessingDuration();
    
    public abstract DirectedGraph.Path queryShortestPath(Integer sourceNode,
                                                         Integer targetNode);
    
    /**
     * Reconstructs the shortest path.
     * 
     * @param targetNode the target node in the target graph.
     * @param parentMap  the map mapping each node to its predecessor.
     * @return the shortest path.
     */
    protected DirectedGraph.Path 
        tracebackPath(Integer targetNode, 
                      Map<Integer, Integer> parentMap) {
            
        List<Integer> pathList = new ArrayList<>();
        Integer node = parentMap.get(targetNode);
        
        while (node != null) {
            pathList.add(node);
            node = parentMap.get(node);
        }
        
        Collections.reverse(pathList);
        return new DirectedGraph.Path(graph, pathList);
    }
        
    protected void checkSourceNodeInGraph(Integer sourceNode) {
        if (!graph.hasNode(sourceNode)) {
            throw new IllegalArgumentException(
                    "The source node (" 
                            + sourceNode 
                            + ") is not in the graph.");
        }
    }
    
    protected void checkTargetNodeInGraph(Integer targetNode) {
        if (!graph.hasNode(targetNode)) {
            throw new IllegalArgumentException(
                    "The target node (" 
                            + targetNode 
                            + ") is not in the graph.");
        }
    }
}
