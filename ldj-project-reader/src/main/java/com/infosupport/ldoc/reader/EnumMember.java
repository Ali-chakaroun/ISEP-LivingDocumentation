package com.infosupport.ldoc.reader;

import java.util.stream.Stream;

/**
 * Interface for class of useful methods when dealing with EnumerationMember nodes.
 */
public interface EnumMember extends HasName, HasModifiers, HasComment, HasAttributes, Node {

  /**
   * Retrieve the value of the enumeration member.
   * @return String with value.
   */
  String value();

  /**
   * Retrieve the arguments of the enumeration member.
   * @return Stream of {@link Argument}.
   */
  Stream<Argument> arguments();
}
