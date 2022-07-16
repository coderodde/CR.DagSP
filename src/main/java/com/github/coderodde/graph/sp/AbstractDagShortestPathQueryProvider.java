package com.github.coderodde.graph.sp;

import com.github.coderodde.graph.impl.DirectedGraph;
import java.util.List;
import java.util.Objects;

/**
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6 ()
 * @since 1.6 ()
 */
public abstract class AbstractDagShortestPathQueryProvider {
    
    protected final DirectedGraph graph;
    protected final AbstractGraphPreprocessor graphPreprocessor;
    
    public AbstractDagShortestPathQueryProvider(
            DirectedGraph graph,
            AbstractGraphPreprocessor graphPreprocessor) {
        this.graph = Objects.requireNonNull(graph);
        this.graphPreprocessor = graphPreprocessor;
    }
    
    public abstract long getPreprocessingDuration();
    
    public abstract List<Integer> queryShortestPath(Integer sourceNode,
                                                    Integer targetNode);
}
