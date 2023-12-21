package com.infosupport.ldoc.reader;

import java.util.stream.Stream;

public interface Method extends Named, Modified, Node {

  String returnType();

  Stream<Parameter> parameters();

  Stream<Statement> statements();

  Stream<Attribute> attributes();

  DocumentationComment documentationComment();

}
