package com.github.coderodde.graph;

/**
 * This class implements the exception type thrown when trying to topologically
 * sort the graph.
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6 (Jul 16, 2022)
 * @since 1.6 (Jul 16, 2022)
 */
public class GraphContainsCyclesException extends Exception {

    public GraphContainsCyclesException() {
        super("The graph contains cycles.");
    }
}
