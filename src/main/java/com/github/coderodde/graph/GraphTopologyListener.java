package com.github.coderodde.graph;

/**
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6 ()
 * @since 1.6 ()
 */
public interface GraphTopologyListener {

    /**
     * Called when adding a node to the owner graph.
     * 
     * @param node added node.
     */
    default void onAddNode(Integer node) {}
    
    /**
     * Called when adding an edge to the owner graph.
     * 
     * @param tail the tail node.
     * @param head the head node.
     * @param weight the edge weight.
     */
    default void onAddEdge(Integer tail, Integer head, double weight) {}
    
    default void onUpdateEdgeWeight(Integer tail, 
                                    Integer head, 
                                    double oldWeight,
                                    double newWeight) {}
    
    default void onRemoveNode(Integer node) {}
    
    default void onClearNode(Integer node) {}
    
    default void onRemoveEdge(Integer tail, Integer head, double weight) {}
    
    default void onClearGraph() {}
    
    default void onAny() {}
}
