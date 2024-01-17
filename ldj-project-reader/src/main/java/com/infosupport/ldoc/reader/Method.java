package com.infosupport.ldoc.reader;

import java.util.stream.Stream;

/**
 * Interface for class of useful methods when dealing with Method nodes.
 */
public interface Method extends HasName, HasModifiers, HasComment, HasAttributes, Node {

  String returnType();

  Stream<Parameter> parameters();

  Stream<Statement> statements();
}
