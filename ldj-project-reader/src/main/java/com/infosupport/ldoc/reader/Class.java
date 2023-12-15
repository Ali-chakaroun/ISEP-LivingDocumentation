package com.infosupport.ldoc.reader;

import java.util.stream.Stream;

public interface Class extends Type {

  String fullName();

  Stream<Method> methods();

}
