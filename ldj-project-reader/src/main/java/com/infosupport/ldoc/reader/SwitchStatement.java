package com.infosupport.ldoc.reader;

import java.util.stream.Stream;

/**
 * Interface for class of useful methods when dealing with SwitchStatement nodes.
 */
public interface SwitchStatement extends Statement {

  /**
   * Retrieve the switch expression.
   *
   * @return String with expression.
   */
  String expression();

  /**
   * Retrieve the switch sections of the statement.
   *
   * @return Stream of switch sections.
   */
  Stream<SwitchSection> sections();

}
