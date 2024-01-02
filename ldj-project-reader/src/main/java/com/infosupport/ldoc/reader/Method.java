package com.infosupport.ldoc.reader;

import java.util.stream.Stream;

public interface Method extends HasName, HasModifiers, HasComment, HasAttributes, Node {

  String returnType();

  Stream<Parameter> parameters();

  Stream<Statement> statements();
}
