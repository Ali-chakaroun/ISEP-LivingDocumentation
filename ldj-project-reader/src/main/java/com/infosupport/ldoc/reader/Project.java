package com.infosupport.ldoc.reader;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * Interface for class of useful methods when dealing with Project nodes.
 */
public interface Project extends Node {

  Stream<Type> allTypes();

  /** Find the Type with this (fully qualified or unqualified) name, if there is one. */
  Optional<Type> type(String name);

  Stream<Class> classes();

  Stream<Interface> interfaces();

  Stream<Enum> enums();

  Stream<Struct> structs();
}
