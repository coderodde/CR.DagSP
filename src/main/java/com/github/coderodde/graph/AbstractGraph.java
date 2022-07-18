package com.github.coderodde.graph;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * This class defines the API for graph data structures. The actual nodes are 
 * represented as objects of type {@link Integer}. The client programmer should 
 * always be able to map the nodes to domain specific objects. 
 * <p>
 * Not only the query methods return a boolean value, but any other method
 * returns a boolean value indicating whether the structure of the graph has 
 * changed.
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.61 (Jul 18, 2022)
 * @version 1.6 (Jan 10, 2016)
 * @since 1.6 (Jan 10, 2016)
 */
public abstract class AbstractGraph {

    /**
     * This field caches the amount of changes made to this graph. This is used
     * for keeping track whether the structure of the graph has changed since 
     * the previous check of the modification count. Adding or removing a node
     * contributes one unit to the counter, and adding or removing or updating 
     * an edge contributes one unit as well. Note, that if we remove a node that
     * has incident edges to it, the counter will reflect the removal of the 
     * edges as well.
     */
    protected long modificationCount;
    
    /**
     * Caches the number of edges in this graph.
     */
    protected int edges;
    
    /**
     * The set of graph topology listeners.
     */
    protected final Set<GraphTopologyListener> listeners = new HashSet<>();

    /**
     * Returns the number of nodes in this graph.
     * 
     * @return the size of this graph. 
     */
    public abstract int size();

    /**
     * Returns the number of edges in this graph.
     * 
     * @return the number of edges. 
     */
    public int getNumberOfEdges() {
        return edges;
    }
    
    /**
     * Returns the modification count of this graph.
     * @return the modification count of this graph.
     */
    public long getModificationCount() {
        return modificationCount;
    }

    /**
     * Adds the node {@code node} to this graph.
     * 
     * @param node the node to add.
     * @return {@code true} if the structure of this graph has changed, which is
     *         the same as that the added node was not present in the graph.
     */
    public abstract boolean addNode(Integer node);

    /**
     * Checks whether the given node is present in this graph.
     * 
     * @param node the query node.
     * @return {@code true} if and only if the query node is in this graph.
     */
    public abstract boolean hasNode(Integer node);

    /**
     * If {@code node} is present in this graph, removes all edges incident on
     * it.
     * 
     * @param node the node to clear.
     * @return {@code true} if and only if the node {@code node} had at least 
     *         one incident edge and, thus, the structure of the graph changed.
     */
    public abstract boolean clearNode(Integer node);

    /**
     * Removes the argument node from this graph.
     * 
     * @param node the node to remove.
     * @return {@code true} if and only if the node was present in the graph 
     *         which means that the structure of the graph has changed.
     */
    public abstract boolean removeNode(Integer node);

    /**
     * Creates an edge between {@code tailNode} and {@code headNode} with weight 
     ' {@code weight}. It depends on the concrete implementation of this
     * abstract class whether the edge {@code (tailNode, headNode)} is directed
     * or undirected.
     * <p>
     * If some of the input nodes are not present in this graph, they will be 
     * created silently.
     * 
     * @param tailNode the tail node of the edge.
     * @param headNode the head node of the edge.
     * @param weight the weight of the edge.
     * @return {@code true} if and only if the edge was not present in the 
     *         graph, or the weight of the edge has changed.
     */
    public abstract boolean addEdge(Integer tailNode, 
                                    Integer headNode, 
                                    double weight);

    /**
     * Creates an edge between {@code tailNodeId} and {@code headNodeId} with
     * the default weight of 1.0. This method is a shortcut for constructing
     * (virtually) unweighted graphs.
     * 
     * @param tailNode the tail node of the edge.
     * @param headNode the head node of the edge.
     * @return {@code true} if and only if the edge was not present in the
     *         graph, or the weight of the edge has changed.
     */
    public boolean addEdge(Integer tailNode, Integer headNode) {
        return addEdge(tailNode, headNode, 1.0);
    }

    /**
     * Returns a boolean value indicating whether this graph contains an edge
     * from {@code tailNode} to {@code headNode}. 
     * 
     * @param tailNode the tail node of the query edge.
     * @param headNode the head node of the query edge.
     * @return {@code true} if and only if the query edge is in this graph.
     */
    public abstract boolean hasEdge(Integer tailNode, Integer headNode);

    /**
     * Returns the weight of the edge {@code (tailNode, headNode)}. If the
     * query edge does not exist, returns {@link java.lang.Double#NaN}.
     * 
     * @param tailNode the tail node of the query edge.
     * @param headNode the head node of the query edge.
     * @return the weight of the edge.
     */
    public abstract double getEdgeWeight(Integer tailNode, Integer headNode);

    /**
     * Removes the edge from {@code tailNode} and {@code headNode}.
     * 
     * @param tailNode the tail node of the edge to remove.
     * @param headNode the head node of the edge to remove.
     * @return {@code true} if and only if the target edge was in this graph,
     *         and thus is removed.
     */
    public abstract boolean removeEdge(Integer tailNode, Integer headNode);

    /**
     * Returns the set of all nodes that are children of the node 
     * {@code node}. It depends on the actual graph implementation what is 
     * understood by the term <i>child</i>. In unweighted graphs, every child 
     * is also a parent of a node, which is not necessarily true in directed 
     * graphs.
     * 
     * @param node the query node.
     * @return set of nodes that are children of the argument node.
     */
    public abstract Set<Integer> getChildrenOf(Integer node);

    /**
     * Returns the set of all nodes that are parents of the node {@code node}.
     * 
     * @see #getChildrenOf(Integer) 
     * @param node the query node.
     * @return set of nodes that are parent of the argument node.
     */
    public abstract Set<Integer> getParentsOf(Integer node);

    /**
     * Returns the set of all nodes stored in this graph.
     * 
     * @return the set of all nodes.
     */
    public abstract Set<Integer> getAllNodes();

    /**
     * Removes all nodes and edges from this graph.
     */
    public abstract void clear();
    
    /**
     * Adds a listener to this graph.
     * 
     * @param listener the listener to add.
     */
    public void addGraphTopologyListener(GraphTopologyListener listener) {
        listeners.add(Objects.requireNonNull(listener));
    }
    
    /**
     * Removes a listener from this graph.
     * 
     * @param listener the listener to remove.
     */
    public void removeGraphTopologyListener(GraphTopologyListener listener) {
        listeners.remove(listener);
    }
    
    /**
     * Delegates the {@code onAddNode} message to all the listeners.
     * @param node 
     */
    protected void callListenersOnAddNode(Integer node) {
        for (GraphTopologyListener listener : listeners) {
            listener.onAddNode(node);
        }
    }
    
    /**
     * Delegates the {@code onAddEdge} message to all the listeners.
     * 
     * @param tailNode the tail node of the added edge.
     * @param headNode the head node of the added edge.
     * @param weight the weight of the added edge.
     */
    protected void callListenersOnAddEdge(Integer tailNode, 
                                          Integer headNode, 
                                          double weight) {
        for (GraphTopologyListener listener : listeners) {
            listener.onAddEdge(tailNode, headNode, weight);
        }
    }
    
    /**
     * Delegates the {@code onUpdateEdgeWeight} message to all the listeners.
     * 
     * @param tailNode the tail node of the updated edge.
     * @param headNode the head node of the updated edge.
     * @param oldWeight the old weight of the updated edge.
     * @param newWeight the new weight of the updated edge.
     */
    protected void callListenersOnUpdateEdgeWeight(Integer tailNode, 
                                                   Integer headNode, 
                                                   double oldWeight,
                                                   double newWeight) {
        for (GraphTopologyListener listener : listeners) {
            listener.onUpdateEdgeWeight(tailNode, 
                                        headNode, 
                                        oldWeight, 
                                        newWeight);
        }
    }
    
    /**
     * Delegates the {@code onRemoveNode} message to all the listeners.
     * 
     * @param node the removed node.
     */
    protected void callListenersOnRemoveNode(Integer node) {
        for (GraphTopologyListener listener : listeners) {
            listener.onRemoveNode(node);
        }
    }
    
    /**
     * Delegates the {@code onClearNode} message to all the listeners.
     * 
     * @param node the removed node.
     */
    protected void callListenersOnClearNode(Integer node) {
        for (GraphTopologyListener listener : listeners) {
            listener.onClearNode(node);
        }
    }
    
    /**
     * Delegates the {@code onRemoveEdge} message to all the listeners.
     * 
     * @param tailNodee the tail node of the removed edge.
     * @param headNode the head node of the removed edge.
     * @param weight the weight of the removed edge.
     */
    protected void callListenersOnRemoveEdge(Integer tailNodee, 
                                             Integer headNode, 
                                             double weight) {
        for (GraphTopologyListener listener : listeners) {
            listener.onRemoveEdge(tailNodee, headNode, weight);
        }
    }
    
    /**
     * Delegates the {@code onClearGraph} message to all the listeners.
     */
    protected void callListenerssOnClearGraph() {
        for (GraphTopologyListener listener : listeners) {
            listener.onClearGraph();
        }
    }
    
    /**
     * Delegates a message that any of the graph topology changed.
     */
    protected void callListenersOnAny() {
        for (GraphTopologyListener listener : listeners) {
            listener.onAny();
        }
    }
}