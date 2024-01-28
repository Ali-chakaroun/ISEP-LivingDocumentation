package com.infosupport.ldoc.reader;

import java.util.stream.Stream;

/**
 * Interface for class of useful methods when dealing with Constructor nodes.
 */
public interface Constructor extends HasName, HasModifiers, HasComment, HasAttributes, Node {

  /**
   * Retrieve the parameters of the constructor node.
   *
   * @return Stream of {@link Parameter}.
   */
  Stream<Parameter> parameters();

  /**
   * Retrieve the statements of the constructor node.
   *
   * @return Stream of {@link Statement}.
   */
  Stream<Statement> statements();
}
