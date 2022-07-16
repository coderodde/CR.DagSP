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
    
    /**
     * Called when the edge weight changed. This is called only when the old and 
     * the new weights are not equal.
     * 
     * @param tail      the tail node of the edge.
     * @param head      the head node of the edge.
     * @param oldWeight the old edge weight.
     * @param newWeight the new edge weight.
     */
    default void onUpdateEdgeWeight(Integer tail, 
                                    Integer head, 
                                    double oldWeight,
                                    double newWeight) {}
    
    /**
     * Called when a node is removed from the graph.
     * 
     * @param node the node to remove.
     */
    default void onRemoveNode(Integer node) {}
    
    /**
     * Called when a node is cleared from its incident edges.
     * 
     * @param node the node to clear.
     */
    default void onClearNode(Integer node) {}
    
    /**
     * Called when an edge is removed.
     * 
     * @param tail   the tail node of the removed edge.
     * @param head   the head node of the removed edge.
     * @param weight the weight of the removed edge.
     */
    default void onRemoveEdge(Integer tail, Integer head, double weight) {}
    
    /**
     * Called when the entire graph is cleared.
     */
    default void onClearGraph() {}
    
    /**
     * Called when any change in topology takes place.
     */
    default void onAny() {}
}
