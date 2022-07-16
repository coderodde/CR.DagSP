package com.github.coderodde.graph.sp;

import com.github.coderodde.graph.TopologicalSortChecker;
import com.github.coderodde.graph.TopologicalSorter;
import com.github.coderodde.graph.impl.DirectedGraph;
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
public abstract class AbstractGraphPreprocessor {
    
    private static final long UNSET_PREPROCESSING_DURATION = -1L;
    
    protected final List<Integer> topologicallySortedNodes;
    protected final Map<Integer, Integer> indexMap;
    protected long preprocessingDuration = UNSET_PREPROCESSING_DURATION;
    protected final DirectedGraph graph;
    
    public AbstractGraphPreprocessor(DirectedGraph graph) {
        this.graph = Objects.requireNonNull(graph);
        this.topologicallySortedNodes = new ArrayList<>(graph.size());
        this.indexMap = new HashMap<>(graph.size());
    }

    public abstract void preprocessGraph();
    
    protected void preprocessGraph(TopologicalSorter topologicalSorter) {
        long startTime = System.currentTimeMillis();
        
        this.topologicallySortedNodes.clear();
        this.topologicallySortedNodes.addAll(topologicalSorter.sort(graph));
        
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        
        this.preprocessingDuration = duration;
    }
    
    public List<Integer> getTopologicallySortedNodes() {
        return topologicallySortedNodes;
    }
    
    public Map<Integer, Integer> getIndexMap() {
        return indexMap;
    }
    
    public long getPreprocessingDuration() {
        if (preprocessingDuration == UNSET_PREPROCESSING_DURATION) {
            throw new IllegalStateException("Preprocessing was not run.");
        }
        
        return preprocessingDuration;
    }
    
    protected void computeIndexMap() {
        for (int index = 0; index < topologicallySortedNodes.size(); index++) {
            indexMap.put(topologicallySortedNodes.get(index), index);
        }
    }
}
