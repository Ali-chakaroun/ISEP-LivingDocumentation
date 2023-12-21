package com.infosupport.ldoc.reader;

import java.util.List;
import java.util.stream.Stream;

public interface Type extends Named, Modified, Documented, Node {

  List<String> basetypes();

  Stream<Field> fields();

  Stream<Constructor> constructors();

  String fullName();

  Stream<Method> methods();

  Stream<Property> properties();

  Stream<Attribute> attributes();

  Stream<EnumMember> enumMembers();

  Stream<Event> events();
}
