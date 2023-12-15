package com.infosupport.ldoc.reader;

import java.util.stream.Stream;

public interface Event extends Node {

  String name();

  String type();

  String initializer();

  Stream<Attribute> attributes();

  DocumentationComment documentationComment();
}
