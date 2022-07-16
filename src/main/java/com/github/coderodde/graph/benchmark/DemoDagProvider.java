package com.github.coderodde.graph.benchmark;

import com.github.coderodde.graph.impl.DirectedGraph;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DemoDagProvider {
    
    private static final int LAYERS = 100_000;
    private static final int MINIMUM_LAYER_SIZE = 10;
    private static final int MAXIMUM_LAYER_SIZE = 20;
    private static final int MAXIMUM_LAYER_JUMP = 4;

    public static final class DemoData {
        public DirectedGraph graph;
        public List<List<Integer>> graphLayers;
        public List<Integer> unreachableNodes;
    }
    
    private static void getDagLayers(DemoData demoData, Random random) {
        DirectedGraph dag = new DirectedGraph();
        List<List<Integer>> layers = new ArrayList<>(LAYERS);
        int node = 0;
        
        for (int layerNumber = 0; layerNumber < LAYERS; layerNumber++) {
            int layerSize = random.nextInt(MINIMUM_LAYER_SIZE, 
                                           MAXIMUM_LAYER_SIZE + 1);
            
            List<Integer> layer = new ArrayList<>(layerSize);
            
            for (int i = 0; i < layerSize; i++, node++) {
                layer.add(node);
            }
            
            layers.add(layer);
        }
        
        demoData.graphLayers = layers;
        demoData.graph = dag;
    }
    
    private static void createLayerArcs(DemoData demoData, Random random) {
        
    }
}
