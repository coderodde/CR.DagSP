package com.github.coderodde.graph.sp.impl;

import com.github.coderodde.graph.GraphTopologyListener;
import com.github.coderodde.graph.impl.DirectedGraph;
import com.github.coderodde.graph.sp.AbstractDagShortestPathQueryProvider;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6 ()
 * @since 1.6 ()
 */
public class PreprocessingDagShortestPathQueryProvider extends AbstractDagShortestPathQueryProvider {
    
    /**
     * Stores all the graph nodes in topological order.
     */
    private final List<Integer> topologicallySortedNodes = new ArrayList<>();
    
    /**
     * Maps each node to its appearance index in 
     * {@code topologicallySortedNodes}.
     */
    private final Map<Integer, Integer> indexMap = new HashMap<>();
    
    public PreprocessingDagShortestPathQueryProvider(DirectedGraph graph) {
        super.graph = Objects.requireNonNull(graph);
    }

    @Override
    public List<Integer> queryShortestPath(Integer sourceNode, Integer targetNode) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    private final class AnyGraphOperationListener 
            implements GraphTopologyListener {
        
        @Override
        public void onAny() {
            
        }
    }
}
