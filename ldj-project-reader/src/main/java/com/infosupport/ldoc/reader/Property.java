package com.infosupport.ldoc.reader;

public interface Property extends Named, Typed, Modified, Documented, Attributed, Node {

  String initializer();
}
