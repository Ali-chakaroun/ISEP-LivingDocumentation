package com.infosupport.ldoc.reader;

import java.util.stream.Stream;

public interface EnumMember extends Named, Modified, Documented, Attributed, Node {

  String value();

  Stream<Argument> arguments();
}
