package com.infosupport.ldoc.reader;

public interface Event extends Named, Typed, Modified, Documented, Attributed, Node {

  String initializer();
}
