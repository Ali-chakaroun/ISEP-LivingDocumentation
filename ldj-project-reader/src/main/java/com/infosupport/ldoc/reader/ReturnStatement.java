package com.infosupport.ldoc.reader;

/**
 * Interface for class of useful methods when dealing with ReturnStatements nodes.
 */
public interface ReturnStatement extends Statement {

  /**
   * Retrieve the expression of the return statement.
   *
   * @return expression string.
   */
  String expression();
}
