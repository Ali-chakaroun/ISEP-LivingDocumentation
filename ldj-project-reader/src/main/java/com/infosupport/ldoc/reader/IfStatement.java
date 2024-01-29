package com.infosupport.ldoc.reader;

import java.util.stream.Stream;

/**
 * Interface for class of useful methods when dealing with IfStatement nodes.
 */
public interface IfStatement extends Statement {

  /**
   * Retrieve the if-else sections of the if statement.
   *
   * @return Stream of if-else sections.
   */
  Stream<IfElseSection> sections();

}
