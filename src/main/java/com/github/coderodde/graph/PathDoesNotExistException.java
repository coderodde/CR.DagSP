package com.github.coderodde.graph;

/**
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6 ()
 * @since 1.6 ()
 */
public class PathDoesNotExistException extends RuntimeException {

    public PathDoesNotExistException(Integer sourceNode, Integer targetNode) {
        super("A path from " 
                + sourceNode
                + " to " 
                + targetNode 
                + " does not exist.");
    }
}
