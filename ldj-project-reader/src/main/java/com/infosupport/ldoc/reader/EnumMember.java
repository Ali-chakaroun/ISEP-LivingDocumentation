package com.infosupport.ldoc.reader;

import java.util.stream.Stream;

public interface EnumMember extends HasName, HasModifiers, HasComment, HasAttributes, Node {

  String value();

  Stream<Argument> arguments();
}
