package com.github.coderodde.graph.sp.impl;

import com.github.coderodde.graph.GraphTopologyListener;
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
    public List<Integer> queryShortestPath(Integer sourceNode, Integer targetNode) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
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
