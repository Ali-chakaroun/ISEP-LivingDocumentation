package com.infosupport.ldoc.reader;

import java.util.List;
import java.util.stream.Stream;

/**
 * Interface for class of useful methods when dealing with Type nodes.
 */
public interface Type extends HasName, HasModifiers, HasComment, HasAttributes, Node {

  /**
   * Retrieve all base types (superclasses, implemented interfaces), if any.
   *
   * @return Stream of fully qualified names of base types, or null.
   */
  List<String> baseTypes();

  /**
   * Retrieve all fields.
   *
   * @return Stream of fields.
   */
  Stream<Field> fields();

  /**
   * Retrieve all constructors.
   *
   * @return Stream of constructors.
   */
  Stream<Constructor> constructors();

  /**
   * Retrieve the fully qualified name.
   *
   * @return full name string.
   */
  String fullName();

  /**
   * Retrieve all methods.
   *
   * @return Stream of methods.
   */
  Stream<Method> methods();

  /**
   * Retrieve all methods with the given name.
   *
   * @param name string name to look for.
   * @return Stream of methods.
   */
  default Stream<Method> methodsWithName(String name) {
    return methods().filter(m -> m.name().equals(name));
  }

  /**
   * Retrieve all properties.
   *
   * @return Stream of properties.
   */
  Stream<Property> properties();

  /**
   * Retrieve all enum members.
   *
   * @return Stream of enum members.
   */
  Stream<EnumMember> enumMembers();

  /**
   * Retrieve all events.
   *
   * @return Stream of events.
   */
  Stream<Event> events();
}
