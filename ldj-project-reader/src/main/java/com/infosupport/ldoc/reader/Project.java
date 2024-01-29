package com.infosupport.ldoc.reader;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * Interface for class of useful methods when dealing with Project nodes.
 */
public interface Project extends Node {

  /**
   * Retrieve all 'type' nodes in the project.
   *
   * @return Stream of types.
   */
  Stream<Type> allTypes();

  /**
   * Find the Type with this (fully qualified or unqualified) name, if there is one.
   *
   * @param name type to look for
   * @return Optional Type
   */
  Optional<Type> type(String name);

  /**
   * Retrieve all class nodes in the project.
   *
   * @return Stream of classes.
   */
  Stream<Class> classes();

  /**
   * Retrieve all interface nodes in the project.
   *
   * @return Stream of interfaces.
   */
  Stream<Interface> interfaces();

  /**
   * Retrieve all enum nodes in the project.
   *
   * @return Stream of enums.
   */
  Stream<Enum> enums();

  /**
   * Retrieve all struct nodes in the project.
   *
   * @return Stream of structs.
   */
  Stream<Struct> structs();
}
