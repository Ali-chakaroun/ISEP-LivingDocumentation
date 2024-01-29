package com.infosupport.ldoc.reader;

import java.util.stream.Stream;

/**
 * Interface for class of useful methods when dealing with Attribute nodes.
 */
public interface Attribute extends HasName, HasType, Node {

  /**
   * Retrieve the arguments of the attribute.
   *
   * @return Stream of {@link AttributeArgument}.
   */
  Stream<AttributeArgument> arguments();

}
