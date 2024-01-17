package com.infosupport.ldoc.reader;

import java.util.stream.Stream;

/**
 * Interface for class of useful methods when dealing with IfStatement nodes.
 */
public interface IfStatement extends Statement {

  Stream<IfElseSection> sections();

}
