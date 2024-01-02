package com.infosupport.ldoc.reader;

public interface Parameter extends HasName, HasType, HasAttributes, Node {

  boolean hasDefaultValue();
}
