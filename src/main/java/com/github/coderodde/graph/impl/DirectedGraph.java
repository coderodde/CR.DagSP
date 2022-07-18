package com.github.coderodde.graph.impl;

import com.github.coderodde.graph.AbstractGraph;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * This class implements a directed graph.
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.61 (Jul 16, 2022)
 * @version 1.6 (Jan 11, 2016)
 * @since 1.6 (Jan 11, 2016)
 */
public class DirectedGraph extends AbstractGraph {

    public static class Path {
        private final DirectedGraph ownerGraph;
        private final List<Integer> pathNodes = new ArrayList<>();
        private final double totalCost;
        
        public Path(DirectedGraph ownerGraph, List<Integer> pathNodes) {
            this.ownerGraph = ownerGraph;
            this.pathNodes.addAll(pathNodes);
            this.totalCost = computeTotalCost();
        }
        
        public Integer getNode(int index) {
            return pathNodes.get(index);
        }
        
        public double getTotalCost() {
            return totalCost;
        }
        
        private double computeTotalCost() {
            double totalCost = 0.0;
            
            for (int i = 0; i < pathNodes.size() - 1; ++i) {
                totalCost = ownerGraph.getEdgeWeight(pathNodes.get(i),
                                                     pathNodes.get(i + 1));
            }
            
            return totalCost;
        }
    }
    
    private final Map<Integer, 
                      Map<Integer, 
                          Double>> parentMap = new LinkedHashMap<>();

    private final Map<Integer, 
                      Map<Integer, 
                          Double>> childMap = new LinkedHashMap<>();
    
    
    public DirectedGraph() {
        
    }
    
    /**
     * A copy constructor. The input graph remains intact.
     * 
     * @param graph the graph whose graph topology to assume. This includes the
     *              arc weights as well.
     */
    public DirectedGraph(DirectedGraph graph) {
        for (Integer node : graph.getAllNodes()) {
            parentMap.put(node, new HashMap<>());
            childMap.put(node, new HashMap<>());
        }
        
        for (Integer node : graph.getAllNodes()) {
            Map<Integer, Double> childrenMap = new HashMap<>();
            Map<Integer, Double> parentsMap = new HashMap<>();
            
            for (Integer child : graph.getChildrenOf(node)) {
                childrenMap.put(child, graph.getEdgeWeight(node, child));
            }
            
            childMap.get(node).putAll(childrenMap);
            
            for (Integer parent : graph.getParentsOf(node)) {
                parentsMap.put(parent, graph.getEdgeWeight(parent, node));
            }
            
            parentMap.get(node).putAll(parentsMap);
        }
        
        edges = graph.edges;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int size() {
        return parentMap.size();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getNumberOfEdges() {
        return edges;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean addNode(Integer node) {
        Objects.requireNonNull(node);
        
        if (parentMap.containsKey(node)) {
            return false;
        }

        parentMap.put(node, new LinkedHashMap<>());
        childMap .put(node, new LinkedHashMap<>());
        callListenersOnAddNode(node);
        callListenersOnAny();
        modificationCount++;
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasNode(Integer node) {
        Objects.requireNonNull(node);
        return parentMap.containsKey(node);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean clearNode(Integer node) {
        Objects.requireNonNull(node);
        
        if (!hasNode(node)) {
            return false;
        }

        Map<Integer, Double> parents = parentMap.get(node);
        Map<Integer, Double> children = childMap.get(node);

        if (parents.isEmpty() && children.isEmpty()) {
            return false;
        }

        for (Integer childId : children.keySet()) {
            parentMap.get(childId).remove(node);
        }

        for (Integer parentId : parents.keySet()) {
            childMap.get(parentId).remove(node);
        }

        int mod = parents.size() + children.size();
        edges -= mod;
        modificationCount++;
        callListenersOnClearNode(node);
        callListenersOnAny();
        parents.clear();
        children.clear();
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean removeNode(Integer node) {
        Objects.requireNonNull(node);
        
        if (!hasNode(node)) {
            return false;
        }

        clearNode(node);
        parentMap.remove(node);
        childMap.remove(node);
        callListenersOnRemoveNode(node);
        callListenersOnAny();
        modificationCount++;
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean addEdge(Integer tailNode,    
                           Integer headNode, 
                           double newWeight) {
        
        Objects.requireNonNull(tailNode);
        Objects.requireNonNull(headNode);
        
        addNode(tailNode);
        addNode(headNode);

        if (childMap.get(tailNode).containsKey(headNode)) {
            double oldWeight = childMap.get(tailNode).get(headNode);
            childMap.get(tailNode).put(headNode, newWeight);
            parentMap.get(headNode).put(tailNode, newWeight);
            
            if (oldWeight != newWeight) {
                modificationCount++;
                callListenersOnUpdateEdgeWeight(tailNode, 
                                                headNode, 
                                                oldWeight, 
                                                newWeight);
                callListenersOnAny();
                return true;
            }
            
            return false;
        } else {
            childMap.get(tailNode).put(headNode, newWeight);
            parentMap.get(headNode).put(tailNode, newWeight);
            modificationCount++;
            edges++;
            callListenersOnAddEdge(tailNode, headNode, newWeight);
            callListenersOnAny();
            return true;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasEdge(Integer tailNode, Integer headNode) {
        Objects.requireNonNull(tailNode);
        Objects.requireNonNull(headNode);
        
        if (!childMap.containsKey(tailNode)) {
            return false;
        }

        return childMap.get(tailNode).containsKey(headNode);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getEdgeWeight(Integer tailNode, Integer headNode) {
        Objects.requireNonNull(tailNode);
        Objects.requireNonNull(headNode);
        
        if (!hasEdge(tailNode, headNode)) {
            return Double.NaN;
        }

        return childMap.get(tailNode).get(headNode);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean removeEdge(Integer tailNode, Integer headNode) {
        Objects.requireNonNull(tailNode);
        Objects.requireNonNull(headNode);
        
        if (!childMap.containsKey(tailNode)) {
            return false;
        }

        if (!childMap.get(tailNode).containsKey(headNode)) {
            return false;
        }

        double weight = childMap.get(tailNode).get(headNode);
        childMap .get(tailNode).remove(headNode);
        parentMap.get(headNode).remove(tailNode);
        modificationCount++;
        edges--;
        callListenersOnRemoveEdge(tailNode, headNode, weight);
        callListenersOnAny();
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<Integer> getChildrenOf(Integer node) {
        Objects.requireNonNull(node);
        
        if (!childMap.containsKey(node)) {
            throw new IllegalStateException(
                    "Node " + node + " is not in the graph.");
        }

        return Collections.
                <Integer>unmodifiableSet(childMap.get(node).keySet());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<Integer> getParentsOf(Integer node) {
        Objects.requireNonNull(node);
        
        if (!parentMap.containsKey(node)) {
            throw new IllegalStateException(
                    "Node " + node + " is not in the graph.");
        }

        return Collections.
                <Integer>unmodifiableSet(parentMap.get(node).keySet());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<Integer> getAllNodes() {
        return Collections.<Integer>unmodifiableSet(childMap.keySet());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clear() {
        for (Map.Entry<Integer, Map<Integer, Double>> entry : 
                childMap.entrySet()) {
            modificationCount += entry.getValue().size();
            entry.getValue().clear();
        }
        
        for (Map.Entry<Integer, Map<Integer, Double>> entry :
                parentMap.entrySet()) {
            modificationCount += entry.getValue().size();
            entry.getValue().clear();
        }
        
        modificationCount += edges;
        childMap.clear();
        parentMap.clear();
        edges = 0;
        callListenerssOnClearGraph();
        callListenersOnAny();
    }
}