package com.infosupport.ldoc.support;

import java.util.stream.Stream;

public interface Class extends Type {

  String fullName();

  Stream<Method> methods();

}
