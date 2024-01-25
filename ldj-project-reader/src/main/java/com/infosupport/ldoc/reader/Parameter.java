package com.infosupport.ldoc.reader;

/**
 * Interface for class of useful methods when dealing with Parameter nodes.
 */
public interface Parameter extends HasName, HasType, HasAttributes, Node {

  /**
   * Check if the parameter has a default value.
   *
   * @return true if parameter has default value, false if not.
   */
  boolean hasDefaultValue();
}
