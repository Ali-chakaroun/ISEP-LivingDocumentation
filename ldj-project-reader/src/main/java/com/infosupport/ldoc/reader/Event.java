package com.infosupport.ldoc.reader;

/**
 * Interface for class of useful methods when dealing with Event nodes.
 */
public interface Event extends HasName, HasType, HasModifiers, HasComment, HasAttributes, Node {

  /**
   * Retrieve the initial value of the event.
   *
   * @return String with value.
   */
  String initializer();
}
