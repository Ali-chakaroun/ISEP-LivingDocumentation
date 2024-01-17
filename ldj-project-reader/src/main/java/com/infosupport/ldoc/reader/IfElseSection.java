package com.infosupport.ldoc.reader;

import java.util.stream.Stream;

/**
 * Interface for class of useful methods when dealing with IfElseSection nodes.
 */
public interface IfElseSection extends Node {

  String condition();

  Stream<Statement> statements();

}
