package com.infosupport.ldoc.reader;

import java.util.stream.Stream;

public interface Method extends Node {

  String name();

  String returnType();

  Stream<Parameter> parameters();

  Stream<Statement> statements();

  Stream<Attribute> attributes();

  DocumentationComment documentationComment();

}
