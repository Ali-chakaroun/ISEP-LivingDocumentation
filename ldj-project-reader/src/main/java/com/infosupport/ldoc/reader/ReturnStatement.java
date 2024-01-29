package com.infosupport.ldoc.reader;

/**
 * Interface for class of useful methods when dealing with ReturnStatements nodes.
 */
public interface ReturnStatement extends Statement {

  /**
   * Retrieve the expression of the return statement.
   *
   * @return expression string, which may be empty for statements like <code>return;</code>.
   */
  String expression();
}
