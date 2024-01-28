package com.infosupport.ldoc.reader;

/**
 * Interface for class of useful methods when dealing with Assignment nodes.
 */
public interface Assignment extends Statement {

  /**
   * Retrieve the left-hand side of the assignment.
   *
   * @return String containing the left-hand side of the assignment.
   */
  String left();

  /**
   * Retrieve the operator of the assignment.
   *
   * @return String containing the operator of the assignment.
   */
  String operator();

  /**
   * Retrieve the right-hand side of the assignment.
   *
   * @return String containing the right-hand side of the assignment.
   */
  String right();
}
