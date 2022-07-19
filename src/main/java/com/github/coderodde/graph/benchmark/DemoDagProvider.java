package com.github.coderodde.graph.benchmark;

import com.github.coderodde.graph.impl.DirectedGraph;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DemoDagProvider {
    
    static final int LAYERS = 20_000;
    static final int MINIMUM_LAYER_SIZE = 10;
    static final int MAXIMUM_LAYER_SIZE = 20;
    static final int MAXIMUM_LAYER_JUMP = 4;
    static final int ARCS_PER_LAYER = 50;

    public static final class DemoData {
        public final DirectedGraph graph;
        public final List<List<Integer>> graphLayers;
        public Integer isolatedNode;
        
        DemoData() {
            this.graph = new DirectedGraph();
            this.graphLayers = new ArrayList<>(LAYERS);
        }
    }
    
    public static DemoData getDemoData(Random random) {
        DemoData demoData = new DemoData();
        getDagLayers(demoData, random);
        createLayerArcs(demoData, random);
        getIsolatedNode(demoData, random);
        return demoData;
    }
    
    private static void getDagLayers(DemoData demoData, Random random) {
        int node = 0;
        
        for (int layerNumber = 0; layerNumber < LAYERS; layerNumber++) {
            int layerSize = random.nextInt(MINIMUM_LAYER_SIZE, 
                                           MAXIMUM_LAYER_SIZE + 1);
            
            List<Integer> layer = new ArrayList<>(layerSize);
            
            for (int i = 0; i < layerSize; i++, node++) {
                layer.add(node);
            }
            
            demoData.graphLayers.add(layer);
        }
    }
    
    private static void getIsolatedNode(DemoData demoData, Random random) {
        int layerIndex = random.nextInt(demoData.graphLayers.size());
        List<Integer> layer = demoData.graphLayers.get(layerIndex);
        Integer isolatedNode = Integer.MAX_VALUE;
        demoData.graph.addNode(isolatedNode);
        layer.add(random.nextInt(layer.size()), isolatedNode);
        demoData.isolatedNode = isolatedNode;
    }
    
    private static void createLayerArcs(DemoData demoData, Random random) {
        int totalLayers = demoData.graphLayers.size();
        
        for (int layerIndex = 0; layerIndex < totalLayers - 1; layerIndex++) {
            createLayerArcsForLayer(demoData, random, layerIndex);
        }
    }
    
    private static void createLayerArcsForLayer(DemoData demoData,
                                                Random random,
                                                int layerIndex) {
        int totalLayers = demoData.graphLayers.size();
        List<Integer> sourceLayer = demoData.graphLayers.get(layerIndex);
        
        for (int i = 0; i < ARCS_PER_LAYER; i++) {
            Integer node = choose(sourceLayer, random);
            int nextLayerIndex = random.nextInt(1, MAXIMUM_LAYER_JUMP + 1);
            nextLayerIndex = Math.min(layerIndex + nextLayerIndex, 
                                      totalLayers - 1);
            
            List<Integer> nextLayer = demoData.graphLayers.get(nextLayerIndex);
            Integer nextNode = nextLayer.get(random.nextInt(nextLayer.size()));
            demoData.graph.addEdge(node, nextNode, random.nextDouble());
        }
    }
    
    private static <T> T choose(List<T> list, Random random) {
        return list.get(random.nextInt(list.size()));
    }
}
