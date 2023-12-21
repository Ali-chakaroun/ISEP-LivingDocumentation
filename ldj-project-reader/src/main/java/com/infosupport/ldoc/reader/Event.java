package com.infosupport.ldoc.reader;

import java.util.stream.Stream;

public interface Event extends Named, Typed, Modified, Node {

  String initializer();

  Stream<Attribute> attributes();

  DocumentationComment documentationComment();
}
