package com.github.coderodde.graph.sp;

import com.github.coderodde.graph.impl.DirectedGraph;
import java.util.List;

/**
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6 ()
 * @since 1.6 ()
 */
public abstract class AbstractDagShortestPathQueryProvider {
    
    protected DirectedGraph graph;
    
    public abstract List<Integer> queryShortestPath(Integer sourceNode,
                                                    Integer targetNode);
}
