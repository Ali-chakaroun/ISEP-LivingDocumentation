package com.infosupport.ldoc.reader;

public interface Parameter extends Named, Typed, Attributed, Node {

  boolean hasDefaultValue();
}
