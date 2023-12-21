package com.infosupport.ldoc.reader;

public interface Field extends Named, Typed, Modified, Documented, Node {

  String initializer();
}
