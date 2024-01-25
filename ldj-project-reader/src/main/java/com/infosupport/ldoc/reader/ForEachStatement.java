package com.infosupport.ldoc.reader;

import java.util.stream.Stream;

/**
 * Interface for class of useful methods when dealing with ForEachStatement nodes.
 */
public interface ForEachStatement extends Statement {

  /**
   * Retrieve the iteration expression of the foreach statement.
   *
   * @return String with expression.
   */
  String expression();

  /**
   * Retrieve the statements of the foreach statement.
   *
   * @return Stream of statement strings.
   */
  Stream<Statement> statements();

}
