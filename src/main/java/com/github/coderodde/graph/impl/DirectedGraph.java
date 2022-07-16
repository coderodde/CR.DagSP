package com.github.coderodde.graph.impl;

import com.github.coderodde.graph.AbstractGraph;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
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
    public boolean addNode(int nodeId) {
        if (parentMap.containsKey(nodeId)) {
            return false;
        }

        parentMap.put(nodeId, new LinkedHashMap<>());
        childMap .put(nodeId, new LinkedHashMap<>());
        modificationCount++;
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasNode(int nodeId) {
        return parentMap.containsKey(nodeId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean clearNode(int nodeId) {
        if (!hasNode(nodeId)) {
            return false;
        }

        Map<Integer, Double> parents = parentMap.get(nodeId);
        Map<Integer, Double> children = childMap.get(nodeId);

        if (parents.isEmpty() && children.isEmpty()) {
            return false;
        }

        for (Integer childId : children.keySet()) {
            parentMap.get(childId).remove(nodeId);
        }

        for (Integer parentId : parents.keySet()) {
            childMap.get(parentId).remove(nodeId);
        }

        int mod = parents.size() + children.size();
        edges -= mod;
        modificationCount += mod;
        parents.clear();
        children.clear();
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean removeNode(int nodeId) {
        if (!hasNode(nodeId)) {
            return false;
        }

        clearNode(nodeId);
        parentMap.remove(nodeId);
        childMap.remove(nodeId);
        modificationCount++;
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean addEdge(int tailNodeId, int headNodeId, double weight) {
        addNode(tailNodeId);
        addNode(headNodeId);

        if (childMap.get(tailNodeId).containsKey(headNodeId)) {
            double oldWeight = childMap.get(tailNodeId).get(headNodeId);
            childMap.get(tailNodeId).put(headNodeId, weight);
            parentMap.get(headNodeId).put(tailNodeId, weight);
            
            if (oldWeight != weight) {
                modificationCount++;
                return true;
            }
            
            return false;
        } else {
            childMap.get(tailNodeId).put(headNodeId, weight);
            parentMap.get(headNodeId).put(tailNodeId, weight);
            modificationCount++;
            edges++;
            return true;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasEdge(int tailNodeId, int headNodeId) {
        if (!childMap.containsKey(tailNodeId)) {
            return false;
        }

        return childMap.get(tailNodeId).containsKey(headNodeId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getEdgeWeight(int tailNodeId, int headNodeId) {
        if (!hasEdge(tailNodeId, headNodeId)) {
            return Double.NaN;
        }

        return childMap.get(tailNodeId).get(headNodeId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean removeEdge(int tailNodeId, int headNodeId) {
        if (!childMap.containsKey(tailNodeId)) {
            return false;
        }

        if (!childMap.get(tailNodeId).containsKey(headNodeId)) {
            return false;
        }

        childMap .get(tailNodeId).remove(headNodeId);
        parentMap.get(headNodeId).remove(tailNodeId);
        modificationCount++;
        edges--;
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<Integer> getChildrenOf(int nodeId) {
        if (!childMap.containsKey(nodeId)) {
            return Collections.<Integer>emptySet();
        }

        return Collections.
                <Integer>unmodifiableSet(childMap.get(nodeId).keySet());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<Integer> getParentsOf(int nodeId) {
        if (!parentMap.containsKey(nodeId)) {
            return Collections.<Integer>emptySet();
        }

        return Collections.
                <Integer>unmodifiableSet(parentMap.get(nodeId).keySet());
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
    }
}