package com.infosupport.ldoc.reader;

/**
 * Interface for class of useful methods when dealing with Argument nodes.
 */
public interface Argument extends HasType, Node {

  /**
   * Retrieve the text (the value) of the argument.
   * @return String of the text holding the value of the argument.
   */
  String text();
}
