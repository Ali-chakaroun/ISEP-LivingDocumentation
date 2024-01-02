package com.infosupport.ldoc.reader;

import java.util.stream.Stream;

public interface Constructor extends HasName, HasModifiers, HasComment, HasAttributes, Node {

  Stream<Parameter> parameters();

  Stream<Statement> statements();
}
