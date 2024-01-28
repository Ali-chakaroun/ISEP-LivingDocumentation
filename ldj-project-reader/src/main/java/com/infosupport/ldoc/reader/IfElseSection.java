package com.infosupport.ldoc.reader;

import java.util.stream.Stream;

/**
 * Interface for class of useful methods when dealing with IfElseSection nodes.
 */
public interface IfElseSection extends Node {

  /**
   * Retrieve the if condition of the if-else section.
   *
   * @return String with condition.
   */
  String condition();

  /**
   * Retrieve the statements of the if-else section.
   *
   * @return Stream with statements.
   */
  Stream<Statement> statements();

}
