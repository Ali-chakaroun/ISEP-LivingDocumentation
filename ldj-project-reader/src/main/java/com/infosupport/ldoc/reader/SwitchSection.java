package com.infosupport.ldoc.reader;

import java.util.List;
import java.util.stream.Stream;

/**
 * Interface for class of useful methods when dealing with SwitchSection nodes.
 */
public interface SwitchSection extends Node {

  /**
   * Retrieve the case labels for this section of the switch statement.
   *
   * @return List of label strings.
   */
  List<String> labels();

  /**
   * Retrieve the statements in this section of the switch statement.
   *
   * @return Stream of statements.
   */
  Stream<Statement> statements();

}
