package com.infosupport.ldoc.reader;

import java.util.stream.Stream;

/**
 * Interface for class of useful methods when dealing with nodes that have attributes.
 */
public interface HasAttributes {

  /**
   * Retrieve attributes.
   *
   * @return Stream of attributes.
   */
  Stream<Attribute> attributes();

  /**
   * Find and retrieve all attributes of the given type.
   *
   * @param fullNameOfType Fully qualified attribute type.
   * @return Stream of attributes that match the given type.
   */
  default Stream<Attribute> attributesOfType(String fullNameOfType) {
    return attributes().filter(attr -> attr.type().equals(fullNameOfType));
  }

  /**
   * Check if an attribute of the given type exists.
   *
   * @param fullNameOfType Fully qualified attribute type.
   * @return true if a matching attribute exists, false if not.
   */
  default boolean hasAttribute(String fullNameOfType) {
    return attributes().anyMatch(attr -> attr.type().equals(fullNameOfType));
  }
}
