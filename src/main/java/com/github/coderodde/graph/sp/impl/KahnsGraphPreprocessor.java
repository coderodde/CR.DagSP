package com.github.coderodde.graph.sp.impl;

import com.github.coderodde.graph.TopologicalSorter;
import com.github.coderodde.graph.impl.DirectedGraph;
import com.github.coderodde.graph.impl.KahnsTopologicalSorter;
import com.github.coderodde.graph.sp.AbstractGraphPreprocessor;

/**
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6 ()
 * @since 1.6 ()
 */
public class KahnsGraphPreprocessor extends AbstractGraphPreprocessor {
    
    private final TopologicalSorter topologicalSorter = 
            new KahnsTopologicalSorter();
    
    public KahnsGraphPreprocessor(DirectedGraph graph) {
        super(graph);
    }
    
    @Override
    public void preprocessGraph() {
        this.preprocessGraph(topologicalSorter);
    }
}
