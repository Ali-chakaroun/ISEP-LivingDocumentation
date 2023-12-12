package com.infosupport.ldoc.support;

import java.util.stream.Stream;

public interface Project extends Node {
  Stream<Type> allTypes();

  Stream<Class> classes();
  Stream<Interface> interfaces();
  Stream<Enum> enums();
  Stream<Struct> structs();
}
