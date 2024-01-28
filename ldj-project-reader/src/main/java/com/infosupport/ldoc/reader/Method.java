package com.infosupport.ldoc.reader;

import java.util.stream.Stream;

/**
 * Interface for class of useful methods when dealing with Method nodes.
 */
public interface Method extends HasName, HasModifiers, HasComment, HasAttributes, Node {

  /**
   * Retrieve return type of the method.
   *
   * @return Return type string.
   */
  String returnType();

  /**
   * Retrieve the parameters of the method.
   *
   * @return Stream of parameters.
   */
  Stream<Parameter> parameters();

  /**
   * Retrieve the statements of the method.
   *
   * @return Stream of statements.
   */
  Stream<Statement> statements();
}
