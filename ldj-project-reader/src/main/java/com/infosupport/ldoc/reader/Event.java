package com.infosupport.ldoc.reader;

import java.util.stream.Stream;

public interface Event extends Named, Typed, Modified, Documented, Node {

  String initializer();

  Stream<Attribute> attributes();
}
