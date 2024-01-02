package com.infosupport.ldoc.reader;

import java.util.List;
import java.util.stream.Stream;

public interface Type extends HasName, HasModifiers, HasComment, HasAttributes, Node {

  List<String> basetypes();

  Stream<Field> fields();

  Stream<Constructor> constructors();

  String fullName();

  Stream<Method> methods();

  default Stream<Method> methodsWithName(String name) {
    return methods().filter(m -> m.name().equals(name));
  }

  Stream<Property> properties();

  Stream<EnumMember> enumMembers();

  Stream<Event> events();
}
