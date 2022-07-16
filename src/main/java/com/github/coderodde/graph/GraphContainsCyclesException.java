package com.github.coderodde.graph;

import java.util.Objects;

/**
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6 ()
 * @since 1.6 ()
 */
public class GraphContainsCyclesException extends Exception {

    public GraphContainsCyclesException(String message) {
        super(Objects.requireNonNull(message));
    }
}
