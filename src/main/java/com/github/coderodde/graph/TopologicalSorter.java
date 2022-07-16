package com.github.coderodde.graph;

import com.github.coderodde.graph.impl.DirectedGraph;
import java.util.List;

/**
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6 (Jul 15, 2022)
 * @since 1.6 (Jul 15, 2022)
 */
public interface TopologicalSorter {

    List<Integer> sort(DirectedGraph graph) throws GraphContainsCyclesException;
}
