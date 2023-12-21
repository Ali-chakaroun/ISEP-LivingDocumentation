package com.infosupport.ldoc.reader;

import java.util.stream.Stream;

public interface Constructor extends Named, Modified, Node {

  Stream<Parameter> parameters();

  Stream<Statement> statements();

  Stream<Attribute> attributes();

  DocumentationComment documentationComment();

}
