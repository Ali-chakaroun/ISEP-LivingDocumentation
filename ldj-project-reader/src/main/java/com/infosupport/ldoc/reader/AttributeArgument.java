package com.infosupport.ldoc.reader;

/**
 * Interface for class of useful methods when dealing with AttributeArgument nodes.
 */
public interface AttributeArgument extends HasName, HasType, Node {

  /**
   * Retrieve the value of the attribute argument.
   * @return String containing the argument value.
   */
  String value();

}
