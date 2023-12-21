package com.infosupport.ldoc.reader;

import java.util.stream.Stream;

public interface Parameter extends Named, Typed, Node {

  Stream<Attribute> attributes();

  boolean hasDefaultValue();
}
