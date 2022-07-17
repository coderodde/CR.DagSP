package com.github.coderodde.graph.benchmark;

import com.github.coderodde.graph.PathDoesNotExistException;
import com.github.coderodde.graph.benchmark.DemoDagProvider.DemoData;
import com.github.coderodde.graph.impl.DirectedGraph;
import com.github.coderodde.graph.impl.DirectedGraph.Path;
import com.github.coderodde.graph.sp.AbstractDagShortestPathQueryRunner;
import com.github.coderodde.graph.sp.AbstractGraphPreprocessor;
import com.github.coderodde.graph.sp.impl.DFSGraphPreprocessor;
import com.github.coderodde.graph.sp.impl.IndexingPreprocessingDagShortestPathQueryRunner;
import com.github.coderodde.graph.sp.impl.KahnsGraphPreprocessor;
import com.github.coderodde.graph.sp.impl.NaivePreprocessingDagShortestPathQueryRunner;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6 ()
 * @since 1.6 ()
 */
public class DagShortestPathDemo {
    
    private static final class ShortestPathQuery {
        private final Integer sourceNode;
        private final Integer targetNode;

        public ShortestPathQuery(DemoData demoData, Random random) {
            int layers = demoData.graphLayers.size();
            int layerIndex1 = random.nextInt(layers);
            int layerIndex2 = random.nextInt(layers);
            int startLayerIndex = Math.min(layerIndex1, layerIndex2);
            int endLayerIndex = Math.max(layerIndex1, layerIndex2);
            
            List<Integer> layer1 = demoData.graphLayers.get(startLayerIndex);
            List<Integer> layer2 = demoData.graphLayers.get(endLayerIndex);
            
            this.sourceNode = choose(layer1, random);
            this.targetNode = choose(layer2, random);
        }
        
        public Integer getSourceNode() {
            return sourceNode;
        }
        
        public Integer getTargetNode() {
            return targetNode;
        }
    }
    
    private static final class SearchResult {
        private final Path path;
        private final long duration;
        private final String algorithmName;
        
        SearchResult(Path path, long duration, String algorithmName) {
            this.path = path;
            this.duration = duration;
            this.algorithmName = algorithmName;
        }
        
        public Path getPath() {
            return path;
        }
        
        public long getDuration() {
            return duration;
        }
        
        public String getAlgorithmName() {
            return algorithmName;
        }
    }
    
    public static void main(String[] args) {
        long seed = System.currentTimeMillis();
        Random random = new Random(seed);
        System.out.println("<<< Seed = " + seed + " >>>");
        DemoData demoData = DemoDagProvider.getDemoData(random);
        
        System.out.println("<<< Warmup >>>");
        runDemo(demoData, random);
        System.out.println();
        System.out.println("<<< Benchmarking >>>");
        runDemo(demoData, random);
    }
    
    private static void runDemo(DemoData demoData, Random random) {
        System.out.println("--- Normal search ---");
        runNormal(demoData, random);
        System.out.println();
        System.out.println("--- Unreachable search ---");
        runUnreachable(demoData, random);
    }
    
    private static void runNormal(DemoData demoData, Random random) {
        
    }
    
    private static void runUnreachable(DemoData demoData, Random random) {
        DirectedGraph directedGraph = demoData.graph;
        Integer isolatedNode = demoData.isolatedNode;
        List<Integer> nodes = new ArrayList<>(directedGraph.getAllNodes());
        Integer sourceNode = choose(nodes, random);
        
        AbstractGraphPreprocessor dfsPreprocessor = 
                new DFSGraphPreprocessor(directedGraph);
        
        AbstractGraphPreprocessor kahnsPreprocessor = 
                new KahnsGraphPreprocessor(directedGraph);
        
        long startTime = System.currentTimeMillis();
        
        dfsPreprocessor.preprocessGraph();
        
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        
        System.out.println(
                dfsPreprocessor.getClass().getSimpleName() 
                        + " in " 
                        + duration 
                        + " ms.");
        
        startTime = System.currentTimeMillis();
        
        kahnsPreprocessor.preprocessGraph();
        
        endTime = System.currentTimeMillis();
        duration = endTime - startTime;
        
        System.out.println(
                kahnsPreprocessor.getClass().getSimpleName() 
                        + " in " 
                        + duration 
                        + " ms.");
        
        AbstractDagShortestPathQueryRunner naiveDFSRunner = 
                new NaivePreprocessingDagShortestPathQueryRunner(
                        directedGraph, 
                        dfsPreprocessor);
        
        AbstractDagShortestPathQueryRunner naiveKahnsRunner = 
                new NaivePreprocessingDagShortestPathQueryRunner(
                        directedGraph, 
                        kahnsPreprocessor);
        
        AbstractDagShortestPathQueryRunner indexingDFSRunner = 
                new IndexingPreprocessingDagShortestPathQueryRunner(
                        directedGraph, 
                        dfsPreprocessor);
        
        AbstractDagShortestPathQueryRunner indexingKahnsRunner = 
                new IndexingPreprocessingDagShortestPathQueryRunner(
                        directedGraph, 
                        kahnsPreprocessor);
        
        List<SearchResult> searchResults = new ArrayList<>(4);
        
        searchResults.add(search(naiveDFSRunner, sourceNode, isolatedNode));
        searchResults.add(search(naiveKahnsRunner, sourceNode, isolatedNode));
        searchResults.add(search(indexingDFSRunner, sourceNode, isolatedNode));
        searchResults.add(search(indexingKahnsRunner, 
                                 sourceNode, 
                                 isolatedNode));
        
        printTerminalNodes(sourceNode, isolatedNode);
        print(searchResults);
    }
    
    private static void printTerminalNodes(Integer sourceNode, 
                                           Integer targetNode) {
        System.out.println("Source node: " + sourceNode);
        System.out.println("Target node: " + targetNode);
    }
    
    private static void print(List<SearchResult> searchResults) {
        for (SearchResult searchResult : searchResults) {
            System.out.print(searchResult.getAlgorithmName() + " ");
            Path path = searchResult.getPath();
            
            if (path == null) {
                System.out.print("Target unreachable. ");
            } else {
                System.out.print("Path cost: " + path.getTotalCost() + ". ");
            }
            
            System.out.println("Duration: " + searchResult.duration + "^ms.");
        }
    }
    
    private static SearchResult search(
            AbstractDagShortestPathQueryRunner runner, 
            Integer sourceNode, 
            Integer targetNode) {
        
        String algorithmName = runner.getClass().getSimpleName();
        
        try {
            long startTime = System.currentTimeMillis(); 
            Path path = runner.queryShortestPath(sourceNode, targetNode);
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            return new SearchResult(path, duration, algorithmName);
        } catch (PathDoesNotExistException ex) {
            return new SearchResult(null, -1L, algorithmName);
        }
    }
    
    private static <T> T choose(List<T> list, Random random) {
        return list.get(random.nextInt(list.size()));
    }
}
